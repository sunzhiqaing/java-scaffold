package com.opc.scaffold.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * AOP 请求日志切面：为每个请求生成 traceId（MDC），记录方法、参数、耗时。
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    /** Controller 所有 public 方法 */
    @Pointcut("execution(public * com.opc.scaffold.controller..*.*(..))")
    public void controllerLog() {
    }

    @Around("controllerLog()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        // 生成并写入 traceId（配合 logging.pattern 输出）
        MDC.put("traceId", UUID.randomUUID().toString().replace("-", "").substring(0, 16));
        long start = System.currentTimeMillis();
        String clazz = pjp.getSignature().getDeclaringType().getSimpleName();
        String method = pjp.getSignature().getName();
        String params = argsToStr(pjp.getArgs());
        String uri = "?";
        try {
            ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (sra != null) {
                HttpServletRequest req = sra.getRequest();
                uri = req.getMethod() + " " + req.getRequestURI();
            }
        } catch (Exception ignored) {
        }
        try {
            Object result = pjp.proceed();
            log.info("请求完成 [{}] {}.{} 参数={} 耗时={}ms", uri, clazz, method, params, System.currentTimeMillis() - start);
            return result;
        } catch (Throwable e) {
            log.error("请求异常 [{}] {}.{} 参数={} 耗时={}ms 异常={}", uri, clazz, method, params, System.currentTimeMillis() - start, e.getMessage(), e);
            throw e;
        } finally {
            MDC.remove("traceId");
        }
    }

    private String argsToStr(Object[] args) {
        if (args == null || args.length == 0) {
            return "";
        }
        return Arrays.stream(args)
                .map(a -> {
                    if (a == null) return "null";
                    String s = String.valueOf(a);
                    return s.length() > 300 ? s.substring(0, 300) + "...(len=" + s.length() + ")" : s;
                })
                .collect(Collectors.joining(","));
    }
}
