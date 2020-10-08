# 初识Spring



# 一. 认识Spring家族

Spring框架是用来构架企业级应用的轻量级一站式解决方案，它解决的问题其实是大型项目中复杂对象的创建问题。这种复杂体现在有复杂依赖关系的对象创建问题上。开发人员需要自己编写大量的代码去维护这样复杂的关系，每次创建目标对象A，要先new一大堆A依赖的对象B、C、D等等，把它们装配完毕才能最终装配A对象，非常低效率。通过“依赖注入”和“控制反转”，Spring框架自动化了这个过程，控制反转是目的，依赖注入是手段，框架完成创建并装配。

但Spring框架本身需要开发人员做大量配置，也不轻松，所以后来在这个基础上诞生了SpringBoot框架。使用SpringBoot可以快速构建基于Spring的应用程序，既可以开箱即用，又可以按需改动。SpringMVC是在Spring这套思想的基础上构建的MVC框架，用于开发Web应用。

围绕Spring衍生出了一整个家族体系。SpringData用于数据访问，SpringWebFlux用于异步Web开发，SpringMVC用于快速开发web应用，SpringCloud提供配置管理、服务注册与发现、熔断和服务追踪等等，用于简化分布式系统开发。再比如Spring和Redis，Dubbo这些框架的结合，本质上是用Spring去帮这些框架解决和处理对象创建和相互之间的依赖关系注入，这些衍生项目固化了业界最佳实践，执行了最严苛的代码质量要求。



# 二. Spring快速上手

helloworld项目是参考官方文档写的快速上手项目，用SpringBoot框架写一个简单的demo来感受一下。首先通过 [start.spring.io](https://start.spring.io/)快速创建一个基于maven的项目框架，添加SpringWeb依赖。然后编写一个简单的代码，提供一个hello的GET接口，该接口演示了如何定义请求参数，包括名称和默认值。

# 三. 使用Maven构建Java项目

Maven是现在比较流行的Java构建工具，另一个后起之秀是Gradle。这里用一个简单的示例项目learnmaven来研究下如何使用Maven来管理Java项目的构建，目录结构如下：

```java
├── pom.xml
├── src
│   ├── main
│   │   └── java
│   │       └── hello
│   │           ├── Greeter.java
│   │           └── HelloWorld.java
│   └── test
│       └── java
│           └── hello
│               └── GreeterTest.java
```

pom.xml类似于C++的Makefile，是用来配置Maven的，一个最简单的pom文件如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.springframework</groupId>
    <artifactId>gs-maven</artifactId>
    <packaging>jar</packaging>
    <version>0.1.0</version>

    <properties>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.2.4</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <transformers>
                                <transformer
               implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                    <mainClass>hello.HelloWorld</mainClass>
                                </transformer>
                            </transformers>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

一些关键的项目配置如下：

* <modelVersion>：POM模型版本
* <groupId>：用逆向域名表示的组织名称
* <artifactId>：项目名称，最后用来命名生成的JAR或WAR包
* <version>：项目版本
* <packaging>：打包的文件格式，JAR或者WAR

Maven中有很多用来构建项目的生命周期目标。常见的有编译、打包和安装到本地仓库。使用`mvn compile`命令可以对代码进行编译，生成的.class文件存放在target/classes文件夹下。使用`mvn package`可以打包生成JAR文件，同样存放在target文件夹下，生成的JAR文件是按artifactId-version.jar格式命名的。使用`mvn install`命令将对代码进行编译、测试和打包一条龙服务，然后把它拷贝到本地依赖库，然后就可以让别的代码来使用了。

可以在Maven中定义对其他包的依赖，比如项目如果要使用第三方的时间日期库，可以在<project>下写入如下内容：

```xml
<dependencies>
		<dependency>
			<groupId>joda-time</groupId>
			<artifactId>joda-time</artifactId>
			<version>2.9.2</version>
		</dependency>
</dependencies>
```

有些依赖还可以通过<scope>指定范围：

* provided：该依赖用于编译项目代码，但在运行时由一个运行代码的容器提供支持；
* test：用来编译和测试的依赖，但不需要在代码运行时使用。

比如使用JUnit提供测试支持，可以这么写：

```xml
<dependency>
	<groupId>junit</groupId>
	<artifactId>junit</artifactId>
	<version>4.12</version>
	<scope>test</scope>
</dependency>
```

在`src/test/java/hello/GreeterTest.java`编写好相关测试代码后，就可以使用`mvn test`就可以运行测试代码得到测试结果。

```java
package hello;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

import org.junit.Test;

public class GreeterTest {

  private Greeter greeter = new Greeter();

  @Test
  public void greeterSaysHello() {
    assertThat(greeter.sayHello(), containsString("Hello"));
  }

}
```

