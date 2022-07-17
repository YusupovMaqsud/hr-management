package uz.pdp.hrmanagement.payload;

import lombok.Data;
import java.util.Date;
import java.util.UUID;

@Data
public class TaskDto {
    private String name;
    private String body;
    private Date deadLine;
    private UUID responsibleId;
}
