package br.com.acoes.util;

import static java.util.Objects.isNull;
import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.Collection;
import java.util.function.Function;

public final class ObjectUtil {
	public static <T, R> R getIfExists(final T object, final Function<T, R> function) {
		return isNull(object) ? null : function.apply(object);
	}
	
	public static <T extends Collection<?>, R> R getIfExists(final T object, final Function<T, R> function) {
		return isEmpty(object) ? null : function.apply(object);
	}
}