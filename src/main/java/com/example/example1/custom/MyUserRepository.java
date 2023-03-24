package com.example.example1.custom;

import com.example.example1.User;
import org.springframework.data.repository.Repository;

/**
 * 使用的时候直接继承 UserRepositoryCustom接口即可
 */
public interface MyUserRepository extends Repository<User, Long>, UserRepositoryCustom {
}
