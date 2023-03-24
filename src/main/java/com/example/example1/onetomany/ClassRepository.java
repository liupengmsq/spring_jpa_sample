package com.example.example1.onetomany;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRepository extends JpaRepository<ClassOfStudent, Long> {
    /*
    Hibernate:
    select
        class0_.id as id1_1_,
        class0_.name as name2_1_,
        class0_.teacher as teacher3_1_
    from
        class_one_to_many class0_
    where
        class0_.name=?
     */
    ClassOfStudent findByName(String name);
}
