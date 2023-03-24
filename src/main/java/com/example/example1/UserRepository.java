package com.example.example1;

import org.springframework.data.repository.PagingAndSortingRepository;

//此接口继承自pagingandsorting，就是支持了分页和排序的功能，spring会自动实现接口方法，来支持User实体类的数据库功能
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
}