package com.example.example1;

import javax.persistence.Column;
import javax.persistence.Entity;

//此类继承公用的entity类，继承了公用的id和name列的定义
@Entity
public class Dog extends AbstractCommonEntity {
    @Column(name = "food", nullable = false, length = 45)
    private String food;

    @Column(name = "size", nullable = false)
    private Integer size;

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
