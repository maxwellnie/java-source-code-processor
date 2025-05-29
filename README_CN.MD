# 介绍
基于SPI机制与Java Annotation Processor功能在编译期处理源代码
# 架构
```text
|----CoreProcessor--(核心处理器，基于SPI机制，按照需要处理的注解来分发任务到专属处理器)<--
|                                                                            |
|                                                                            |扩展               
|                                                                            |
|----CustomProcessor--(专属处理器，能够处理核心处理器找到的带有具体注解的元素)-------


@SPIPlugin--扩展处理器注解，用于标识专属处理器可用的注解和最低支持的Java版本

@Libraries--用于初始化全局类库，如：velocity

CoreProcessor.properties---(核心配置文件，用于配置专属处理器的实现类、Java Annotation Processor的其他支持选项、支持的最低Java版本)
```

# 快速开始
1. 添加依赖
```xml
<dependency>
    <groupId>com.javormio</groupId>
    <artifactId>javormio-processor</artifactId>
    <version>0.0.beta</version>
</dependency>
```
2. 添加注解
```java
public @interface Annotation1 {
    String value();
}
```
3. 添加处理器
#### 这里的value()为专属处理器所处理的注解，sourceVersion为最低支持的Java版本

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
4. 添加配置文件CoreProcessor.properties

```java
extension-plugin-classes=Annotation1Processor
```
#### 当然，我们也可以添加多个专属处理器
```java
extension-plugin-classes=Annotation1Processor,Annotation2Processor
```
6. 运行
```shell
mvn clean compile
```
7. 运行结果
```shell
[INFO] --- javormio-processor:1.0.0.SNAPSHOT:process (default) @ javormio-processor-demo ---
[INFO] @Annotation1: Annotation1Processor
```