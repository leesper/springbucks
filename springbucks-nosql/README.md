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

# Spring访问Redis

