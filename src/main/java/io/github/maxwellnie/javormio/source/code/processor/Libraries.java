package io.github.maxwellnie.javormio.source.code.processor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表示当前SPI插件依赖的全局库
 * @author Maxwell Nie
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Libraries {
    Class<? extends Library>[] value();
}
