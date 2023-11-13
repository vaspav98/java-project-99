package hexlet.code.controller;

import hexlet.code.dto.UserCreateDTO;
import hexlet.code.dto.UserDTO;
import hexlet.code.dto.UserUpdateDTO;
import hexlet.code.exception.AccessDeniedException;
import hexlet.code.exception.MethodNotAllowedException;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.UserService;
import hexlet.code.util.UserUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("")
    public ResponseEntity<List<UserDTO>> index() {
        List<UserDTO> userDTOList = userService.getAll();
        return ResponseEntity.status(HttpStatus.OK)
                .header("X-Total-Count", String.valueOf(userDTOList.size()))
                .body(userDTOList);
    }

    @GetMapping("/{id}")
    public UserDTO show(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@Valid @RequestBody UserCreateDTO data) {
        return userService.create(data);
    }

    @PutMapping("/{id}")
    public UserDTO update(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO data) {
        if (userUtils.getCurrentUser().getId() != id) {
            throw new AccessDeniedException("You do not have permission to update this user");
        }
        return userService.update(id, data);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (userUtils.getCurrentUser().getId() != id) {
            throw new AccessDeniedException("You do not have permission to delete this user");
        }

        if (!taskRepository.findByAssigneeId(id).isEmpty()) {
            throw new MethodNotAllowedException("You cannot delete a user. The user is associated with tasks.");
        }

        userService.delete(id);
    }
}
