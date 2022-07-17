package uz.pdp.hrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.pdp.hrmanagement.entity.Months;
import uz.pdp.hrmanagement.enums.MonthName;

@RepositoryRestResource(path = "month")
public interface MonthRepository extends JpaRepository<Months, Integer>{
}
