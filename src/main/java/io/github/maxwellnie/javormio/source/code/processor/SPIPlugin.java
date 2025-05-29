package io.github.maxwellnie.javormio.source.code.processor;

import javax.lang.model.SourceVersion;
import java.lang.annotation.*;

/**
 * SPI插件注解
 * @author Maxwell Nie
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SPIPlugin {
    /**
     * 适用注解
     * @return String
     */
    Class<? extends Annotation> value();

    /**
     * 支持最低JDK版本
     * @return SourceVersion
     */
    SourceVersion sourceVersion() default SourceVersion.RELEASE_8;
}
