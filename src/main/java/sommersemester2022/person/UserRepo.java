package sommersemester2022.person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import sommersemester2022.userroles.RoleEntity;
import sommersemester2022.userroles.UserRole;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public interface UserRepo extends JpaRepository<UserEntity, Integer> {
  Optional<UserEntity> findByUsernameAndPassword(String username, String password);
  boolean existsByUsername(String username);
  Optional<UserEntity> findByUsername(String username);


}
