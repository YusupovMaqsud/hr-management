package uz.pdp.hrmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Turniket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private boolean status;               // true-ishxonaga kirdi; false-ishdan chiqdi

    @CreatedBy
    private UUID createdBy;               // ishga kiruvchi user

    @NotNull
    private LocalDateTime enterDateTime;   // ishga kirgan vaqti

    @NotNull
    private LocalDateTime exitDateTime;    // ishdan chiqqan vaqti
}
