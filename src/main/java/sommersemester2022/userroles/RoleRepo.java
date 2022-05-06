package sommersemester2022.userroles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Service
@Repository
public interface RoleRepo extends JpaRepository<RoleEntity, Integer> {
  Optional<RoleEntity> findByName(UserRole name);
}
