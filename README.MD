# Introduction
The source code is processed during the compilation period based on the SPI mechanism and the Java Annotation Processor function
# Architecture
```text
|----CoreProcessor--(Core processor, based on SPI mechanism, distributes tasks to specific processors according to the annotations to be processed)
|                                                                            
| 
|                                                                            
|----CustomProcessor--(Custom processor, capable of processing elements with specific annotations found by the core processor)-------


@SPIPlugin- Extended processor annotations, used to identify the annotations available for a dedicated processor and the minimum supported Java version

@Libraries- Used for initializing global class libraries, such as: velocity

CoreProcessor.properties-- (Core configuration file, used to configure the implementation classes of the dedicated Processor, other support options for Java Annotation Processor, and the minimum supported Java version)
```

# Start Quickly
Add dependencies
```xml
<dependency>
    <groupId>com.javormio</groupId>
    <artifactId>javormio-processor</artifactId>
    <version>0.0 beta</version>
</dependency>
```
2. Add annotations
```java
public @interface Annotation1 {
String value();
}
```
3. Add a processor
#### The "value()" is the annotation handled by the dedicated processor, and sourceVersion is the minimum supported version of Java

```java
import io.github.maxwellnie.javormio.source.code.processor.CustomProcessor;
import io.github.maxwellnie.javormio.source.code.processor.ExtensionProcessor;
import io.github.maxwellnie.javormio.source.code.processor.Libraries;
import io.github.maxwellnie.javormio.source.code.processor.SPIPlugin;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import java.util.Set;


@SPIPlugin(value = Annotation1.class, sourceVersion = SourceVersion.RELEASE_8)
@Libraries(VelocityLibrary.class)
public class Annotation1Processor implements CustomProcessor {
    @Override
    public void handle(Set<? extends Element> element, ProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
        for (Element element : elements) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
                    "@Annotation1: " + element.getSimpleName() + ", element);
        }
    }
}
```
4. Add the configuration file CoreProcessor.properties

```java
extension-plugin-classes=Annotation1Processor
```
#### Of course, we can also add more dedicated processors
```java
extension-plugin-classes=Annotation1Processor,Annotation2Processor
```
6. Run
```shell
mvn clean compile
```
7. Operation Results
```shell
[the INFO] javormio - processor: 1.0.0. The SNAPSHOT: the process (the default) @ javormio - processor - demo -
[INFO] @Annotation1: Annotation1Processor
```