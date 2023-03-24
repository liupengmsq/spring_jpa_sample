package com.example.example1.custom;

import com.example.example1.User;

import java.util.List;

public interface UserRepositoryCustom {
    /**
     * 自定义一个查询方法，name的like查询，此处仅仅是演示例子，实际中直接用QueryMethod即可
     * @param name
     * @return
     */
    List<User> customerMethodNamesLike(String name);
}
