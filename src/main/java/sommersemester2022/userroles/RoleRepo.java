package sommersemester2022.userroles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import sommersemester2022.person.UserEntity;
import sommersemester2022.task.TaskCategory;
import sommersemester2022.task.TaskDifficulty;
import sommersemester2022.task.TaskEntity;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@Repository
public interface RoleRepo extends JpaRepository<RoleEntity, Integer> {
  RoleEntity findByName(UserRole name);
  boolean existsByName(UserRole name);
  List<RoleEntity> findAll();

  @Query(value="SELECT roles.id, name FROM app.user_entity_roles INNER JOIN app.roles ON app.user_entity_roles.roles_id = app.roles.id where = 4", nativeQuery = true)
  List<RoleEntity> findALLRoles(int id);
}
