package com.example.example1;

import javax.persistence.*;

/* 以下是此实体类对应的数据库表的创建sql：
CREATE TABLE `person` (
  `id` int NOT NULL AUTO_INCREMENT,
  `firstname` varchar(50) DEFAULT NULL,
  `lastname` varchar(50) DEFAULT NULL,
  `age` int NOT NULL DEFAULT '0',
  `email` varchar(200) DEFAULT NULL,
  `active` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
 */
@Entity
public class Person {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id; //注：ORM类中的字段的类型都必须是包装类，不能是原始的long，或者int。

    @Column(name = "firstname", nullable = true, length = 50)  // 注：这些注解要么都统一标记在field上，要么就统一标记在get方法上。
    private String firstName;

    @Column(name = "lastname", nullable = true, length = 50)
    private String lastName;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "email", nullable = true, length = 200)
    private String email;

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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
