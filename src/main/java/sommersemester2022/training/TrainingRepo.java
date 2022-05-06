package sommersemester2022.training;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Service
public interface TrainingRepo extends JpaRepository<TrainingEntity, Integer> {
}
