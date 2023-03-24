package com.example.example1;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.List;

@NoRepositoryBean // <<-- 这个注解表示spring排除当前接口作为JPA的Repository对象，也就是此类只用来定义公用的访问数据库的方法
public interface CommonRepository<T extends AbstractCommonEntity> extends Repository<T, Long> {
    @Query("select t from #{#entityName} t where t.name = ?1")  //<<-- 这里使用spel表达式来接收变量，这里的变量entityName表示的是实体的名字
    List<T> findAllByName(String name);

    List<Dog> findByAgeGreaterThan(Integer greaterThan);
}
