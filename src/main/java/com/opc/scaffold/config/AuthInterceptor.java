package com.opc.scaffold.config;

import com.opc.scaffold.common.exception.BizException;
import com.opc.scaffold.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT 鉴权拦截器：校验 Authorization: Bearer <token>。
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    public AuthInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 放行预检请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String auth = request.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ") && auth.length() > 7) {
            try {
                String username = jwtUtil.parse(auth.substring(7));
                request.setAttribute("loginUser", username);
                return true;
            } catch (Exception e) {
                // token 无效，走下方 401
            }
        }
        throw new BizException(401, "未登录或登录已过期");
    }
}
