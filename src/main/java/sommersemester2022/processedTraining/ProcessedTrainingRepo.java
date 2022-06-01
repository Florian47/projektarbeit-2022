package sommersemester2022.processedTraining;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * ProcessedTrainingRepo implementiert das JpaRepository und schafft die Verbindung zur Datenbank. Außerdem führt das
 * Repository u.a. alle CRUD-Operationen für die bearbeitetes Training-Entität durch.
 * @author Tobias Esau, Alexander Kiehl
 */
@Transactional
@Service
public interface ProcessedTrainingRepo extends JpaRepository<ProcessedTrainingEntity, Integer> {

  /**
   * Gibt eine Liste aller Trainings zurück, welche bereits von dem Schüler mit der angegebenen ID bearbeitet wurden.
   * @return
   */
  List<ProcessedTrainingEntity> findAllByStudentId(int id);
}
