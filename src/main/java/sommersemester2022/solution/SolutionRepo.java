package sommersemester2022.solution;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import sommersemester2022.person.UserEntity;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * SolutionRepo implementiert das JpaRepository und schafft die Verbindung zur Datenbank. Außerdem führt das
 * Repository u.a. alle CRUD-Operationen für die Lösung durch.
 * @author Tobias Esau, Alexander Kiehl
 */
@Transactional
@Service
public interface SolutionRepo extends JpaRepository<SolutionEntity, Integer> {
}
