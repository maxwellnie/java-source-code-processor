package io.github.maxwellnie.javormio.source.code.processor;

/**
 * @author Maxwell Nie
 */
public class Info {
    String namespace;
    String message;

    public Info(String namespace, String message) {
        this.namespace = namespace;
        this.message = message;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "The \""+ namespace + "\" information: " + message;
    }
}
