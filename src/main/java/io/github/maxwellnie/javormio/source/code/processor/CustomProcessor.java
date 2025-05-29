package io.github.maxwellnie.javormio.source.code.processor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import java.util.Set;

/**
 * 扩展处理器
 *
 * @author Maxwell Nie
 */
public interface CustomProcessor {
    /**
     * 处理被遍历到的元素
     *
     * @param element
     * @param processingEnv
     * @param roundEnv
     */
    void process(Set<? extends Element> element, ProcessingEnvironment processingEnv, RoundEnvironment roundEnv);
}
