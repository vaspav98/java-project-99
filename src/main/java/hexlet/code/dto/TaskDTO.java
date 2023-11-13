package hexlet.code.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TaskDTO {
    private Long id;
    private String title;
    private Integer index;
    private String content;
    private String status;
    private Long assignee_id;
    private Date createdAt;
}
