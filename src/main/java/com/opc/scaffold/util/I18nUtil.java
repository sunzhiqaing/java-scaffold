package com.opc.scaffold.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.Locale;

/**
 * 国际化工具：获取当前请求的语言环境。
 * 统一走 RequestContextUtils（经 LocaleResolver 解析），
 * 保证"首次按地区自动检测 + cookie 手动切换"都生效。
 */
public final class I18nUtil {

    private I18nUtil() {
    }

    /**
     * 获取当前请求的 Locale：
     * - 有 cookie（用户手动切换）→ 返回 cookie 中的语言
     * - 无 cookie → 按 Accept-Language 自动检测地区语言
     * - 兜底返回系统默认
     */
    public static Locale getLocale() {
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            Locale locale = RequestContextUtils.getLocale(request);
            if (locale != null) {
                return locale;
            }
        }
        return Locale.getDefault();
    }
}
