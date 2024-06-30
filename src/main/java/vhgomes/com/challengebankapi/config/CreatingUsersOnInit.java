package vhgomes.com.challengebankapi.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import vhgomes.com.challengebankapi.models.User;
import vhgomes.com.challengebankapi.repository.UserRepository;

import java.math.BigDecimal;

@Configuration
public class CreatingUsersOnInit implements CommandLineRunner {

    private UserRepository userRepository;

    public CreatingUsersOnInit(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        for (int i = 0; i < 3; i++) {
            var userToCreate = new User();
            userToCreate.setTotalSaldo(BigDecimal.valueOf(2500));
            userToCreate.setUserId((long) i);
            userRepository.save(userToCreate);
            System.out.println("User created: " + userToCreate);
        }
    }
}
