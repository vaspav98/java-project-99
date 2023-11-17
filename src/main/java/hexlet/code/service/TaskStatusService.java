package hexlet.code.service;

import hexlet.code.dto.TaskStatusCreateDTO;
import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.dto.TaskStatusUpdateDTO;
import hexlet.code.exception.MethodNotAllowedException;
import hexlet.code.exception.ResourceNotFountException;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskStatusService {

    @Autowired
    private TaskStatusRepository statusRepository;

    @Autowired
    private TaskStatusMapper statusMapper;

    public List<TaskStatusDTO> getAll() {
        List<TaskStatus> statuses = statusRepository.findAll();
        return statuses.stream()
                .map(t -> statusMapper.map(t))
                .toList();
    }

    public TaskStatusDTO getById(Long id) {
        TaskStatus status = statusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFountException("Task status with id " + id + "not found"));
        return statusMapper.map(status);
    }

    public TaskStatusDTO create(TaskStatusCreateDTO data) {
        TaskStatus newStatus = statusMapper.map(data);
        statusRepository.save(newStatus);
        return statusMapper.map(newStatus);
    }

    public TaskStatusDTO update(Long id, TaskStatusUpdateDTO data) {
        TaskStatus status = statusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFountException("Task status with id " + id + "not found"));
        statusMapper.update(data, status);
        statusRepository.save(status);
        return statusMapper.map(status);
    }

        public void delete(Long id) {
        TaskStatus status = statusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFountException("Task status with id " + id + "not found"));
        if (!status.getTasks().isEmpty()) {
            throw new MethodNotAllowedException("You cannot delete a status. The status is associated with tasks.");
        }

        statusRepository.deleteById(id);
    }

}
