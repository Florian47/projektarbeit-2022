package sommersemester2022.processedTraining;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public interface ProcessedTrainingRepo extends JpaRepository<ProcessedTrainingEntity, Integer> {
}
