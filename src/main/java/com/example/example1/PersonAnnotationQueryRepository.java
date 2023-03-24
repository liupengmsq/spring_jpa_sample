package com.example.example1;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// 通过使用注解来写查询, 更改和删除语句, 支持原生SQL语法，和JPQL语法。更推荐使用后者。
// 注意：此类中的接口的方法名字可以是什么的名字，不会影响sql语句，因为我们用的是在注解中写sql的方式。
public interface PersonAnnotationQueryRepository extends JpaRepository<Person, Long> {
    //此方法对应的sql语句如下：
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
        person0_.email=?
     */
    @Query("select p from Person p where p.email = ?1")
    Person findByEmailAddress(String emailAddress);

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
        person0_.firstname like ?
     */
    @Query("select p from Person p where p.firstName like %?1")
    List<Person> findByFirstNameEndWith(String firstName);

    //以下是使用原生的SQL来查询数据库
    /*
    Hibernate:
    SELECT
        *
    FROM
        person
    WHERE
        email = ?
     */
    @Query(value = "SELECT * FROM person WHERE email = ?1", nativeQuery = true)
    Person findByEmailAddressUseNativeSQL(String emailAddress);

    //使用原生SQL，加上排序语句
    /*
    Hibernate:
    SELECT
        *
    FROM
        person
    WHERE
        firstname = ?
    ORDER BY
        ?
     */
    @Query(value = "SELECT * FROM person WHERE firstname = ?1 ORDER BY ?2", nativeQuery = true)
    List<Person> findByFirstNameSortUseNativeSQL(String firstName, String orderBy);

    //使用JPQL实现查询结果的排序
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
        person0_.age asc
     */
    @Query(value = "select p from Person p where p.firstName = ?1")
    List<Person> findByFirstNameSortUseJPQL(String firstName, Sort orderBy);

    /*
    Hibernate:
    select
        person0_.id as col_0_0_,
        length(person0_.firstname) as col_1_0_
    from
        person person0_
    where
        person0_.lastname like ?
    order by
        person0_.age asc
     */
    @Query("select p.id, LENGTH(p.firstName) as fn_len from Person p where p.lastName like ?1%")
    List<Object[]> findByAsArrayAndSort(String lastName, Sort sort);

    //JPQL对分页的支持，JPQL不用有任何改变，只需传入Pageable即可实现分页
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
    @Query("select p from Person  p where p.lastName = ?1")
    Page<Person> findByLastName(String lastName, Pageable pageable);

    //使用@Param注解来映射注解中的参数名和方法中的入参名
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
        or person0_.firstname=?
     */
    @Query("select p from Person p where p.lastName = :last_name or p.firstName = :first_name")
    List<Person> findByLastNameOrFirstName(@Param("last_name") String lastName, @Param("first_name") String firstName);
}
