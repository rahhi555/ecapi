package utils;

import com.example.ecapi.security.Role;
import com.example.ecapi.security.User;
import com.example.ecapi.security.controller.RegisterRequest;
import com.example.ecapi.security.repository.UserRepository;
import com.example.ecapi.security.service.AuthenticationService;
import org.springframework.stereotype.Component;

@Component
public class UserInsertOne {
    public record UserAndToken(User user, String token, String rawPassword) {}

    public static UserAndToken createVendor(AuthenticationService service, UserRepository repository) {
        RegisterRequest register = RegisterRequest.builder()
                .email("vendor@example.com")
                .password("password")
                .firstname("太郎")
                .lastname("ベンダー")
                .role(Role.VENDOR)
                .build();
        String token = service.register(register).getToken();
        User user = repository.findByEmail("vendor@example.com").orElseThrow();
        return new UserAndToken(user, token, "password");
    }

    public static UserAndToken createCustomer(AuthenticationService service, UserRepository repository) {
        RegisterRequest register = RegisterRequest.builder()
                .email("customer@example.com")
                .password("password")
                .firstname("二郎")
                .lastname("カスタマー")
                .role(Role.CUSTOMER)
                .build();
        String token = service.register(register).getToken();
        User user = repository.findByEmail("customer@example.com").orElseThrow();
        return new UserAndToken(user, token, "password");
    }
}
