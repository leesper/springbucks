# Springbucks-jpa

# 一. 项目分析

主程序演示了项目的主要需求：

* 从数据库中读出所有存储的咖啡数据并打印到日志中；
* 查找名为“Latte”的（拿铁）咖啡；
* 若找到了，则为名为“Li Lei”（李雷）的顾客创建订单；
* 尝试将订单的状态从`INIT`改为`PAID`，查看日志（成功）；
* 尝试将订单的状态从`PAID`改回`INIT`，查看日志（失败）。

项目是按照`service-repository-model`的结构进行组织的。主程序通过调用相关service来实现上述需求。`CoffeeService`用于操作咖啡数据，`CoffeeOrderService`用于操作订单数据。

`CoffeeService`通过`CoffeeRepository`实现对咖啡数据的操作；`CoffeeOrderService`通过`CoffeeOrderRepository`实现对订单数据的操作。`CoffeeRepository`和`CoffeeOrderRepository`是两个继承了`JpaRepository`泛型接口的接口，其方法由spring-data-jpa自动生成。

`Repository`接口操作一系列的实体类，让实体类和数据库表之间建立对应关系。实体类`Coffee`包含“咖啡名称”和“单价”属性；`CoffeeOrder`包含“顾客姓名”、“咖啡列表”和“订单状态”属性，二者都继承自`BaseEntity`类，后者包含id，创建时间和修改时间三个属性。`OrderState`是枚举类型，表示订单的5种状态：

* INIT：初始状态
* PAID：已支付
* BREWING：咖啡制作中
* BREWED：咖啡制作完成
* TAKEN：已取餐
* CANCELLED：已取消

# 二. 涵盖的技术点

## 2.1. Spring Data JPA

Hibernate是JPA（Java Persistence API）的标准实现方式，通过学习一个简单的示例来看看Spring Boot如何与Hibernate集成起来。首先，添加Spring Data JPA和h2的maven依赖：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

启动时如果看到下面的信息就说明hibernate配置成功了：

> *HHH000412: Hibernate Core {#Version}*
>
> *HHH000206: hibernate.properties not found*
>
> *HCANN000001: Hibernate Commons Annotations {#Version}*
>
> *HHH000400: Using dialect: org.hibernate.dialect.H2Dialect*

下面开始编写和数据库表对应的实体类，在model包下开发Book实体：

```java
@Entity
public class Book {
 
    @Id
    @GeneratedValue
    private Long id;
    private String name;
 
    // standard constructors
 
    // standard getters and setters
}
```

此时如果重启应用，就会在控制台看见一个对应的Book表被创建了。可以在resources目录下创建sql文件，这样重启Spring Boot应用时就会读取sql进行数据库的初始化。SQL文件可以命名为import.sql（Hibernate）或data.sql（Spring JDBC），或者分开成schema.sql和data.sql，一个负责创建表，一个负责插入数据。

```sql
insert into book values(1, 'The Tartar Steppe');
insert into book values(2, 'Poem Strip');
insert into book values(3, 'Restless Nights: Selected Stories of Dino Buzzati');
```

在JPA中定义DAO很方便，只需要继承Repository或者它的子类，就能让JPA自动生成各种执行CRUD的操作，不需要再编写。新建repository包，编写如下代码：

```java
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
```

创建service包，编写如下代码，形成service-repository-model的结构：

```java
@Service
public class BookService {
 
    @Autowired
    private BookRepository bookRepository;
 
    public List<Book> list() {
        return bookRepository.findAll();
    }
}
```

然后就可以写一个单元测试来验证一下了：

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class BookServiceUnitTest {
 
    @Autowired
    private BookService bookService;
 
    @Test
    public void whenApplicationStarts_thenHibernateCreatesInitialRecords() {
        List<Book> books = bookService.list();
 
        Assert.assertEquals(books.size(), 3);
    }
}
```

默认情况下Hibernate是以小写的形式创建对应表的，但可以通过注解+配置来指定表名：

```java
@Entity(name="BOOK")
public class Book {
    // members, standard getters and setters
}
```

```properties
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
```

###2.1.2 常用配置

spring.jpa.hibernate.ddl-auto用来配置应用启动时是否自动创建相关数据库表，有none、validate、update和create-drop四种值：

* none：启动时不创建表；
* validate：验证数据库表字段和实体类属性是否匹配，不匹配就报错；
* update：服务首次启动时创建表，后续启动服务时如果实体类属性有变化会自动修改表格相关字段；
* create-drop：应用关闭时删掉数据中的相关表

spring.jpa.show-sql设置为true表示执行数据库操作时打印相关SQL语句。spring.jpa.properties.hibernate.format_sql=true设置为true表示对答应的SQL语句进行格式化。

### 2.1.3 常用注解

@Entity用来注解某一个类为实体类。实体类建立和数据库中的表的关联关系。应用首次启动时，默认会在数据中生成一个同实体类相同名字的表，可以通过注解中的name属性来修改表名称：@Entity(name=“xxx”)。@Table 注解也是一个类注解，用来修改表的名字（可以忽略掉不用而使用@Entity 注解的name属性）。

@Id用来注解某个实体类的属性对应的字段是一个主键，该属性不可缺少。@GeneratedValue注解常和@Id注解一起使用，用来定义主键生成策略：

1. @GeneratedValue(strategy = GenerationType.IDENTITY) ：主键为自增ID，oracle不支持；
2. @GeneratedValue(strategy = GenerationType.AUTO)  ：采用默认主键生成策略，oracle默认为序列化，mysql 默认为主键自增；
3. @GeneratedValue(strategy = GenerationType.SEQUENCE)： 根据数据库的序列来生成主键，Mysql不支持；
4. @GeneratedValue(strategy = GenerationType.TABLE) ：使用一个特定的数据库表保存主键，很少使用。

@Column用来注解某个实体类的属性，可以定义一个字段映射到数据库属性的具体特征，比如字段长度，映射到数据库时属性的具体名字等，例如：

```java
@Column(name="DESC", nullable=false, length=512)
    public String getDescription() { return description; }
```

updatable如果设置为false，则该字段无法被更新，可以用在createTime这样的字段上。@Transient注解标注的字段不会被映射到数据库当中。

@MappedSuperclass注解用在父类中，父类不需要定义@Entity注解，子类继承父类后每个子类对应创建一张表，而父类不会建表。每个子类对应的表都有父类中定义的属性。@Enumerated注解表示属性以枚举类型的形式持久化存储。@ManyToMany和@JoinTable注解通常放在一起使用，多用来映射多对多的关联关系，每个多对多关系都有拥有方（owning）和被拥有方（inverse），@JoinTable通常定义在拥有的一方，比如一个订单“拥有”多个咖啡，那么在CoffeeOrder上添加注解。

```java
@JoinTable(
        name="CUST_PHONE",
        joinColumns=
            @JoinColumn(name="CUST_ID", referencedColumnName="ID"),
        inverseJoinColumns=
            @JoinColumn(name="PHONE_ID", referencedColumnName="ID")
)
```

## 2.2 Spring Boot常用注解

**@SpringBootApplication**注解等同于@Configuration + @EnableAutoConfiguration + @ComponentScan。申明让spring boot自动给程序进行必要的配置。**@Configuration**相当于传统的XML配置文件，如果有些第三方库需要用到XML文件，建议仍然通过@Configuration类作为项目的配置主类：可以使用@ImportResource注解加载xml配置文件。**@EnableAutoConfiguration**注解开启Spring Boot自动配置，即尝试根据项目添加的 jar包依赖进行Spring应用的自动配置。例如如果classpath下存在H2DB，并且没有手动配置任何数据库连接beans，那么我们将自动配置一个内存数据库。可以使用@EnableAutoConfiguration注解的排除属性来禁用不想要的特定自动配置类。**@ComponentScan**表示将自动扫描并发现组件。经常使用@ComponentScan注解搜索beans，并结合@Autowired注解导入。可以自动收集所有的 Spring 组件，包括 @Configuration 类。**@Autowired**注解用于自动导入依赖的 bean，**@Service**注解用于修饰 service 层的组件。

## 2.3 Lombok常用注解

@Data注解相当于装配了@Getter、@Setter、@RequiredArgsConstructor、@ToString和@EqualsAndHashCode注解，可用于注解实体类或者Bean类。

```java
@Data
public class UserBean{
  private Integer id;
  private String userName;
}
```

@Builder注解使得一个类支持建造者模式：

- 它创建了一个私有的全参构造器。也就意味着编译器不创建无参构造器；这个类也不可以直接构造对象
- 它为每一个属性创建了一个同名的方法用于赋值，代替了setter，而该方法的返回值为对象本身

```java
// definition
@Builder
public class UserBean {
      private Integer id;
      private String userName;
}

// usage
UserBean u=UserBean.builder()
    .id(1001)
    .userName("polly")
    .build();
System.out.println(u);
```

当一个Java Bean类作为ORM实体类，或者XML/JSON的映射类时，需要这个类有这几个特征：

- 拥有无参构造器
- 拥有setter方法，用以反序列化
- 拥有getter方法，用以序列化

可以同时使用@Data和@Builder注解一个类，让它既支持建造者模式，又定义成一个实体类。但@Builder默认引入了全参构造器，无参构造器就没了，因此需要@NoArgsConstructor注解，但此时全参构造器也没了，因此还要引入@AllArgsConstructor(access = AccessLevel.PRIVATE)注解（注意全参构造器访问级别没有破坏Builder模式的规则）。因此经常看到这4个注解放在一起用：

```java
@Builder
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UserBean {
      private Integer id;
      private String userName;
      private List<String> addresses;
}
```

@Singular注解可以配合@Builder实现对列表属性的操作：

```java
// definition
@Builder
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UserBean {
      private Integer id;
      private String userName;
      @Singular
      private List<String> favorites;
}

// usage
UserBean u = UserBean.builder()
    .id(1001)
    .userName("polly")
    .favorite("music")
    .favorite("movie")
    .build();
```

另外，对于有默认值的字段，只有用@Default注解才能生效：

```java
@Builder
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UserBean {
      private Integer id;
      private String userName;
      @Default
      private String example="123456";
}

UserBean u = UserBean.builder()
    .id(1001)
    .userName("polly")
    .build(); // 没有给example属性赋值
System.out.println(u.toString());
```

其他常用的还有：

* @Slf4j：自动生成log对象
* @Getter(lazy=true)：可以替代经典的Double Check Lock样板代码

## 2.4 集成H2数据库

Spring Boot提供了对H2数据库的无缝支持。首先，添加依赖：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

Spring Boot默认配置应用通过用户名sa和空密码来连接H2提供的内存数据库。但我们可以通过application.properties改变默认行为：

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```

应用一旦重启，内存数据库中的数据就不复存在了。可以通过配置改成基于文件的存储，使用`spring.datasource.url=jdbc:h2:file:/data/demo`即可。可以在resources目录下添加一个data.sql文件。

```sql
DROP TABLE IF EXISTS billionaires;
 
CREATE TABLE billionaires (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  first_name VARCHAR(250) NOT NULL,
  last_name VARCHAR(250) NOT NULL,
  career VARCHAR(250) DEFAULT NULL
);
 
INSERT INTO billionaires (first_name, last_name, career) VALUES
  ('Aliko', 'Dangote', 'Billionaire Industrialist'),
  ('Bill', 'Gates', 'Billionaire Tech Entrepreneur'),
  ('Folrunsho', 'Alakija', 'Billionaire Oil Magnate');
```

Spring Boot会在启动时读取该文件，并将数据插入到H2数据库。可以通过开启`spring.h2.console.enabled=true`使用H2内嵌的控制台来浏览数据库的内容并运行SQL查询。访问`http://localhost:8080/h2-console`输入用户名sa，密码留空，就可以进去了。可以对控制台进行配置：

```properties
spring.h2.console.path=/h2-console
spring.h2.console.settings.trace=false
spring.h2.console.settings.web-allow-others=false
```

上述配置规定改变了控制台的路径，关闭了追踪输出，取消了远程访问。

