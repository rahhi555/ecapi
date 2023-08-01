package com.example.ecapi.security.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.ecapi.model.EnumRole;
import com.example.ecapi.security.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Sql("/common/VendorInsertOne.sql")
@Transactional
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void test_findByEmail() {
        User user = userRepository.findByEmail("test@example.com").orElseThrow();
        assertThat(user.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    public void test_findByEmailAndRole() {
        User user = userRepository.findByEmailAndRole("test@example.com", EnumRole.VENDOR).orElseThrow();
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getRole()).isEqualTo(EnumRole.VENDOR);
    }

    @Test
    public void test_findById() {
        int id = userRepository.findByEmail("test@example.com").orElseThrow().getId();
        User user = userRepository.findById(id).orElseThrow();
        assertThat(user.getId()).isEqualTo(id);
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
        User user = userRepository.findById(newUser.getId()).orElseThrow();
        assertThat(user.getId()).isEqualTo(newUser.getId());
        assertThat(user.getEmail()).isEqualTo(newUser.getEmail());
        assertThat(user.getLastname()).isEqualTo(newUser.getLastname());
        assertThat(user.getFirstname()).isEqualTo(newUser.getFirstname());
        assertThat(user.getRole()).isEqualTo(newUser.getRole());
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
        User user = userRepository.findById(newUser.getId()).orElseThrow();
        assertThat(user.getRole()).isEqualTo(EnumRole.CUSTOMER);
    }

    @Test
    public void test_save_異なるRoleの場合emailは重複可能であること() {
        User newUser = User.builder()
                .email("test@example.com")
                .password("password")
                .firstname("newFirstName")
                .lastname("newLastName")
                .role(EnumRole.CUSTOMER)
                .build();
        assertDoesNotThrow(() -> userRepository.save(newUser));
    }

    @Test
    public void test_save_同一のRoleの場合emailが重複不可であること() {
        User newUser = User.builder()
                .email("test@example.com")
                .password("password")
                .firstname("newFirstName")
                .lastname("newLastName")
                .role(EnumRole.VENDOR)
                .build();
        assertThrows(DuplicateKeyException.class, () -> userRepository.save(newUser));
    }
}
