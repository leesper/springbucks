# 1. 问题界定

## 1.1 专业术语

经过多年的发展，Spring框架已经成为世界上最流行的Java后端开发框架，并衍生出了一整套家族体系。无论是车辆网、在线电商还是在线视频，到处都能看到Spring家族的身影。Spring框架非常灵活，支持各种扩展和第三方库。框架之所以灵活，是因为采用了被称为**控制反转（Inversion of Control）**和**依赖注入（Dependency Injection）**的技术。

根据维基百科的解释，“控制反转”字面上的意思是“对控制流的反转”，实际含义是指框架的使用者自己编写的那部分代码由框架提供的控制流进行控制。比如我们使用Python写一个小工具，就需要调用各种第三方库来完成想要实现的功能，就是由开发人员定义控制流对代码进行控制。使用框架进行开发时，开发人员只需要写与业务强相关的代码即可，然后通过某种方式“注册”到框架中让框架感知到，最后由框架去调用开发人员写的这部分代码。

“依赖注入”是“控制反转”的一种特例。一个对象需要调用到的其他对象称为这个对象的“依赖”。前者称为client，后者称为service。调用service的client不需要知道service是怎么被构建出来的，框架负责创建这样的依赖（注入）。

Spring全家桶相关名词解释：

* Spring：提供最核心的支持，包括依赖注入、会话管理、web、数据访问和消息通信；
* Spring Boot：在Spring基础上进行默认配置，做到既可以开箱即用，又可以定制开发；
* Spring Data：提供一致的数据访问范式，可以访问各种关系型和非关系型DB；
* Spring Cloud：提供一套方便开发分布式系统的工具，主要用来开发微服务；
* Project Reactor：第四代响应式开发库，支持完全非阻塞式背压（背压是指消费者在消费生产者产生的数据时，能够向上游反馈流量需求的一种机制，非阻塞式背压由消费者规定生产者生产多少消息量，并最多只能发送这些量）；
* Spring MVC：基于Servlet API构建Web应用；
* Spring WebFlux：基于响应式技术栈构建Web应用；
* Spring Cloud Stream：轻量级事件驱动微服务框架。

Spring和Spring MVC是什么关系？用Spring框架进行开发解决的核心问题，是开发人员自己不需要创建和维护具有复杂依赖关系的对象集，而由框架来负责的问题。所以Spring MVC可以在Spring框架的基础上，提供基于Servlet API开发Web服务的框架。Spring Boot诞生后又进一步减少了很多配置，加快了Web开发的速度。

## 1.2 问题分析

下面针对如何掌握Spring全家桶进行“3W-2S-1R”问题分析。

掌握Spring全家桶技术栈是Java后端开发工程师必备的技能，也是迈向JavaEE企业级架构师的第一步。要想成为JavaEE企业级工程师或架构师，Spring全家桶是必须掌握的，要能熟练应用这套技术栈来开发各种前后端分离的企业级应用。

几乎所有的Java工程师在能力上的要求，都是要精通SSM框架，而Spring全家桶就占了两个“S”，岗位的需求量很大，且一门技术学精了以后完全可以将知识迁移到其他领域进行应用。通过一个springbucks的教学案例学习完《玩转Spring全家桶》，就能熟悉并上手上述技术栈，为成为JavaEE架构师奠定基础。

从整个大局上去分析，要在8个月的时间内点亮“JavaEE企业级工程师/架构师”和“大数据开发工程师”两棵技能树，那么每棵技能树平均需要4个月的时间完成。而Spring全家桶又是前者的核心内容，掌握了它其他知识就迎刃而解了。因此需要在3-4个月的时间内完成所有教学视频的观看和所有内容的学习，然后输出必要的学习笔记，并跟着课程一步一步完成springbucks项目。

要解决的问题包括如下范围：

1. Spring家族主要成员。学习JDBC相关知识，包括数据源、连接池、Spring JDBC、事务以及异常抽象、Spring Data JPA和MyBatis。完成springbucks第1个版本，包括了解需求、通过JPA定义实体和通过Spring Data JPA定义简单的Repository；
2. NoSQL实践。学习使用Docker辅助开发、在Spring中访问MongoDB和Redis、缓存抽象。完成springbucks的第2个版本，包括使用不同类型DB存储咖啡信息和如何结合JPA与Redis来优化咖啡信息的存储；
3. 数据访问进阶。学习Project Reactor、通过Reactive方式访问Redis/MongoDB/RDBMS、使用AOP打印数据访问层摘要。完成springbucks第3个版本，通过Reactive方式保存数据和操作缓存；
4. Spring MVC实践。学习Spring应用上下文、请求的处理机制、视图、静态资源和缓存、MVC异常处理、切入点。完成springbucks第4个版本，包括拆分waiter-service，增加更多REST方法和增加缓存、性能日志与异常处理；
5. 访问web资源。学习使用RestTemplate和WebClient访问资源。完成springbucks第5个版本，增加customer-service，包括通过编码方式查询咖啡和创建订单；
6. web开发进阶。学习设计RESTful Web Service、HATEOAS、Spring Data REST实现超媒体服务、分布式session和WebFlux。完成springbucks第6个版本，包括waiter-service的改进（更改为HATEOAS风格服务和使用WebFlux）和customer-service的改进（更换为HATEOAS风格服务调用方式）；
7. 重新认识Spring Boot。学习如何实现自动配置、起步依赖、配置加载、Actuator、容器配置和Docker镜像打包。完成springbucks第7个版本，包括对waiter-service的改进（增加咖啡数量健康检查、订单数量监控和HTTPS/HTTP2支持）和对customer-service的改进（增加HTTPS/HTTP2支持）；
8. Spring Cloud云原生。学习云原生、微服务、Spring Cloud的各组成部分、各种服务注册和发现的组件。完成springbucks第8个版本，包括使用多种服务注册中心注册waiter-service服务和customer-service通过多种服务注册中心发现waiter-service；
9. 服务熔断。学习使用Hystrix实现服务熔断和Resilience4j实现服务限流。完成springbucks第9个版本，包括使用Resilience4j的RateLimiter防护waiter-service，以及使用Hystrix或Resilience4j熔断和并发控制customer-service；
10. 服务配置。学习基于Git的配置中心、基于ZK的配置中心、Spring Cloud的配置抽象、基于Consul的配置中心和基于Nacos的配置中心。完成springbucks第10个版本，包括增加订单金额与折扣、增加Waiter名称和使用不同配置中心；
11. Spring Cloud Stream。学习如何使用Spring Cloud Stream访问RabbitMQ和Kafka。完成springbucks第11个版本，包括waiter-service增加支付功能、制作订单消息通知、接收订单完成通知，customer-service增加支付功能、查询订单状态并取咖啡和barista-service等待通知并制作订单；
12. 服务链路追踪。学习如何追踪消息链路、使用Dapper进行链路治理和使用Spring Cloud Sleuth实现链路追踪。完成springbucks第12个版本，包括waiter-service/customer-service增加基于Web向Zipkin埋点、barista-service增加基于MQ向Zipkin埋点和最终通过Docker运行整个springbucks应用。

学习整个课程的过程中面临的第一个挑战是对Spring全家桶的技术栈熟悉程度不够，既缺乏全面的理论知识储备，又缺乏必要的实战经验。光是靠课程的内容可能是不行的，还需要下来多阅读各种课外资料。整个学习的过程就像在拼一副拼图，其中有很多残缺的部分，需要找其他资料来补充，进行“交叉验证”。第二个挑战是学习内容难度比较大，如果不采取正确的学习方法，容易半途而废，坚持不下去。第三个挑战是学习的内容比较庞杂，且实践性较强，需要花大量的时间实践相关内容，会用了才是真正掌握了。

要克服这些挑战，关键是要采用科学的学习方法来应对。学习的本质是一个认识不断深入的过程，实践是第一位的。要先通过实践积累感性认识，然后把感性认识提炼总结形成理性认识，最后用理性认识反过来指导实践。因此学习《玩转Spring全家桶》可以分为两个大的阶段来进行。第一个阶段是对springbucks线上咖啡馆实战项目尝试进行需求建模、分析建模和设计建模，实践一下COMET软件工程方法；第二个阶段是通过观看课程视频、阅读课程教案、运行示例代码来一步一步学习相关知识，并仔细分析每个版本的springbucks示例代码，从中提取出功能点，然后自己尝试实现一遍，并与示例代码进行对比研究，总结开发经验。

## 1.3 问题陈述

综合上面所有的分析可以得出如下问题陈述：在4个月的时间内，通过观看课程视频、阅读课程教案、运行示例代码和仿写springbucks在线咖啡馆示例代码掌握和学习Spring全家桶，目标是使用Spring技术栈开发springbucks在线咖啡馆实战项目。

# 2. 拆解问题

使用议题树对要学习的内容进行拆解，如下所示：

![](/Users/likejun/springbucks/springbucks.png)

# 3. 执行解决

将议题树转化为开发计划，如下所示（详细计划请参考schedule.xlsx）：

![](/Users/likejun/springbucks/schedule.png)

