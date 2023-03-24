package com.example.example1.custom;

import com.example.example1.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

//用@Repository 将此实现交给Spring bean加载
@Repository
//这里模仿SimpleJpaRepository的实现，默认将所有方法都开启一个事务
@Transactional(readOnly = true)
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    //这里使用注解的方式注入容器自己管理的entityManager实例
    @PersistenceContext
    EntityManager entityManager;

    /**
     * 自定义一个查询name的方法
     * @param name
     * @return
     */
    @Override
    public List<User> customerMethodNamesLike(String name) {
        Query query = entityManager.createNativeQuery("SELECT u.* FROM user as u " +
                "WHERE u.name LIKE ?", User.class);
        query.setParameter(1, name + "%");
        return query.getResultList();
    }
}