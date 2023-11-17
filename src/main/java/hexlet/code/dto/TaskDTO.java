package hexlet.code.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class TaskDTO {
    private Long id;
    private String title;
    private Integer index;
    private String content;
    private String status;
    private Long assignee_id;
    private List<Long> taskLabelIds;
    private Date createdAt;
}
