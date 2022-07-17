package uz.pdp.hrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.hrmanagement.entity.Role;
import uz.pdp.hrmanagement.entity.Task;
import uz.pdp.hrmanagement.entity.Turniket;
import uz.pdp.hrmanagement.entity.User;
import uz.pdp.hrmanagement.enums.RoleName;
import uz.pdp.hrmanagement.enums.TaskStatus;
import uz.pdp.hrmanagement.payload.ApiResponse;
import uz.pdp.hrmanagement.repository.RoleRepository;
import uz.pdp.hrmanagement.repository.TaskRepository;
import uz.pdp.hrmanagement.repository.TurniketRepository;
import uz.pdp.hrmanagement.repository.UserRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    TurniketRepository turniketRepository;
    @Autowired
    TaskRepository taskRepository;

    public ApiResponse findAllEmployees() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        if (user.getRoles().equals("DIRECTOR") || user.getRoles().equals("HR_MANAGER")) {
            Role roleEmployee = roleRepository.findByRoleName(RoleName.EMPLOYEE);
            List<User> employeeList = userRepository.findAllByRoleIn(Collections.singleton(roleEmployee));
            return new ApiResponse("Barcha xodimlar ro'yhati: " + employeeList, true);
        }
        return new ApiResponse("Sizda xodimlarni ko'rish uchun xuquq yo'q", false);
    }

    public ApiResponse findByData(UUID id, LocalDate start, LocalDate finish) {
        Optional<User> optionalEmployee = userRepository.findById(id);
        if (!optionalEmployee.isPresent())
            return new ApiResponse("Bunday xodim mavjud emas!", false);
        User user = optionalEmployee.get();
        if (user.getRoles().equals("EMPLOYEE")) {
            List<Turniket> turniketList = turniketRepository.findAllByCreatedByAndEnterDateTimeAndExitDateTimeBefore(id, start, finish);
            if (turniketList.isEmpty())
                return new ApiResponse("Bunday vaqt oraligidagi natija topilmadi ", false);
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setTurniketList(turniketList);
            List<Task> taskList = taskRepository.findAllByTaskStatusAndResponsibleUser(TaskStatus.COMPLETED, user);
            apiResponse.setTaskList(taskList);
            apiResponse.setSuccess(true);
            apiResponse.setMessage("Xodimning bajargan vazifalari va turniket malumotlari");
            return apiResponse;
        } else {
            return new ApiResponse("Boshqa xodim haqida ma'lumot olish mumkin emas", false);
        }
    }

}
