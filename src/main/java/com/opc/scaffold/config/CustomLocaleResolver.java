package com.opc.scaffold.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import java.util.Locale;

/**
 * 自定义语言解析器：
 * 1. 优先读取 cookie（用户手动切换的选择，持久化）
 * 2. 无 cookie 时，按请求 Accept-Language 头自动检测当前地区语言
 * 3. 都不匹配时回退默认语言（en_US）
 */
public class CustomLocaleResolver extends CookieLocaleResolver {

    public CustomLocaleResolver() {
        super(I18nConfig.COOKIE_NAME);
        setDefaultLocale(I18nConfig.DEFAULT_LOCALE);
    }

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        // 1. cookie 优先（用户手动选择）
        String lang = readCookie(request);
        if (StringUtils.hasText(lang)) {
            Locale fromCookie = matchSupported(lang);
            if (fromCookie != null) {
                return fromCookie;
            }
        }
        // 2. 无 cookie → 按 Accept-Language 地区自动检测
        Locale accept = request.getLocale();
        if (accept != null) {
            Locale matched = matchAccept(accept);
            if (matched != null) {
                return matched;
            }
        }
        return getDefaultLocale();
    }

    /** 校验 cookie 值是否在支持列表内 */
    private Locale matchSupported(String tag) {
        for (Locale l : I18nConfig.SUPPORTED) {
            if (l.toLanguageTag().equalsIgnoreCase(tag) || l.toString().equalsIgnoreCase(tag)) {
                return l;
            }
        }
        return null;
    }

    /** Accept-Language 匹配：先精确语言+国家，再仅语言 */
    private Locale matchAccept(Locale accept) {
        for (Locale l : I18nConfig.SUPPORTED) {
            if (l.equals(accept)) {
                return l;
            }
        }
        for (Locale l : I18nConfig.SUPPORTED) {
            if (l.getLanguage().equalsIgnoreCase(accept.getLanguage())) {
                return l;
            }
        }
        return null;
    }

    private String readCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (I18nConfig.COOKIE_NAME.equals(c.getName())) {
                    return c.getValue();
                }
            }
        }
        return null;
    }
}
