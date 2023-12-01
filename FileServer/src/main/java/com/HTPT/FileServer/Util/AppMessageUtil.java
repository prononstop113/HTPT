/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.HTPT.FileServer.Util;

import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

/**
 *
 * @author Hello
 */
public class AppMessageUtil {

    private static final AppMessageUtil APP_MESSAGE_UTIL = new AppMessageUtil();
    private final MessageSource messageSource;

    private AppMessageUtil() {
        try {
            ReloadableResourceBundleMessageSource source
                    = new ReloadableResourceBundleMessageSource();
            source.setBasenames(
                    "classpath:/message/error_resource",
                    "classpath:/message/response_resource"
            );
            source.setDefaultEncoding("UTF-8");
            messageSource = source;
        } catch (Exception e) {
            throw e;
        }
    }

    public static AppMessageUtil getInstance() {
        return APP_MESSAGE_UTIL;
    }

    public String getMessage(String key, Object[] args) {
        return this.messageSource.getMessage(key, args, Locale.US);
    }
}
