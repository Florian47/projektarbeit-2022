package sommersemester2022.processedTraining;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * ProcessedTrainingRepo implements JpaRepository and connects to database and handles all CRUD-operations for the
 * processed training entities.
 * @author Tobias Esau, Alexander Kiehl
 */
@Transactional
@Service
public interface ProcessedTrainingRepo extends JpaRepository<ProcessedTrainingEntity, Integer> {
}
