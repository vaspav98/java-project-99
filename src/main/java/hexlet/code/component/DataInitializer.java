package hexlet.code.component;

import hexlet.code.dto.UserCreateDTO;
import hexlet.code.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        String firstName = "Pavel";
        String lastName = "Vasilev";
        String email = "hexlet@example.com";
        String password = "qwerty";

        UserCreateDTO userData = new UserCreateDTO();
        userData.setFirstName(firstName);
        userData.setLastName(lastName);
        userData.setEmail(email);
        userData.setPassword(password);

        userService.create(userData);
    }
}
