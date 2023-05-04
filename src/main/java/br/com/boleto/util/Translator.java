package br.com.boleto.util;

import java.util.Locale;
import java.util.Optional;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Translator {
	public static String toLocale(String msgCode) {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("messages");
		messageSource.setDefaultEncoding("ISO-8859-1");
		messageSource.setUseCodeAsDefaultMessage(true);		
		Locale locale = Optional.ofNullable(LocaleContextHolder.getLocale()).orElse(new Locale("pt", "BR"));
		return messageSource.getMessage(msgCode, null, locale);
	}
}