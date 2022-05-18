package sommersemester2022.solution;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import sommersemester2022.person.UserEntity;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Service
public interface SolutionRepo extends JpaRepository<SolutionEntity, Integer> {

  Optional<SolutionEntity> findSolutionEntityByRelatedTask_Id(int id);
}
