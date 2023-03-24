package com.example.example1.onetomany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Set;

//建表语句：
/*
CREATE TABLE `class_one_to_many` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `teacher` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
 */
@Entity
@Table(name = "class_one_to_many")
public class ClassOfStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "name", nullable = false, length = 45, unique = true)
    private String name;

    @Column(name= "teacher", nullable = false, length = 45, unique = true)
    private String teacher;

    // 此注解是为了解决JSON序列化的双向关联会死循环的问题。
    // @JsonManagedReference 标注的属性则会被序列化。
    // 在反序列化（deserialization，即 JSON 数据转换为对象）时，如果没有 @JsonManagedReference，则不会自动注入@JsonBackReference 标注的属性；
    // 如果有 @JsonManagedReference，则会自动注入@JsonBackReference 标注的属性。
    @JsonManagedReference
    // 注意：这里的mappedBy指定的就是关联关系的拥有一方。这里就是指定的是Student表这方是关联关系的owner。
    @OneToMany(mappedBy = "classOfStudent", fetch = FetchType.LAZY, cascade = CascadeType.ALL )
    //这个orderby是指定关联的students按照那个列排序
    @OrderBy("firstName")
    private Set<Student> students;

    // 由于使用了延时加载，那么调用下面方法并当获取Set中的student的时候，hibernate才会执行如下语句：
    /*
    Hibernate:
        select
            students0_.class_id as class_id5_6_0_,
            students0_.id as id1_6_0_,
            students0_.id as id1_6_1_,
            students0_.age as age2_6_1_,
            students0_.class_id as class_id5_6_1_,
            students0_.first_name as first_na3_6_1_,
            students0_.last_name as last_nam4_6_1_
        from
            student_many_to_one students0_
        where
            students0_.class_id=?
     */
    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
