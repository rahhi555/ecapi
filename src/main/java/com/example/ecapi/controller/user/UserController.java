package com.example.ecapi.controller.user;

import com.example.ecapi.controller.UsersApi;
import com.example.ecapi.model.DTOUser;
import com.example.ecapi.model.EnumRole;
import com.example.ecapi.security.User;
import com.example.ecapi.security.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UsersApi {

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public ResponseEntity<DTOUser> getUser(Integer id) {
        User user =
                repository
                        .findById(id)
                        .orElseThrow(() -> new UserIdNotFoundException("User Not Found"));
        DTOUser DTOUser = new DTOUser(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                EnumRole.valueOf(user.getRole().name())
        );

        return ResponseEntity.ok(DTOUser);
    }

    @GetMapping("/users/me")
    public ResponseEntity<DTOUser> me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        DTOUser DTOUser = new DTOUser(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                EnumRole.valueOf(user.getRole().name())
        );
        return ResponseEntity.ok(DTOUser);
    }
}
