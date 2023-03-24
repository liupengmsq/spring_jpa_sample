package com.example.example1.onetomany;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

//建表语句：
/*
CREATE TABLE `student_many_to_one` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `age` int NOT NULL,
  `class_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_firstname_last_name` (`first_name`,`last_name`),
  KEY `fk_student_class_idx` (`class_id`),
  CONSTRAINT `fk_student_class` FOREIGN KEY (`class_id`) REFERENCES `class_one_to_many` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

 */
@Entity
@Table(name = "student_many_to_one")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "first_name", nullable = false, length = 45, unique = true)
    private String firstName;

    @Column(name= "last_name", nullable = false, length = 45, unique = true)
    private String lastName;

    @Column(name = "age", nullable = false)
    private Integer age;

    // 此注解是为了解决JSON 序列化的双向关联会死循环的问题。
    // @JsonBackReference 标注的属性在序列化（serialization，即将对象转换为 JSON 数据）时，会被忽略（即结果中的 JSON 数据不包含该属性的内容）。
    // 在序列化时，@JsonBackReference 的作用相当于 @JsonIgnore，此时可以没有 @JsonManagedReference，但在反序列化（deserialization，即 JSON 数据转换为对象）时，
    // 如果没有 @JsonManagedReference，则不会自动注入 @JsonBackReference 标注的属性；
    // 如果有 @JsonManagedReference，则会自动注入 @JsonBackReference 标注的属性。
    // 注意，在反序列化时，@JsonBackReference不能标注在List， Set等集合类型的字段上。只能标注在下面这种引用类型的字段上。
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "class_id", referencedColumnName = "id", nullable = false)
    private ClassOfStudent classOfStudent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public ClassOfStudent getClassOfStudent() {
        return classOfStudent;
    }

    public void setClassOfStudent(ClassOfStudent classOfStudent) {
        this.classOfStudent = classOfStudent;
    }
}
