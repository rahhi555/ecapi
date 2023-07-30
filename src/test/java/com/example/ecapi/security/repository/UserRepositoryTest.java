package com.example.ecapi.security.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.ecapi.model.EnumRole;
import com.example.ecapi.security.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@Sql("UserRepositoryTest.sql")
@Transactional
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void test_findByEmail() {
        Optional<User> user = userRepository.findByEmail("test@example.com");
        assertThat(user.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    public void test_findById() {
        int id = userRepository.findByEmail("test@example.com").get().getId();
        Optional<User> user = userRepository.findById(id);
        assertThat(user.get().getId()).isEqualTo(id);
    }

    @Test
    public void test_save() {
        User newUser = User.builder()
                        .email("new-user@example.com")
                        .password("password")
                        .firstname("newFirstName")
                        .lastname("newLastName")
                        .role(EnumRole.VENDOR)
                        .build();
        userRepository.save(newUser);
        Optional<User> user = userRepository.findById(newUser.getId());
        assertThat(user.get().getId()).isEqualTo(newUser.getId());
        assertThat(user.get().getEmail()).isEqualTo(newUser.getEmail());
        assertThat(user.get().getLastname()).isEqualTo(newUser.getLastname());
        assertThat(user.get().getFirstname()).isEqualTo(newUser.getFirstname());
        assertThat(user.get().getRole()).isEqualTo(newUser.getRole());
    }

    @Test
    public void test_save_roleが空白の場合はデフォルトでCUSTOMERになること() {
        User newUser = User.builder()
                .email("new-user@example.com")
                .password("password")
                .firstname("newFirstName")
                .lastname("newLastName")
                .build();
        userRepository.save(newUser);
        Optional<User> user = userRepository.findById(newUser.getId());
        assertThat(user.get().getRole()).isEqualTo(EnumRole.CUSTOMER);
    }

    @Test
    public void test_save_同一のemailが存在する場合はエラーになること() {
        User newUser = User.builder()
                .email("test@example.com")
                .password("password")
                .firstname("newFirstName")
                .lastname("newLastName")
                .build();
        assertThrows(DuplicateKeyException.class, () -> userRepository.save(newUser));
    }
}
