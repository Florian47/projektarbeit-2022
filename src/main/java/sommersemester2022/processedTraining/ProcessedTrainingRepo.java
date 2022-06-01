package sommersemester2022.processedTraining;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * ProcessedTrainingRepo implementiert das JpaRepository und schafft die Verbindung zur Datenbank. Außerdem führt das
 * Repository u.a. alle CRUD-Operationen für die bearbeitetes Training-Entität durch.
 * @author Tobias Esau, Alexander Kiehl
 */
@Transactional
@Service
public interface ProcessedTrainingRepo extends JpaRepository<ProcessedTrainingEntity, Integer> {
}
