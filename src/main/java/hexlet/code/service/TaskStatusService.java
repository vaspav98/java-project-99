package hexlet.code.service;

import hexlet.code.dto.TaskStatusCreateDTO;
import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.dto.TaskStatusUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskStatusService {

    private final TaskStatusRepository statusRepository;

    private final TaskStatusMapper statusMapper;

    public List<TaskStatusDTO> getAll() {
        List<TaskStatus> statuses = statusRepository.findAll();
        return statuses.stream()
                .map(t -> statusMapper.map(t))
                .toList();
    }

    public TaskStatusDTO getById(Long id) {
        TaskStatus status = statusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task status with id " + id + "not found"));
        return statusMapper.map(status);
    }

    public TaskStatusDTO create(TaskStatusCreateDTO data) {
        TaskStatus newStatus = statusMapper.map(data);
        statusRepository.save(newStatus);
        return statusMapper.map(newStatus);
    }

    public TaskStatusDTO update(Long id, TaskStatusUpdateDTO data) {
        TaskStatus status = statusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task status with id " + id + "not found"));
        statusMapper.update(data, status);
        statusRepository.save(status);
        return statusMapper.map(status);
    }

        public void delete(Long id) {
        TaskStatus status = statusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task status with id " + id + "not found"));

        statusRepository.deleteById(id);
    }

}
