package utils;

import com.example.ecapi.model.EnumRole;
import com.example.ecapi.model.FormRegister;
import com.example.ecapi.security.User;
import com.example.ecapi.security.repository.UserRepository;
import com.example.ecapi.security.service.AuthenticationService;
import org.springframework.stereotype.Component;

@Component
public class UserInsertOne {
    public static UserAndToken createVendor(AuthenticationService service, UserRepository repository) {
        FormRegister register = new FormRegister("test@example.com","password","太郎","ベンダー",EnumRole.VENDOR);
        String token = service.register(register).getToken();
        User user = repository.findByEmailAndRole("test@example.com", EnumRole.VENDOR).orElseThrow();
        return new UserAndToken(user, token, "password");
    }

    public static UserAndToken createCustomer(AuthenticationService service, UserRepository repository) {
        FormRegister register = new FormRegister("test@example.com","password","二郎","カスタマー",EnumRole.CUSTOMER);
        String token = service.register(register).getToken();
        User user = repository.findByEmailAndRole("test@example.com", EnumRole.CUSTOMER).orElseThrow();
        return new UserAndToken(user, token, "password");
    }

    public record UserAndToken(User user, String token, String rawPassword) {}
}
