package uz.pdp.hrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.hrmanagement.entity.Task;
import uz.pdp.hrmanagement.entity.User;
import uz.pdp.hrmanagement.enums.TaskStatus;
import uz.pdp.hrmanagement.payload.ApiResponse;
import uz.pdp.hrmanagement.payload.TaskDto;
import uz.pdp.hrmanagement.repository.TaskRepository;
import uz.pdp.hrmanagement.repository.UserRepository;

import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    JavaMailSender javaMailSender;

    public ApiResponse createTask(TaskDto taskDto) {
        //YENGI VAZIFA YARATISH
        Task newTask = new Task();
        newTask.setName(taskDto.getName());
        newTask.setBody(taskDto.getBody());
        newTask.setDeadLine(taskDto.getDeadLine());
        Optional<User> optionalUser = userRepository.findById(taskDto.getResponsibleId());
        if (!optionalUser.isPresent())
            return new ApiResponse("Bunday xodim topilmadi", false);
        //MASUL XODIM LAVOZIMINI ANIQLASH
        User responsibleUser = optionalUser.get();
        //VAZIFANI YUKLASH
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        if (user.getRoles().equals("DIRECTOR")) {
            if (user.getRoles().equals("MANAGER")) {
                newTask.setResponsibleUser(responsibleUser);
                newTask.setTaskStatus(TaskStatus.NEW);
                taskRepository.save(newTask);
                sendEmailAboutTask(responsibleUser.getEmail(), newTask.getId());
            } else {
                return new ApiResponse("Siz bu foydalanuvchiga vazifa yuklay olmaysiz!", false);
            }
        }

        if (user.getRoles().equals("HR_MANAGER")) {
            if (user.getRoles().equals("MANAGER") || user.getRoles().equals("EMPLOYEE")) {
                newTask.setResponsibleUser(responsibleUser);
                newTask.setTaskStatus(TaskStatus.NEW);
                taskRepository.save(newTask);
                sendEmailAboutTask(responsibleUser.getEmail(), newTask.getId());
            } else {
                return new ApiResponse("Siz bu foydalanuvchiga vazifa yuklay olmaysiz!", false);
            }
        }
        return new ApiResponse("Vazifa " + user.getFirstName() + " " + user.getLastName() + " tomonidan "
                + user.getRoles() + " " + "ga yuklatildi", true);
    }






    public boolean sendEmailAboutTask(String sendingEmail, Integer taskId) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("shahzadenishonova@gmail.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Vazifa yuklatilganligi haqida ma'lumot");
            mailMessage.setText("<a href='http://localhost:8080/api/auth/getTask?taskId=" + taskId + "&email=" + sendingEmail + "'>VAzifani Tasdiqlang</a>");
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }






    public ApiResponse getTask(Integer taskId, String email) {
        Optional<Task> optionalTask = taskRepository.findByIdAndResponsibleUserEmail(taskId, email);
        if (!optionalTask.isPresent())
            return new ApiResponse("Bu xodimda bunday vazifa yuklatilmagan", false);
        Task task = optionalTask.get();
        task.setTaskStatus(TaskStatus.IN_PROCESS);
        taskRepository.save(task);
        return new ApiResponse("Vazifani qabul qildingiz. Vazifani yakunlash kerak boladigan sana " + task.getDeadLine(), true);
    }





    public ApiResponse completeTask(Integer taskId) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (!optionalTask.isPresent())
            return new ApiResponse("Bunday vazifa topilmadi", false);
        Task task = optionalTask.get();
        task.setTaskStatus(TaskStatus.COMPLETED);
        taskRepository.save(task);
        // Vazifani biriktirgan xodimni ID si boyica topish
        Optional<User> optionalUser = userRepository.findById(task.getCreatedBy());
        if (!optionalUser.isPresent())
            return new ApiResponse("Bunday xodim topilmadi", false);
        User createUser = optionalUser.get();
        sendEmailAboutCompleteTask(createUser.getEmail(), task.getId());
        return new ApiResponse("Vazifa bajarilganlifgi haqidagi habar yuborildi", true);

    }





    public boolean sendEmailAboutCompleteTask(String sendingEmail, Integer taskId) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("Company@gmail.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Vazifa haqida ma'lumot");
            mailMessage.setText("Vazifa bajarildi");
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
