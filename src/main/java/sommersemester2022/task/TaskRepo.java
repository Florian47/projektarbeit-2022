package sommersemester2022.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import sommersemester2022.person.UserEntity;
import sommersemester2022.solution.SolutionEntity;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public interface TaskRepo extends JpaRepository<TaskEntity, Integer> {

  List<TaskEntity> findAllByCategoryAndDifficulty(TaskCategory category, TaskDifficulty difficulty);

  boolean existsByName(String name);

}
