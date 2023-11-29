package hexlet.code.controller;

import hexlet.code.dto.LabelCreateDTO;
import hexlet.code.dto.LabelDTO;
import hexlet.code.dto.LabelUpdateDTO;
import hexlet.code.service.LabelService;
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
@RequestMapping("/api/labels")
@AllArgsConstructor
public class LabelController {

    private final LabelService labelService;

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get list of all labels")
    @ApiResponse(responseCode = "200", description = "List of all labels")
    @GetMapping("")
    public ResponseEntity<List<LabelDTO>> index() {
        List<LabelDTO> labelDTOList = labelService.getAll();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(labelDTOList.size()))
                .body(labelDTOList);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get specific label by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label found"),
            @ApiResponse(responseCode = "404", description = "Label with that id not found")
    })
    @GetMapping("/{id}")
    public LabelDTO show(
            @Parameter(description = "Id of label to be found")
            @PathVariable Long id) {
        return labelService.getById(id);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Create new label")
    @ApiResponse(responseCode = "201", description = "Label created")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public LabelDTO create(
            @Parameter(description = "Label data to save")
            @Valid @RequestBody LabelCreateDTO data) {
        return labelService.create(data);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Update label by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label updated"),
            @ApiResponse(responseCode = "404", description = "Label with that id not found")
    })
    @PutMapping("/{id}")
    public LabelDTO update(
            @Parameter(description = "Id of label to be updated")
            @PathVariable Long id,
            @Parameter(description = "Label data to update")
            @Valid @RequestBody LabelUpdateDTO data) {
        return labelService.update(id, data);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Delete label by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Label deleted"),
            @ApiResponse(responseCode = "404", description = "Label with that id not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @Parameter(description = "Id of label to be deleted")
            @PathVariable Long id) {
        labelService.delete(id);
    }
}
