## Spring Data  学习笔记
- 简介
- Spring Data : Spring 的一个子项目。用于简化数据库访问，支持NoSQL 和关系数据存储。其主要目标是使数据库的访问变得方便快捷。
- SpringData项目所支持NoSQL存储：
  - MongoDB （文档数据库）
  - Neo4j（图形数据库）
  - Redis（键/值存储）
  - Hbase（列族数据库）
- SpringData项目所支持的关系数据存储技术：
  - JDBC
  - JPA
  
### JPA 开发步骤
- 使用 Spring Data JPA 进行持久层开发需要的四个步骤：
  - 配置 Spring 整合 JPA。
  - 在 Spring 配置文件中配置 Spring Data，让 Spring 为声明的接口创建代理对象。配置了 ```<jpa:repositories>``` 后，Spring 初始化容器时将会扫描 base-package  指定的包目录及其子目录，为继承 Repository 或其子接口的接口创建代理对象，并将代理对象注册为 Spring Bean，业务层便可以通过 Spring 自动封装的特性来直接使用该对象。
  - 声明持久层的接口，该接口继承  Repository，Repository 是一个标记型接口，它不包含任何方法，如必要，Spring Data 可实现 Repository 其他子接口，其中定义了一些常用的增删改查，以及分页相关的方法。
  - 在接口中声明需要的方法。Spring Data 将根据给定的策略（具体策略稍后讲解）来为其生成实现代码。

#### 搭建环境
- 加入相关maven依赖
```xml
        <!-- https://mvnrepository.com/artifact/org.hibernate/hibernate-entitymanager -->
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-entitymanager</artifactId>
            <version>5.2.13.Final</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/javax.persistence/persistence-api -->
        <dependency>
            <groupId>javax.persistence</groupId>
            <artifactId>persistence-api</artifactId>
            <version>1.0.2</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.springframework.data/spring-data-jpa -->
        <dependency>
            <groupId>org.springframework.data</groupId>
            <artifactId>spring-data-jpa</artifactId>
            <version>2.0.4.RELEASE</version>
        </dependency>
```
- 使用hibernate作为JPA的实现框架，因此需要加入hibernate-entitymanager的依赖，然后在配置文件中指定Vendor。

```xml
<bean id="entityManagerFactory" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        <property name="jpaVendorAdapter">
            <!--指定hibernate为JPA提供框架-->
            <bean class="org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter"/>
        </property>
        <property name="packagesToScan" value="com.javase.spring.data.jpa"/>

        <property name="jpaProperties">
            <props>
                <prop key="hibernate.ejb.naming_strategy">org.hibernate.cfg.ImprovedNamingStrategy</prop>
                <prop key="hibernate.show_sql">true</prop>
                <prop key="hibernate.format_sql">true</prop>
                <prop key="hibernate.hbm2ddl.auto">update</prop>
                <prop key="hibernate.dialect">org.hibernate.dialect.MySQL5Dialect</prop>
            </props>
        </property>
    </bean>
```

- 同样还需配置好DataSource、TransactionManager来保证数据库的连接和事务的管理，最后配置jpa的repository接口需要扫描的包。

```xml
<!--配置jpa的repository接口需要扫描的包-->
<jpa:repositories base-package="com.javase.spring.data.jpa" entity-manager-factory-ref="entityManagerFactory"/>
```

#### 编写实体类和接口

- JPA基本注解：
  - @Entity：标注用于实体类声明语句之前，**指出该Java类为实体类，将映射到指定的数据库表**。如声明一个实体类Customer，它将映射到数据库中的customer表上。
  - @Table：**当实体类与其映射的数据库表名不同名**时需要使用@Table标注说明，该标注与@Entity标注并列使用，置于实体类声明语句之前，可写于单独语句行，也可与声明语句同行。
  - @Id：@Id标注用于声明一个实体类的属性映射为数据库的**主键列**。该属性通常置于属性声明语句之前，可与声明语句同行，也可写在单独行上。
  - @GeneratedValue：**用于标注主键的生成策略，通过 strategy属性指定**。默认情况下，JPA自动选择一个最适合底层数据库的主键生成策略：SQL Server对应IDENTITY，MySQL对应AUTO INCREMENT。
  - @Column：**当实体的属性与其映射的数据库表的列不同名时需要使用@Column标注说明**，该属性通常置于实体的属性声明语句之前，还可与@Id标注一起使用。@Column标注的常用属性是name，用于设置映射数据库表的列名。此外，该标注还包含其它多个属性，如：**unique 、nullable、length** 等。
  - @Transient：表示该属性并非一个到数据库表的字段的映射，**ORM框架将忽略该属性**。如果一个属性并非数据库表的字段映射，就务必将其标示为@Transient。否则，ORM框架默认其注解为@Basic。
  - @Temporal：在核心的 JavaAPI 中并没有定义Date类型的精度(temporal precision)。而在数据库中，表示Date类型的数据有DATE，TIME， 和 TIMESTAMP三种精度(即单纯的日期，时间，或者两者兼备)。**在进行属性映射时可使用@Temporal注解来调整精度。**
- 基本注解示例：

```java
@Table(name = "JPA_PERSONS")
@Entity
public class Person {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "LAST_NAME")
    private String lastName;
    private String email;
    private int age;
    @Temporal(TemporalType.DATE)
    private Date birth;
    //省去Getter和Setter方法。
}
```



- 映射关联关系

  - 关联关系分为单向关联和双向关联。单向关联需要在维护端添加@OneToMany注释或者@ManyToOne注释来表明维护关系。随后通过targetEntity属性指定关联实体类。之后使用@JoinColumn设置外键列名称。
  - 双向关联关系分为一对多、一对一和多对多关系。双向一对多关系中，必须存在一个关系维护端，在JPA规范中，要求 many 的一方作为关系的维护端(ownerside)， one 的一方作为被维护端(inverseside)。建议在one方指定 @OneToMany 注释并设置**mappedBy 属性**，以指定它是这一关联中的被维护端，many 为维护端。
  - 双向一对一及关系：基于外键的 1-1 关联关系：在双向的一对一关联中，需要在关系被维护端(inverseside)中的 @OneToOne注释中指定**mappedBy**，以指定是这一关联中的被维护端。同时需要在关系维护端(ownerside)建立外键列指向关系被维护端的主键列。
  - 双向多对多关系：在双向多对多关系中，我们必须指定一个关系维护端(ownerside),可以通过@ManyToMany注释中指定**mappedBy属性**来标识其为关系被维护端。同时需要在关系维护端加入@JoinTable来指定关联表。

  ```java
  @JoinTable(name="中间表名称",
  joinColumns=@joinColumn(name="本类的外键",
  referencedColumnName="本类与外键对应的主键"),
  inversejoinColumns=@JoinColumn(name="对方类的外键",
  referencedColunName="对方类与外键对应的主键")
  )
  ```

- 在上述示例中，为每个Person加一个商品类来加以对应，示例代码如下：

```java
@Table(name = "JPA_INDENTS")
@Entity
public class Indent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "INDENT_NAME")
    private String indentName;

    @JoinColumn(name = "PERSON_ID")
    //@ManyToOne 表示多个商品对应一个人
    @ManyToOne(targetEntity = Person.class)
    private Person person;
    //省去Getter和Setter方法
}
```



