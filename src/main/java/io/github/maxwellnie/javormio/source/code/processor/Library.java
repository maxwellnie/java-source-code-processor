package io.github.maxwellnie.javormio.source.code.processor;

/**
 * 对于属于全局的库进行初始化
 * @author Maxwell Nie
 */
public interface Library {
    void init() throws LibraryInitializationException;
    String getName();
}
