package uz.pdp.hrmanagement.payload;

import lombok.Data;

import java.util.UUID;
@Data
public class SalaryDto {
    private UUID employee;
    private  Double amountSalary;
    private Integer monthId;

}
