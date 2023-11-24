package hexlet.code.controller;

import hexlet.code.dto.UserCreateDTO;
import hexlet.code.dto.UserDTO;
import hexlet.code.dto.UserUpdateDTO;
import hexlet.code.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@AllArgsConstructor
public class UserController {

    private static final String ONLY_OWNER_BY_ID = """
            @userUtils.getCurrentUser().getId() == #id
        """;

    private final UserService userService;

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get list of all users")
    @ApiResponse(responseCode = "200", description = "List of all users")
    @GetMapping("")
    public ResponseEntity<List<UserDTO>> index() {
        List<UserDTO> userDTOList = userService.getAll();
        return ResponseEntity.status(HttpStatus.OK)
                .header("X-Total-Count", String.valueOf(userDTOList.size()))
                .body(userDTOList);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get specific user by his id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User with that id not found")
    })
    @GetMapping("/{id}")
    public UserDTO show(
            @Parameter(description = "Id of user to be found")
            @PathVariable Long id) {
        return userService.getById(id);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Create new user")
    @ApiResponse(responseCode = "201", description = "User created")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(
            @Parameter(description = "User data to save")
            @Valid @RequestBody UserCreateDTO data) {
        return userService.create(data);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Update user by his id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "404", description = "User with that id not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public UserDTO update(
            @Parameter(description = "Id of user to be updated")
            @PathVariable Long id,
            @Parameter(description = "User data to update")
            @Valid @RequestBody UserUpdateDTO data) {

        return userService.update(id, data);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Delete user by his id")
    @ApiResponse(responseCode = "204", description = "User deleted")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public void delete(
            @Parameter(description = "Id of user to be deleted")
            @PathVariable final Long id) {

        userService.delete(id);
    }
}
