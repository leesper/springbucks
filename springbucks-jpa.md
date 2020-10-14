# Springbucks-jpa

## 一. 项目分析

主程序演示了项目的主要需求：

* 从数据库中读出所有存储的咖啡数据并打印到日志中；
* 查找名为“Latte”的（拿铁）咖啡；
* 若找到了，则为名为“Li Lei”（李雷）的顾客创建订单；
* 尝试将订单的状态从`INIT`改为`PAID`，查看日志（成功）；
* 尝试将订单的状态从`PAID`改回`INIT`，查看日志（失败）。

项目是按照`service-repository-model`的结构进行组织的。主程序通过调用相关service来实现上述需求。`CoffeeService`用于操作咖啡数据，`CoffeeOrderService`用于操作订单数据。

`CoffeeService`通过`CoffeeRepository`实现对咖啡数据的操作；`CoffeeOrderService`通过`CoffeeOrderRepository`实现对订单数据的操作。`CoffeeRepository`和`CoffeeOrderRepository`是两个继承了`JpaRepository`泛型接口的接口，其方法由spring-data-jpa自动生成。

`Repository`接口操作一系列的实体类，让实体类和数据库表之间建立对应关系。实体类`Coffee`包含“咖啡名称”和“单价”属性；`CoffeeOrder`包含“咖啡列表”和“订单状态”属性，二者都继承自`BaseEntity`类，后者包含id，创建时间和修改时间三个属性。`OrderState`是枚举类型，表示订单的5种状态：

* INIT：初始状态
* PAID：已支付
* BREWING：咖啡制作中
* BREWED：咖啡制作完成
* TAKEN：已取餐
* CANCELLED：已取消

## 二. 涵盖的技术点

### 1. Spring框架

1. ApplicationRunner
2. spring-data-jpa
3. hibernate
4. ExampleMatcher
5. @Transactional、@Service、@Autowired、@Entity、@Table、@Enumerated、@ManyToMany、@JoinTable注解

### 2. Java语言

1. Optional类的使用
2. 注解
3. 枚举类

### 3. 其他

1. lombok的使用
2. usertype.core库
3. joda-money库
4. h2 database
5. schema.sql

