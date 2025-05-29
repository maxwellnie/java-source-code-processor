package io.github.maxwellnie.javormio.source.code.processor;


import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.*;


/**
 * @author Maxwell Nie
 */
public class CoreProcessor extends AbstractProcessor {
    private final Map<String, List<CustomProcessor>> elementHandlersMap;
    private final Set<String> supportedOptions;
    private SourceVersion supportedSourceVersion;
    private Exception exception;
    private final String lineSeparator = System.lineSeparator();
    private final StringBuilder infos = new StringBuilder();

    public CoreProcessor() {
        elementHandlersMap = new HashMap<>();
        supportedOptions = new HashSet<>();
        supportedSourceVersion = SourceVersion.RELEASE_8;
        loadConfigurations();
    }

    /**
     * 加载配置
     */
    private void loadConfigurations() {
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("CoreProcessor.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            String options = properties.getProperty("supported-options");
            if (options != null)
                supportedOptions.addAll(Arrays.asList(options.split(",")));
            String sourceVersion = properties.getProperty("supported-source-version");
            if (sourceVersion != null)
                supportedSourceVersion = SourceVersion.valueOf(sourceVersion);
            String classNames = properties.getProperty("extension-plugin-classes");
            if (classNames == null)
                return;
            installExtensionSPIPlugin(classNames);
        } catch (Exception e) {
            exception = e;
        }
    }
    /**
     * 安装扩展SPI插件
     *
     * @param classNames
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    private void installExtensionSPIPlugin(String classNames) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, LibraryInitializationException {
        HashSet<String> libraryNames = new HashSet<>();
        for (String className : classNames.split(",")) {
            if (className != null) {
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(SPIPlugin.class)){
                    SPIPlugin spiPlugin = clazz.getAnnotation(SPIPlugin.class);
                    SourceVersion pluginSupportedSourceVersion = spiPlugin.sourceVersion();
                    if (pluginSupportedSourceVersion.compareTo(supportedSourceVersion) <= 0){
                        List<CustomProcessor> customProcessors = elementHandlersMap.computeIfAbsent(spiPlugin.value().getName(), k -> new LinkedList<>());
                        if (clazz.isAnnotationPresent(Libraries.class)){
                            Libraries  libraries = clazz.getAnnotation(Libraries.class);
                            for (Class<? extends Library> libraryClass : libraries.value()) {
                                Library library = libraryClass.getConstructor().newInstance();
                                if (!libraryNames.contains(library.getName())){
                                    libraryNames.add(library.getName());
                                    library.init();
                                }else
                                    infos.append(
                                            new Info(clazz.getName(),
                                                    "Library \"" + library.getName() + "\" has been registered")
                                    ).append(lineSeparator);
                            }
                        }
                        CustomProcessor customProcessor = (CustomProcessor) clazz.getConstructor().newInstance();
                        customProcessors.add(customProcessor);
                    }
                }
            }
        }
    }
    /**
     * 获取异常链
     *
     * @param throwable
     * @return String
     */
    private String getExceptionChain(Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        while (throwable != null) {
            sb.append(throwable.getClass().getName())
                    .append(": ")
                    .append(throwable.getMessage())
                    .append(" -> ");
            throwable = throwable.getCause();
        }
        return sb.toString();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (exception != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            exception.printStackTrace(pw);
            processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.ERROR,
                    "Processing failed: " + exception.getMessage() +
                            lineSeparator +"StackTrace: " + sw +
                            lineSeparator +"CauseChain: " + getExceptionChain(exception),
                    null
            );
        }
        if (infos.length() > 0)
            processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, infos.toString(), null);
        for (TypeElement annotation : annotations) {
            List<CustomProcessor> customProcessors = this.elementHandlersMap.get(annotation.getQualifiedName().toString());
            if (customProcessors != null) {
                for (CustomProcessor customProcessor : customProcessors)
                    customProcessor.process(roundEnv.getElementsAnnotatedWith(annotation), processingEnv, roundEnv);
            } else {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING,
                        "No handler for annotation " + annotation.getQualifiedName(), annotation);
            }
        }
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return elementHandlersMap.keySet();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return supportedSourceVersion;
    }

    @Override
    public Set<String> getSupportedOptions() {
        return supportedOptions;
    }
}
