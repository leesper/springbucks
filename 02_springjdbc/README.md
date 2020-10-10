# JDBC必知必会

# 一. 配置单数据源

项目jdbcdemo演示了如何在SpringBoot中配置单一数据源。该项目在创建时首先需要引入以下五种依赖：

* H2：使用Java编写的关系型数据库，属于内存数据库，项目中用来演示与DB交互
* JDBC：即spring-boot-starter-jdbc，JDBC与SpringBoot的集成
* Lombok：需要在IDEA中下载这个插件
* Web：即SpringMVC
* Actuator：这样可以通过/actuator/beans查看Bean信息，但注意要配置properties文件

可以看到示例代码还是很简单的，之所以这么方便是因为SpringBoot在背后帮我们做了大量的自动配置，比如：

* DataSourceAutoConfiguration：配置DataSource
* DataSourceTransactionManagerAutoConfiguration：配置DataSourceTransactionManager
* JdbcTemplateAutoConfiguration：配置JdbcTemplate

有一些与数据源相关的通用配置可以了解下，比如：

* spring.datasource.url=jdbc:mysql://localhost/test
* spring.datasource.username=dbuser
* spring.datasource.password=dbpass
* spring.datasource.driver-class-name=com.mysql.jdbc.Driver

如果要初始化内嵌数据库，要注意spring.datasource.schema和spring.datasource.data可以确定初始化的SQL文件，在Spring官方文档中有这么一句话：

> Spring Boot can automatically create the schema (DDL scripts) of your DataSource and initialize it (DML scripts). It loads SQL from the standard root classpath locations: schema.sql and data.sql, respectively. In addition, Spring Boot processes the schema-\${platform}.sql and data-${platform}.sql files (if present), where platform is the value of spring.datasource.platform. This allows you to switch to database-specific scripts if necessary. For example, you might choose to set it to the vendor name of the database (hsqldb, h2, oracle, mysql, postgresql, and so on)

在项目的resources目录中如果有schema.sql和data.sql文件，那么SpringBoot在启动时就会对这两个文件进行自动加载，初始化相关表的创建和数据的加载。

# 二. 配置多数据源

可以在项目中配置不同的数据源，实现生产环境和测试环境的切换，或者跨数据源的数据操作。要注意不同数据源的配置要分开。在SpringBoot中配置多数据源有两种方法，一种是手工配置多组DataSource及相关内容（事务，ORM等）；另一种是与SpringBoot协同。协同有两种方式，一种是使用@Primary类型的Bean，一种是主动排除SpringBoot的自动配置：

* DataSourceAutoConfiguration
* DataSourceTransactionManagetAutoConfiguration
* JdbcTemplateAutoConfiguration

```java
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class,
		JdbcTemplateAutoConfiguration.class})
@Slf4j
public class MultijdbcdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultijdbcdemoApplication.class, args);
	}

  // foo数据源相关配置
	@Bean
	@ConfigurationProperties("foo.datasource")
	public DataSourceProperties fooDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean
	public DataSource fooDataSource() {
		DataSourceProperties dataSourceProperties = fooDataSourceProperties();
		log.info("foo datasource: {}", dataSourceProperties.getUrl());
		return dataSourceProperties.initializeDataSourceBuilder().build();
	}

	@Bean
	@Resource
	public PlatformTransactionManager fooTxManager(DataSource fooDataSource) {
		return new DataSourceTransactionManager(fooDataSource);
	}

  // bar数据源相关配置
	@Bean
	@ConfigurationProperties("bar.datasource")
	public DataSourceProperties barDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean
	public DataSource barDataSource() {
		DataSourceProperties dataSourceProperties = barDataSourceProperties();
		log.info("bar datasource: {}", dataSourceProperties.getUrl());
		return dataSourceProperties.initializeDataSourceBuilder().build();
	}

	@Bean
	@Resource
	public PlatformTransactionManager barTxManager(DataSource barDataSource) {
		return new DataSourceTransactionManager(barDataSource);
	}
}

```

# 三. 一些好用的连接池介绍

## 1. HikariCP

高性能JDBC连接池，做了很多底层细节的优化，比如字节码级别优化和各种小的改进。SpringBoot2.x中已经默认使用HikariCP了，可以通过spring.datasource.hikari.*进行配置。常用配置有：

* spring.datasource.hikari.maximumPoolSize=10
* spring.datasource.hikari.minimumIdle=10
* spring.datasource.hikari.idleTimeout=600000
* spring.datasource.hikari.connectionTimeout=30000
* spring.datasource.hikari.maxLifetime=1800000

## 2. Alibaba Druid

Druid是阿里巴巴开源的数据库连接池，为监控而生，内置强大的，不影响性能的监控功能。这套连接池经受住了阿里巴巴各大系统的考验，还是值得信赖的。支持全面详细的监控，提供SQL防注入，还有众多方便定制的扩展点。可以使用spring.datasource.druid.*进行配置，项目示例代码druiddemo中有演示。

可以继承FilterEventAdapter然后对连接池操作的各个环节进行定制，注意，需要修改META-INF/druid-filter.properties增加Filter配置：

```properties
druid.filters.conn=com.springbucks.druiddemo.ConnectionLogFilter
```

ConnectionLogFilter在连接前后都会打印输出相关信息：

```java
@Slf4j
public class ConnectionLogFilter extends FilterEventAdapter {
    @Override
    public void connection_connectBefore(FilterChain chain, Properties info) {
        log.info("BEFORE CONNECTION");
    }

    @Override
    public void connection_connectAfter(ConnectionProxy connection) {
        log.info("AFTER CONNECTION");
    }
}
```

连接池选型时主要考虑以下因素：

* 可靠性
* 性能
* 功能
* 可运维性
* 可扩展性
* 其他……

# 四. 使用SpringJDBC访问数据库

在simplejdbcdemo中简单演示了一下使用spring-jdbc操作数据库的方法。既可以一条一条插入，又可以批量插入。JdbcTemplate是最常用的类，包含如下方法：

* query
* queryForObject
* queryForList
* update
* execute

如果要实现批量处理，比如批量插入操作，可以使用JdbcTemplate的batchUpdate()方法结合BatchPreparedStatementSetter；也可以使用NamedParameterJdbcTemplate的batchUpdate()方法结合SqlParameterSourceUtils.createBatch()实现。具体请参考代码示例。

# 五. 事务抽象

Spring框架提供一致的事务模型，无论你是使用JDBC/Hibernate/MyBatis，还是使用DataSource/JTA。programmatictransdemo演示了如何使用编程式事务，而declarativetransdemo演示了如何使用声明式事务，其中声明式事务是利用AOP创建动态代理对象来实现的。



