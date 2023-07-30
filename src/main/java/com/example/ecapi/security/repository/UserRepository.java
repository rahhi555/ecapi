package com.example.ecapi.security.repository;

import com.example.ecapi.security.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface UserRepository {
    @Select("SELECT * FROM users WHERE email = #{email}")
    Optional<User> findByEmail(String email);

    @Select("SELECT * FROM users WHERE id = #{id}")
    Optional<User> findById(int id);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("""
            INSERT INTO
                users(firstname, lastname, email, password, enabled, role)
            VALUES
                (#{firstname}, #{lastname}, #{email}, #{password}, true, #{role}::role_enum)
            """)
    void save(User user);
}
