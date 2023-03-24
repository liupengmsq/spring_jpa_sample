# 此项目为Spring JPA的学习项目
## 入口点为TestController.java
- 直接使用了spring jpa接口提供的功能
- 通过定义spring识别的方法名，来定制了不同的查询语句
- 关于分页和排序方法的示例
- 使用top，first查询的示例
- 通过使用 Java 8 Stream 作为返回类型来逐步处理查询方法的结果
- 使用注解`@Query`来查询数据库的示例
- 使用CommonRepository定义公用的DB操作
- 使用注解`@Modifying`来修改和删除数据的示例
- 定义实体间的一对一关系，并使用它们
- 定义实体间的一对多关系，并使用它们
- 定义实体间的多对多关系，并使用它们
- 使用QueryByExampleExecutor接口进行基于Example的多种条件组合查询
- 使用QueryByExampleExecutor接口进行基于Example的null值的查询
- 使用JpaSpecificationExecutor接口进行数据库的查询
- 使用自定义的UserRepositoryCustomImpl来实现自定义一个查询User表的方法 (私有的 Repository)
