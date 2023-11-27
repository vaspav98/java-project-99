package hexlet.code.service;

import hexlet.code.dto.LabelCreateDTO;
import hexlet.code.dto.LabelDTO;
import hexlet.code.dto.LabelUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LabelService {

    private final LabelRepository labelRepository;

    private final LabelMapper labelMapper;

    public List<LabelDTO> getAll() {
        List<Label> labels = labelRepository.findAll();
        return labels.stream()
                .map(l -> labelMapper.map(l))
                .toList();
    }

    public LabelDTO getById(Long id) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found"));
        return labelMapper.map(label);
    }

    public LabelDTO create(LabelCreateDTO data) {
        Label newLabel = labelMapper.map(data);
        labelRepository.save(newLabel);
        return labelMapper.map(newLabel);
    }

    public LabelDTO update(Long id, LabelUpdateDTO data) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found"));
        labelMapper.update(data, label);
        labelRepository.save(label);
        return labelMapper.map(label);
    }

    public void delete(Long id) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found"));

        labelRepository.deleteById(id);
    }

}
