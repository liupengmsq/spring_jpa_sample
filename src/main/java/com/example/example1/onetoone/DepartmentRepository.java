package com.example.example1.onetoone;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    //此接口是为了测试级联删除：删除department时，会级联删除关联的employee的记录
    /*
    Hibernate:
    delete
    from
        department_one_to_one
    where
        name=?
     */
    @Modifying
    @Query("delete from Department d where d.name = ?1")
    void deleteByName(String name);
}
