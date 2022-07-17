package uz.pdp.hrmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import uz.pdp.hrmanagement.enums.TaskStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String name;

    @NotNull
    private String body;

    @NotNull
    private Date deadLine;         //vazifani yakunlanadigan vaqti

    @ManyToOne
    private User responsibleUser;  // vazifa uchun ma'sul xodim

    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus; // qaysi holatdaligi


    @CreatedBy
    private UUID createdBy;        //vazifa qo'shuvchi
}
