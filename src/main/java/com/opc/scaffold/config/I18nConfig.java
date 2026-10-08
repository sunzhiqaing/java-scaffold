package com.opc.scaffold.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

/**
 * 国际化（i18n）配置：
 * - MessageSource：加载 i18n/messages*.properties（UTF-8）
 * - LocaleResolver：自定义解析器（cookie 优先 + Accept-Language 地区自动检测）
 */
@Configuration
public class I18nConfig {

    public static final String COOKIE_NAME = "lang";
    public static final Locale DEFAULT_LOCALE = Locale.US;

    /** 支持的语言：中文 / 英文 / 日文 */
    public static final Locale[] SUPPORTED = {
            Locale.SIMPLIFIED_CHINESE, // zh_CN
            Locale.US,                 // en_US
            Locale.JAPAN               // ja_JP
    };

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("i18n/messages");
        source.setDefaultEncoding("UTF-8");
        return source;
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new CustomLocaleResolver();
    }
}
