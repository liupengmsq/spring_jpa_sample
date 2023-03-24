package com.example.example1;

import javax.persistence.Column;
import javax.persistence.Entity;

//此类继承公用的entity类，继承了公用的id和name列的定义
@Entity
public class Cat extends AbstractCommonEntity {
    @Column(name = "color", nullable = false, length = 45)
    private String color;

    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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
}
