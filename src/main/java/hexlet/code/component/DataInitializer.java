package hexlet.code.component;

import hexlet.code.dto.TaskStatusCreateDTO;
import hexlet.code.dto.UserCreateDTO;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.TaskStatusService;
import hexlet.code.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository statusRepository;

    @Autowired
    private TaskStatusService statusService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.findByEmail("hexlet@example.com").isEmpty()) {
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

        Map<String, String> statuses = new HashMap<>(
                Map.of("draft", "В разработке", "to_review", "На рассмотрении",
                        "to_be_fixed", "Должно быть исправлено",
                        "to_publish", "Готово к публикации", "published", "Опубликовано")
        );

        for (Map.Entry<String, String> status : statuses.entrySet()) {
            TaskStatusCreateDTO data = new TaskStatusCreateDTO();
            if (statusRepository.findBySlug(status.getKey()).isEmpty()) {
                data.setSlug(status.getKey());
                data.setName(status.getValue());
                statusService.create(data);
            }
        }
    }

}
