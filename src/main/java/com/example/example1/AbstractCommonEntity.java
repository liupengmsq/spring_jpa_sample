package com.example.example1;

import javax.persistence.*;

// 公用的entity类型，在这个类中定义每个表相同的列
@MappedSuperclass // <---- 这个注解就是表示这个类不是一个数据库表，而是一个公用的Entity基类
public class AbstractCommonEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Column(name = "age", nullable = false)
    private Integer age;
}
