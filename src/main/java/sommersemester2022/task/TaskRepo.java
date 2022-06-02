package sommersemester2022.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * TaskRepo implementiert das JpaRepository und schafft die Verbindung zur Datenbank. Außerdem führt das
 * Repository u.a. alle CRUD-Operationen für die Aufgaben durch.
 * @author Tobias Esau, Alexander Kiehl
 */
@Transactional
@Service
public interface TaskRepo extends JpaRepository<TaskEntity, Integer> {
  /**
   * Gibt alle Aufgaben mit angegebener Kategorie und Schwierigkeit zurück
   * @param category Kategorie der Aufgabe
   * @param difficulty Schwierigkeit der Aufgabe
   * @return Liste aller Aufgaben mit angegebener Kategorie und Schwierigkeit
   */
  List<TaskEntity> findAllByCategoryAndDifficultyAndIndividualFalse(TaskCategory category, TaskDifficulty difficulty);

  /**
   * Gibt alle "originalen" Aufgaben zurück, also die nicht beim Training kopierten.
   * @return Liste aller "originalen" Aufgaben
   */
  List<TaskEntity> findAllByIndividualFalse();

  boolean existsByName(String name);
}
