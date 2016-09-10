package com.example.thymeleaf.repository;

import com.example.thymeleaf.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author hxz
 * @since 2016/08/30 10:10
 */
//@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByNameAndPassword(String name, String password);
}
