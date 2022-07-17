package uz.pdp.hrmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import uz.pdp.hrmanagement.enums.MonthName;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Salary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private User employee;          // qaysi ishchi uchun oylik

    @NotNull
    private  Double amountSalary;   // oylik miqdori

    @ManyToOne
    private Months month;           // qaysi oy uchun berilayotgani


    @Column(nullable = false,updatable = false)
    @CreationTimestamp
    private java.sql.Timestamp createdAt;  // oylikni qacon berilganligi
}
