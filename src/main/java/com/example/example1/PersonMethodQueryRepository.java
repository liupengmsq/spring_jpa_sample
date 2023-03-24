package com.example.example1;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.stream.Stream;

// 通过定义方法名，jpa会自动通过动态代理生成对应的sql语句
public interface PersonMethodQueryRepository extends Repository<Person, Long> /* 注，这里的第二个类型参数Long，就是Person这个类在数据表中主键的类型 */ {
    // and的查询关系
    /* 此方法对应的sql语句如下：
    Hibernate:
    select
        person0_.id as id1_0_,
        person0_.active as active2_0_,
        person0_.age as age3_0_,
        person0_.email as email4_0_,
        person0_.firstname as firstnam5_0_,
        person0_.lastname as lastname6_0_
    from
        person person0_
    where
        person0_.email=?
        and person0_.lastname=?
     */
    List<Person> findByEmailAndLastName(String email, String lastName);

    // 包含distinct去重，or的sql语法
    /*
    Hibernate:
    select
        distinct person0_.id as id1_0_,
        person0_.active as active2_0_,
        person0_.age as age3_0_,
        person0_.email as email4_0_,
        person0_.firstname as firstnam5_0_,
        person0_.lastname as lastname6_0_
    from
        person person0_
    where
        person0_.lastname=?
        or person0_.firstname=?
     */
    List<Person> findDistinctPersonByLastNameOrFirstName(String lastName, String firstName);
    //下面的这种和上面的方法效果相同
    List<Person> findPersonDistinctByLastNameOrFirstName(String lastName, String firstName);

    // 根据lastname字段查询忽略大小写
    /* 此方法对应的sql语句如下：
    Hibernate:
    select
        person0_.id as id1_0_,
        person0_.active as active2_0_,
        person0_.age as age3_0_,
        person0_.email as email4_0_,
        person0_.firstname as firstnam5_0_,
        person0_.lastname as lastname6_0_
    from
        person person0_
    where
        upper(person0_.lastname)=upper(?)
     */
    List<Person> findByLastNameIgnoreCase(String lastName);

    // 根据lastname和firstname查询equal并且忽略大小写
    /*
    Hibernate:
    select
        person0_.id as id1_0_,
        person0_.active as active2_0_,
        person0_.age as age3_0_,
        person0_.email as email4_0_,
        person0_.firstname as firstnam5_0_,
        person0_.lastname as lastname6_0_
    from
        person person0_
    where
        upper(person0_.lastname)=upper(?)
        and upper(person0_.firstname)=upper(?)
     */
    List<Person> findByLastNameAndFirstNameAllIgnoreCase(String lastName, String firstName);

    // 对查询结果根据lastname排序
    /*
    Hibernate:
    select
        person0_.id as id1_0_,
        person0_.active as active2_0_,
        person0_.age as age3_0_,
        person0_.email as email4_0_,
        person0_.firstname as firstnam5_0_,
        person0_.lastname as lastname6_0_
    from
        person person0_
    where
        person0_.lastname=?
    order by
        person0_.firstname asc
     */
    List<Person> findByLastNameOrderByFirstNameAsc(String lastName);
    List<Person> findByLastNameOrderByFirstNameDesc(String lastName);

    // 传入的Pageable对象的page和size会对应sql的limit， 其中的sort对应的是order by， 和asc
    /*
    Hibernate:
    select
        person0_.id as id1_0_,
        person0_.active as active2_0_,
        person0_.age as age3_0_,
        person0_.email as email4_0_,
        person0_.firstname as firstnam5_0_,
        person0_.lastname as lastname6_0_
    from
        person person0_
    where
        person0_.lastname=?
    order by
        person0_.age asc limit ?, ?
     */
    Page<Person> findByLastName(String lastName, Pageable pageable);


    // 与上面不同的是下面的方法返回的是Slice
    /*
    Hibernate:
    select
        person0_.id as id1_0_,
        person0_.active as active2_0_,
        person0_.age as age3_0_,
        person0_.email as email4_0_,
        person0_.firstname as firstnam5_0_,
        person0_.lastname as lastname6_0_
    from
        person person0_
    where
        person0_.firstname=?
    order by
        person0_.age asc limit ?, ?
     */
    Slice<Person> findByFirstName(String firstName, Pageable pageable);

    //
    /*
    Hibernate:
    select
        person0_.id as id1_0_,
        person0_.active as active2_0_,
        person0_.age as age3_0_,
        person0_.email as email4_0_,
        person0_.firstname as firstnam5_0_,
        person0_.lastname as lastname6_0_
    from
        person person0_
    where
        person0_.age=?
    order by
        person0_.lastname asc
     */
    List<Person> findByAge(Integer age, Sort sort);

    /*
    Hibernate:
    select
        person0_.id as id1_0_,
        person0_.active as active2_0_,
        person0_.age as age3_0_,
        person0_.email as email4_0_,
        person0_.firstname as firstnam5_0_,
        person0_.lastname as lastname6_0_
    from
        person person0_
    where
        person0_.age=?
    order by
        person0_.firstname asc limit ?, ?
     */
    List<Person> findByAgeOrderByFirstName(Integer age, Pageable pageable);

    /*
    Hibernate:
    select
        person0_.id as id1_0_,
        person0_.active as active2_0_,
        person0_.age as age3_0_,
        person0_.email as email4_0_,
        person0_.firstname as firstnam5_0_,
        person0_.lastname as lastname6_0_
    from
        person person0_
    where
        person0_.lastname=? limit ?
     */
    Page<Person> queryFirst5ByLastName(String lastName, Pageable pageable);

    /*
    Hibernate:
    select
        person0_.id as id1_0_,
        person0_.active as active2_0_,
        person0_.age as age3_0_,
        person0_.email as email4_0_,
        person0_.firstname as firstnam5_0_,
        person0_.lastname as lastname6_0_
    from
        person person0_
    order by
        person0_.lastname asc limit ?
     */
    Person findFirstByOrderByLastNameAsc();

    /*
    Hibernate:
    select
        person0_.id as id1_0_,
        person0_.active as active2_0_,
        person0_.age as age3_0_,
        person0_.email as email4_0_,
        person0_.firstname as firstnam5_0_,
        person0_.lastname as lastname6_0_
    from
        person person0_
     */
    @Query("select p from Person p")
    Stream<Person> findAllByCustomQueryAndStream();
}
