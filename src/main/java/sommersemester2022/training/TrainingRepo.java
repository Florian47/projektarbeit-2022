package sommersemester2022.training;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import sommersemester2022.person.UserEntity;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * TrainingRepo implementiert das JpaRepository und schafft die Verbindung zur Datenbank. Außerdem führt das
 * Repository u.a. alle CRUD-Operationen für die Trainings durch.
 * @author Tobias Esau, Alexander Kiehl
 */
@Transactional
@Service
public interface TrainingRepo extends JpaRepository<TrainingEntity, Integer> {
  /**
   * Gibt alle Trainings zurück, welche speziell für den angegebenen User freigegeben wurden.
   * @param user User, für den Trainings gesucht werden
   * @return Liste aller Trainings, die für den User vorhanden sind
   */
  Optional<List<TrainingEntity>> findByStudentsAndIndividualTrue(Optional<UserEntity> user);

  /**
   * Gibt alle Trainings zurück, welche individuell erstellt wurden.
   * @return Liste aller Trainings, welche individuell erstellt wurden
   */
  List<TrainingEntity> findByIndividualTrue();
}
