package uz.pdp.hrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.pdp.hrmanagement.entity.Salary;
import uz.pdp.hrmanagement.entity.Task;
import uz.pdp.hrmanagement.entity.User;
import uz.pdp.hrmanagement.enums.TaskStatus;

import javax.validation.constraints.Email;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "task")
public interface TaskRepository extends JpaRepository<Task,Integer> {
    Optional<Task> findByIdAndResponsibleUserEmail(Integer id, @Email String responsibleUser_email);

    List<Task> findAllByTaskStatusAndResponsibleUser(TaskStatus taskStatus, User responsibleUser);
}
