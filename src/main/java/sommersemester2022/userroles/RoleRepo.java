package sommersemester2022.userroles;

import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import sommersemester2022.person.UserEntity;
import sommersemester2022.task.TaskCategory;
import sommersemester2022.task.TaskDifficulty;
import sommersemester2022.task.TaskEntity;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * @author David Wiebe, Tobias Esau
 * Eine Role Repositoryklasse in der die SQL Abfragen für die Klasse RoleEntity definiert werden.
 * @see RoleController
 */
@Transactional
@Service
@Repository
public interface RoleRepo extends JpaRepository<RoleEntity, Integer> {
  RoleEntity findByName(UserRole name);
  boolean existsByName(UserRole name);
  List<RoleEntity> findAll();

  /**
   * Es wurde eine etwas komplexere SQL Abfrage definiert, um alle Rollen eines Users aus der Datenbank zu ziehen.
   * @param authentication
   * @return Liste RoleEntity
   */
  @Query(value="SELECT roles.id, name FROM app.user_entity_roles INNER JOIN app.roles ON app.user_entity_roles.roles_id = app.roles.id where user_entity_id = ?#{#authentication.principal.id}", nativeQuery = true)
  List<RoleEntity> findALLRoles(@Param("authentication") Authentication authentication);
}
