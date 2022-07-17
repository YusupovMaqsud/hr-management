package uz.pdp.hrmanagement.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.hrmanagement.entity.Task;
import uz.pdp.hrmanagement.entity.Turniket;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {
    private String message;
    private boolean success;
    private Object object;
    List<Turniket> turniketList;
    List<Task> taskList;


    public ApiResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}
