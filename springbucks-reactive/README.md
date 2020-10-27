# 响应式数据访问

# 1. Reactor基础

相关资料可以参考[Intro To Reactor Core](https://www.baeldung.com/reactor-core)和[官方文档](https://projectreactor.io/docs/core/release/reference/)，要掌握的重要知识点包括Flux和Mono、背压、Publisher-Subscriber、BaseSubscriber回调、算子、合并多个流，以及这些知识点之间的联系。

## 1.1 任务1：理解响应式编程中背压的概念

编写代码，使用Flux产生一个1-9的序列，使用`.log()`打印日志观察代码行为，在subscribe的时候就地创建一个BaseSubscriber，编写hookOnSubscribe()和hookOnNext()回调，实现背压机制：1次只获取3个元素添加到列表elements中。

期望输出：

```
request(3)
onNext(1)
onNext(2)
onNext(3)
request(3)
onNext(4)
onNext(5)
onNext(6)
request(3)
onNext(7)
onNext(8)
onNext(9)
request(3)
onComplete()
Complete
Elements: [1, 2, 3, 4, 5, 6, 7, 8, 9]
```

## 1.2 任务2：理解算子的概念

编写代码，产生一个1-6的序列，然后将每个元素乘以3后添加到列表elements中。期望输出：

```
operatorsDemo: [3, 6, 9, 12, 15, 18]
```

## 1.3 任务3：合并两个流

编写代码，产生一个1-3的序列，然后与包含“Linux Torvalds”, “Turing”和“Bill Gates”的序列zip到一起，在合并函数中将它们拼接成字符串，然后添加到列表elements中，期望输出：

```
combineTwoSteamasDemo: [1 - Linux Torvalds, 2 - Turing, 3 - Bill Gates]
```

## 1.4 任务4：多线程

创建一个1-6的序列，doOnRequest()打印所请求的数目，以elastic方式创建另一个线程运行剩下的任务：

1. 输出onComplete()回调
2. 对每一个数字打印一下日志，格式为“Publish 当前线程名称: 数字”，然后返回数字乘以2
3. 再次输出onComplete()回调
4. 以single方式创建线程
5. 对每一个数字减去1
6. 调用subscribe()添加三个Lambda函数，一个打印每次处理过的值，格式为“Subscribe 当前线程名称: 数字”；一个打印异常值；一个在成功结束时打印“Subscriber COMPLETE”
7. 睡眠2秒等待上述线程处理完毕（关于线程同步，还有更好的方法，见下文）

# 2. 响应式访问Redis

阅读资料[An Introduction to Spring Data Redis Reactive](https://www.baeldung.com/spring-data-redis-reactive)，了解R额activeStringRedisTemplate的基本使用。

## 2.1 任务1：创建咖啡菜单到Redis

1. 创建相关properties文件和sql文件

   * properties文件

     ```properties
     spring.redis.host=localhost
     ```

   * schema.sql

     ```sql
     drop table t_coffee if exists;
     create table t_coffee (
         id bigint auto_increment,
         create_time timestamp,
         update_time timestamp,
         name varchar(255),
         price bigint,
         primary key (id)
     );
     ```

   * data.sql

     ```sql
     insert into t_coffee (name, price, create_time, update_time) values ('espresso', 2000, now(), now());
     insert into t_coffee (name, price, create_time, update_time) values ('latte', 2500, now(), now());
     insert into t_coffee (name, price, create_time, update_time) values ('capuccino', 2500, now(), now());
     insert into t_coffee (name, price, create_time, update_time) values ('mocha', 3000, now(), now());
     insert into t_coffee (name, price, create_time, update_time) values ('macchiato', 3000, now(), now());
     ```

2. 编写业务逻辑

   * 编写方法通过ReactiveRedisConnectionFactory对象创建ReactvieRedisTemplate的bean
   * 通过JdbcTemplate查询数据库中所有数据，然后构造Coffee对象放到列表中
   * 使用列表构造Flux序列，然后放到single线程中执行，使用CountDownLatch建立与主线程同步，完成后打印list ok
   * 通过Hash操作建立咖啡名字和价格的信息，存储到COFFEE_MENU的KEY中，将返回值收集到Flux中，完成后打印set ok
   * 设置KEY过期时间为1分钟，将返回值合并到上面的返回值中，完成后打印expire ok
   * 如果出错，打印异常值，返回单一值false
   * 订阅序列，打印每个返回值，打印异常信息，同步计数减1
   * 打印Waiting，主线程等待

3. 编写Coffee类

   * Coffee类包含ID，名字和价格，分别为Long、String和Long类型
   * 通过lombok注解添加Data+Builder+无参构造+全参构造

# 3. 响应式访问MongoDB







