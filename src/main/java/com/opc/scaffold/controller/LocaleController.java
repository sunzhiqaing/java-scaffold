package com.opc.scaffold.controller;

import com.opc.scaffold.common.result.Result;
import com.opc.scaffold.config.I18nConfig;
import com.opc.scaffold.util.I18nUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 国际化（i18n）接口：
 * - 获取当前语言
 * - 支持的语言列表
 * - 切换语言（写入 cookie，下次访问持久生效）
 * - 当前语言演示（欢迎语）
 */
@Tag(name = "国际化演示")
@RestController
@RequestMapping("/api/i18n")
public class LocaleController {

    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    public LocaleController(MessageSource messageSource, LocaleResolver localeResolver) {
        this.messageSource = messageSource;
        this.localeResolver = localeResolver;
    }

    @Operation(summary = "获取当前语言")
    @GetMapping("/current")
    public Result<Map<String, Object>> current() {
        Locale locale = I18nUtil.getLocale();
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("language", locale.toLanguageTag());
        m.put("displayName", locale.getDisplayName(locale));
        return Result.success(m);
    }

    @Operation(summary = "支持的语言列表")
    @GetMapping("/languages")
    public Result<Map<String, String>> languages() {
        Map<String, String> map = new LinkedHashMap<>();
        for (Locale l : I18nConfig.SUPPORTED) {
            map.put(l.toLanguageTag(), l.getDisplayName(l));
        }
        return Result.success(map);
    }

    @Operation(summary = "切换语言（lang=zh_CN / en_US / ja_JP）")
    @PostMapping("/locale")
    public Result<Map<String, Object>> switchLocale(@RequestParam("lang") String lang,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) {
        Locale target = match(lang);
        if (target == null) {
            return Result.error(400, "Unsupported language: " + lang);
        }
        localeResolver.setLocale(request, response, target);
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("language", target.toLanguageTag());
        m.put("message", messageSource.getMessage("i18n.switched",
                new Object[]{target.getDisplayName(target)}, target));
        return Result.success(m);
    }

    @Operation(summary = "当前语言演示（欢迎语 / 应用名）")
    @GetMapping("/demo")
    public Result<Map<String, Object>> demo() {
        Locale locale = I18nUtil.getLocale();
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("language", locale.toLanguageTag());
        m.put("appName", messageSource.getMessage("app.name", null, locale));
        m.put("welcome", messageSource.getMessage("i18n.hello", null, locale));
        return Result.success(m);
    }

    private Locale match(String tag) {
        for (Locale l : I18nConfig.SUPPORTED) {
            if (l.toLanguageTag().equalsIgnoreCase(tag) || l.toString().equalsIgnoreCase(tag)) {
                return l;
            }
        }
        return null;
    }
}
