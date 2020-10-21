# NoSQL 实践

本目录中的两个项目演示如何使用Spring访问NoSQL类型的数据库。以文档型数据库MongoDB和KV类型数据库Redis作为例子。分别演示如何使用Template或Repository方式访问数据库。

# 1. Spring访问MongoDB

## 1.1 通过Docker启动MongoDB

步骤：

1. 安装docker desktop

2. 拉取MongoDB官方镜像

   `docker pull mongo`

3. 运行MongoDB镜像

   `docker run --name mongo -p 27017:27017 -v ~/docker-data/mongo:/data/db -e MONGO_INITDB_ROOT_USERNAME=admin -e MONGO_INITDB_ROOT_PASSWORD=admin -d mongo`

4. 登录到MongoDB容器中

   `docker exec -it mongo bash`

5. 进入MongoDB命令行（使用的用户名和密码是上面环境变量指定好了的）

   `mongo -u admin -p amdin`
   
6. 创建库

   `use springbucks;`

7. 创建用户，添加读写权限

   ```
   db.createUser(
     {
   		user: "springbucks",
   		pwd: "springbucks",
   		roles: [
   			{ role: "readWrite", db: "springbucks" }
   		]
   	}
   )
   ```

## 1.2 实践

application.properties中配置MongoDB数据库连接：

```properties
spring.data.mongodb.uri=mongodb://springbucks:springbucks@localhost:27017/springbucks
```

对应用进行分层设计，分为model层和repository层。repository层包含CoffeeRepository接口，声明接口方法`List<Coffee> findByName(String)`，继承MongoRepository，model层包含Coffee业务模型，具有以下字段：

```java
public class Coffee {
  private String id;
  private String name;
  private Long price;
  private Date createTime;
  private Date updateTime;
}
```

### 1.2.1 任务1：使用MongoTemplate

1. 创建espresso，初始化咖啡名称（espresso）、价格（20元）、创建时间和修改时间，保存到MongoDB数据库，然后打印保存后的数据

   期望输出：

   `Coffee Coffee(id=X, name=espresso, price=CNY 2000, createTime=XXX, updateTime=XXX)`

2. 使用Query+Criteria查找名为espresso的咖啡列表，打印找到的条数，和所有的数据

   期望输出：

   `Find 1 Coffee`

   `Coffee Coffee(id=X, name=espresso, price=CNY 2000, createTime=XXX, updateTime=XXX)`

3. 睡眠1秒，然后将名称为espresso的咖啡数据更新为30元（注意同步更新updateTime），打印更新了多少条数据

   期望输出：`Update Result: 1`

4. 通过ID查找刚才被更新的咖啡数据，打印结果

   期望输出：

   `Update Result: Coffee(id=X, name=espresso, price=CNY 3000, createTime=XXX, updateTime=XXX)`

5. 删除该数据

### 1.2.2 任务2：使用Repository

1. 创建espresso和latte，初始化咖啡名称（espresso、latte）、价格（20元、30元）、创建时间和修改时间，批量插入，然后查找全部数据并按name排序，将咖啡数据打印到日志；

   期望输出：

   `Saved Coffee Coffee(id=X, name=espresso, price=CNY 2000, createTime=XXX, updateTime=XXX)`

   `Saved Coffee Coffee(id=Y, name=latte, price=CNY 3000, createTime=XXX, updateTime=XXX)`

2. 睡眠1秒，将latte价格更新到35元（注意同步更新updateTime），保存到数据库，然后通过name查找数据并打印到日志；

   期望输出：

   `Coffee Coffee(id=Y, name=latte, price=CNY 3500, createTime=XXX, updateTime=XXX)`

3. 删除全部数据

# 2. Spring访问Redis

Redis是开源的高性能KV存储，支持各种数据结构。Spring对Redis的支持可以通过Jedis/Lettuce、RedisTemplate和Repository等方式实现。

## 2.1 通过Docker启动Redis

步骤：

1. 拉取官方镜像

   `docker pull redis`

2. 启动Redis

   `docker run --name redis -d -p 6379:6379 redis`

## 2.2 实践

在springbucks-data/springbucks-jpa的基础上对项目进行改造，引入Redis，目标是学习Redis的各种使用场景，特别是理解缓存抽象的概念。在这个过程中相应地学习Spring的各种知识。

### 2.2.1 任务1：学会使用Jedis

需求：

1. 日志打印JedisPoolConfig的配置信息

   期望输出：

   ```
   JedisPoolConfig [lifo=true, fairness=false, maxWaitMillis=-1, minEvictableIdleTimeMillis=60000, softMinEvictableIdleTimeMillis=-1, numTestsPerEvictionRun=-1, evictionPolicyClassName=org.apache.commons.pool2.impl.DefaultEvictionPolicy, testOnCreate=false, testOnBorrow=true, testOnReturn=false, testWhileIdle=true, timeBetweenEvictionRunsMillis=30000, blockWhenExhausted=true, jmxEnabled=true, jmxNamePrefix=pool, jmxNameBase=null, maxTotal=5, maxIdle=5, minIdle=0]
   ```

2. 从JedisPool获取Jedis实例，调用coffeeService从数据库查找所有咖啡数据，遍历每个找到的对象，创建springbucks-menu哈希表，其中key为咖啡名字，value为价格

3. 再从springbucks-menu中读出所有的内容，然后打印到日志

   期望输出：`Menu: {mocha=3000, espresso=2000, capuccino=2500, latte=2500, macchiato=3000}`

4. 获取springbucks-menu中espresso的价格，打印到日志

   期望输出：`espresso - 2000`

实现：

1. 上述需求在jedisDemo()函数中实现
2. CoffeeService包含两个方法：findAllCoffee()和findOneCoffee()。findAllCoffee()调用CoffeeRepository完成
3. findOneCoffee()通过Find By Example实现。对name进行忽略大小写的精确（exact）查找

涉及到的知识点：

1. redis相关配置及其含义	

   ```properties
   redis.host=localhost
   redis.maxTotal=5
   redis.maxIdle=5
   redis.testOnBorrow=true
   ```

2. Jedis的使用，如何通过JedisPoolConfig和JedisPool初始化Jedis实例

3. 如何使用[@Bean](https://www.baeldung.com/spring-bean)、[@Value](https://www.baeldung.com/spring-value-annotation)和[@ConfigurationProperties](https://www.baeldung.com/configuration-properties-in-spring-boot)注解

4. Repository中使用"Find By Example"进行[查找](https://www.baeldung.com/spring-data-query-by-example)（Example和ExampleMatcher）

5. Java的try块实现资源自动释放

6. Redis哈希表相关[操作](https://redis.io/commands/hset)

### 2.2.2 任务2：Spring缓存抽象

需求：

1. 查找所有的咖啡，打印得到的结果数目，此时获取的结果是从数据库中取到的，并自动缓存一份

   期望输出：`Count: 5`

2. 重复运行10次，此时获取的结果是直接从缓存中读取的

   期望输出：

   ```
   Reading from cache.
   Reading from cache.
   Reading from cache.
   Reading from cache.
   Reading from cache.
   Reading from cache.
   Reading from cache.
   Reading from cache.
   Reading from cache.
   Reading from cache.
   ```

3. 重载咖啡数据，此时缓存清空

4. 再次查找所有咖啡，并遍历打印咖啡名字，此时获取的结果是从数据库中取到的，并自动缓存一份

   期望输出：

   ```
   Reading after refresh.
   Coffee espresso
   Coffee latte
   Coffee capuccino
   Coffee mocha
   Coffee macchiato
   ```

实现：

1. 上述需求在cacheDemo()中实现
2. CoffeeService编写两个方法：findAllCoffeeCacheable()和reloadCoffee()。前者实现通过注解缓存查到的数据，后者清空缓存

涉及到的知识点：

1. Spring的[缓存抽象](https://www.baeldung.com/spring-cache-tutorial)机制
2. 基于注解的缓存
   * @EnableCaching(proxyTargetClass=true)
   * @Cacheable
   * @CacheEvict
   * @CachePut
   * @Caching
   * @CacheConfig(cacheNames="")

### 2.2.3 任务3：通过Spring Boot配置Redis缓存

1. 保持上述代码不变，添加如下依赖和配置：

   ```xml
   <dependency>
   			<groupId>org.springframework.boot</groupId>
   			<artifactId>spring-boot-starter-data-redis</artifactId>
   </dependency>
   ```

   ```properties
   spring.cache.type=redis
   spring.cache.cache-names=coffee
   spring.cache.redis.time-to-live=5000
   spring.cache.redis.cache-null-values=false
   
   spring.redis.host=localhost
   ```

观察日志输出，应该和任务2.2.2一样，只是这次缓存由Redis托管。

### 2.2.4 任务4：使用RedisTemplate

需求：

1. 查找名为mocha的咖啡，并打印到日志

   期望输出：

   `Coffee Optional[Coffee(super=BaseEntity(id=4, createTime=XXX, updateTime=XXX), name=mocha, price=3000)]`

2. 循环5次运行上述查找

   期望输出：

   ```
   Get coffee mocha from Redis.
   Get coffee mocha from Redis.
   Get coffee mocha from Redis.
   Get coffee mocha from Redis.
   Get coffee mocha from Redis.
   ```

3. 输出从Redis获取的咖啡数据

   期望输出：

   `Value from Redis: Optional[Coffee(super=BaseEntity(id=4, createTime=XXX, updateTime=XXX), name=mocha, price=3000)]`

实现：

1. 上述需求在redisTemplateDemo()中实现

2. CoffeeService编写findOneCoffeeWithRedisTemplate()方法，先通过RedisTemplate从名为springbucks-coffee的缓存中获取咖啡数据，若找到了则直接返回Optional的数据，若找不到则从数据库中查找，然后先写入到缓存并设置1分钟过期，然后再返回结果

3. 添加相关配置

   ```properties
   spring.redis.lettuce.pool.maxActive=5
   spring.redis.lettuce.pool.maxIdle=5
   ```

涉及到的知识点：

1. RedisTemplate的使用，如何通过@Bean初始化RedisTemplate

### 2.2.5 任务5：使用RedisRepository

需求：

1. 查找名为mocha的咖啡，并打印到日志

   期望输出：

   `Coffee Optional[Coffee(super=BaseEntity(id=4, createTime=XXX, updateTime=XXX), name=mocha, price=3000)]`

2. 循环5次运行上述查找

   期望输出：

   ```
   Coffee CoffeeCache(id=4, name=mocha, price=CNY 30.00) found in cache.
   Coffee CoffeeCache(id=4, name=mocha, price=CNY 30.00) found in cache.
   Coffee CoffeeCache(id=4, name=mocha, price=CNY 30.00) found in cache.
   Coffee CoffeeCache(id=4, name=mocha, price=CNY 30.00) found in cache.
   Coffee CoffeeCache(id=4, name=mocha, price=CNY 30.00) found in cache.
   ```

3. 输出从Redis获取的咖啡数据

   期望输出：

   `Value from Redis: Optional[Coffee(super=BaseEntity(id=null, createTime=null, updateTime=null), name=mocha, price=3000)]`

实现：

1. 上述需求在redisRepositoryDemo()中实现
2. CoffeeService中增加CoffeeCacheRepository类型成员变量，编写findSimpleCoffeeFromCache()方法
3. findSimpleCoffeeFromCache()方法先从cacheRepository中尝试获取CoffeeCache对象，如果存在则从CoffeeCache构造Coffee数据并返回；否则就从数据库中查找咖啡数据，若找到，则构造CoffeeCache对象并保存到cacheRepository，然后返回找到的对象
4. CoffeeCacheRepository接口包含findOneByName()方法；CoffeeCache类缓存咖啡的名称和价格信息

