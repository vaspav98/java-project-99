package hexlet.code.service;

import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserDTO;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.exception.MethodNotAllowedException;
import hexlet.code.exception.ResourceNotFountException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder encoder;

    public List<UserDTO> getAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(u -> userMapper.map(u))
                .toList();
    }

    public UserDTO getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFountException("User with id " + id + " not found"));
        return userMapper.map(user);
    }

    public UserDTO create(UserCreateDTO data) {
        User newUser = userMapper.map(data);
        String encodedPassword = encoder.encode(data.getPassword());
        newUser.setEncodedPassword(encodedPassword);
        userRepository.save(newUser);
        return userMapper.map(newUser);
    }

    public UserDTO update(Long id, UserUpdateDTO data) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFountException("User with id " + id + " not found"));
        userMapper.update(data, user);

        if (data.getPassword() != null) {
            String encodedPassword = encoder.encode(data.getPassword().get());
            user.setEncodedPassword(encodedPassword);
        }

        userRepository.save(user);
        return userMapper.map(user);
    }

    public void delete(Long id) {
        if (!taskRepository.findByAssigneeId(id).isEmpty()) {
            throw new MethodNotAllowedException("You cannot delete a user. The user is associated with tasks.");
        }

        userRepository.deleteById(id);
    }


}
