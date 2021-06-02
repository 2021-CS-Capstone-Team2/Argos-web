package com.capstone.common.config;

import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mobile.device.DeviceHandlerMethodArgumentResolver;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import net.rakugakibox.util.YamlResourceBundle;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(Locale.KOREA);
		return slr;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
		registry.addInterceptor(deviceResolverHandlerInterceptor());
	}

	@Bean("messageSource")
	public MessageSource messageSource(@Value("${spring.messages.basename}") String basename,
			@Value("${spring.messages.encoding}") String encoding) {
		YamlMessageSource ms = new YamlMessageSource();
		ms.setBasename(basename);
		ms.setDefaultEncoding(encoding);
		ms.setAlwaysUseMessageFormat(true);
		ms.setUseCodeAsDefaultMessage(true);
		ms.setFallbackToSystemLocale(true);
		return ms;
	}
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**"); // 나중에 우리꺼 사용하는 애들만 허락
	}
	
	@Bean
	public DeviceResolverHandlerInterceptor 
	        deviceResolverHandlerInterceptor() {
	    return new DeviceResolverHandlerInterceptor();
	}

	@Bean
	public DeviceHandlerMethodArgumentResolver 
	        deviceHandlerMethodArgumentResolver() {
	    return new DeviceHandlerMethodArgumentResolver();
	}

	@Override
	public void addArgumentResolvers(
	       List<HandlerMethodArgumentResolver> argumentResolvers) {
	   argumentResolvers.add(deviceHandlerMethodArgumentResolver());
	}

}

class YamlMessageSource extends ResourceBundleMessageSource {
	@Override
	protected ResourceBundle doGetBundle(String basename, Locale locale) throws MissingResourceException {
		return ResourceBundle.getBundle(basename, locale, YamlResourceBundle.Control.INSTANCE);
	}
}