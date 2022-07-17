package uz.pdp.hrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.pdp.hrmanagement.entity.Role;
import uz.pdp.hrmanagement.enums.RoleName;

@RepositoryRestResource(path = "role")
public interface RoleRepository extends JpaRepository<Role, Integer>{
    Role findByRoleName(RoleName roleName);
}
