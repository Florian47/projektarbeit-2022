package sommersemester2022.processedTraining;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import sommersemester2022.training.TrainingEntity;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public interface ProcessedTrainingRepo extends JpaRepository<ProcessedTrainingEntity, Integer> {

  List<ProcessedTrainingEntity> findAllById(int id);
}
