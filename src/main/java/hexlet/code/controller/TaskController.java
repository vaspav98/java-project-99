package hexlet.code.controller;

import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskParamsDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
@RequestMapping("/api/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get list of all tasks")
    @ApiResponse(responseCode = "200", description = "List of all tasks")
    @GetMapping("")
    public ResponseEntity<List<TaskDTO>> index(
            @Parameter(description = "Options for filtering tasks")
            TaskParamsDTO params) {
        List<TaskDTO> taskDTOList = taskService.getAll(params);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(taskDTOList.size()))
                .body(taskDTOList);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get specific task by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task found"),
            @ApiResponse(responseCode = "404", description = "Task with that id not found")
    })
    @GetMapping("/{id}")
    public TaskDTO show(
            @Parameter(description = "Id of task to be found")
            @PathVariable Long id) {
        return taskService.getById(id);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Create new task")
    @ApiResponse(responseCode = "201", description = "Task created")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO create(
            @Parameter(description = "Task data to save")
            @Valid @RequestBody TaskCreateDTO data) {
        return taskService.create(data);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Update task by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated"),
            @ApiResponse(responseCode = "404", description = "Task with that id not found")
    })
    @PutMapping("/{id}")
    public TaskDTO update(
            @Parameter(description = "Id of task to be updated")
            @PathVariable Long id,
            @Parameter(description = "Task data to update")
            @Valid @RequestBody TaskUpdateDTO data) {
        return taskService.update(id, data);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Delete task by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted"),
            @ApiResponse(responseCode = "404", description = "Task with that id not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @Parameter(description = "Id of task to be deleted")
            @PathVariable Long id) {
        taskService.delete(id);
    }
}
