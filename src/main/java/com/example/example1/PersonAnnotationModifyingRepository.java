package com.example.example1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

// 此类中的方法使用了@Modifying注解实现更改或删除数据的功能
@Transactional  // << 注意，使用update和delete的时候，必须要加transactional的事务支持
public interface PersonAnnotationModifyingRepository extends JpaRepository<Person, Long> {
    /*
    Hibernate:
    update
        person
    set
        firstname=?
    where
        lastname=?
     */
    @Modifying
    @Query("update Person p set p.firstName = ?1 where p.lastName = ?2")
    int setFirstNameByLastName(String firstName, String lastName);

    /*
    Hibernate:
    delete
    from
        person
    where
        lastname=?

     */
    @Modifying
    @Query("delete from Person p where p.lastName = ?1")
    void deleteByLastName(String lastName);
}
