package sommersemester2022.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public interface TaskRepo extends JpaRepository<TaskEntity, Integer> {

}
