package hexlet.code.service;

import hexlet.code.dto.LabelCreateDTO;
import hexlet.code.dto.LabelDTO;
import hexlet.code.dto.LabelUpdateDTO;
import hexlet.code.exception.MethodNotAllowedException;
import hexlet.code.exception.ResourceNotFountException;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private LabelMapper labelMapper;

    public List<LabelDTO> getAll() {
        List<Label> labels = labelRepository.findAll();
        return labels.stream()
                .map(l -> labelMapper.map(l))
                .toList();
    }

    public LabelDTO getById(Long id) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFountException("Label with id " + id + " not found"));
        return labelMapper.map(label);
    }

    public LabelDTO create(LabelCreateDTO data) {
        Label newLabel = labelMapper.map(data);
        labelRepository.save(newLabel);
        return labelMapper.map(newLabel);
    }

    public LabelDTO update(Long id, LabelUpdateDTO data) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFountException("Label with id " + id + " not found"));
        labelMapper.update(data, label);
        labelRepository.save(label);
        return labelMapper.map(label);
    }

    public void delete(Long id) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFountException("Label with id " + id + " not found"));

        if (!label.getTasks().isEmpty()) {
            throw new MethodNotAllowedException("You cannot delete a label. The label is associated with tasks.");
        }

        labelRepository.deleteById(id);
    }

}
