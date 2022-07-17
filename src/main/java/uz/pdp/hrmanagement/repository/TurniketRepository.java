package uz.pdp.hrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.pdp.hrmanagement.entity.Task;
import uz.pdp.hrmanagement.entity.Turniket;
import uz.pdp.hrmanagement.entity.User;
import uz.pdp.hrmanagement.enums.TaskStatus;

import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RepositoryRestResource(path = "turniket")
public interface TurniketRepository extends JpaRepository<Turniket,Integer> {
    Optional<Turniket> findByCreatedByAndStatus(UUID createdBy, boolean status);

    @Query("select tur from Turniket tur " +
            "where tur.createdBy = :employeeId and (tur.enterDateTime >= :start or tur.enterDateTime <= :finish)")
    List<Turniket> findAllByCreatedByAndEnterDateTimeAndExitDateTimeBefore(UUID employeeId, LocalDate start, LocalDate finish);
}
