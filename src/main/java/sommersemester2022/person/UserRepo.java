package sommersemester2022.person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * UserRepo implementiert das JpaRepository und schafft die Verbindung zur Datenbank. Außerdem führt das Repository alle
 * CRUD-Operationen für die User-Entität durch.
 * @author Florian Weinert
 */
@Transactional
@Service
public interface UserRepo extends JpaRepository<UserEntity, Integer> {
  /**
   * Gibt optional eine User-Entität zurück, wenn die angegebene Kombination aus Username und Passwort in der Datenbank
   * existiert.
   * @param username - der Benutzername
   * @param password - das Passwort
   * @return optional die User-Entität
   */
  Optional<UserEntity> findByUsernameAndPassword(String username, String password);

  /**
   * Gibt die Information zurück, ob ein Benzutzer bereits im System existiert.
   * @param username - der Benutzername
   * @return Wahrheitswert, ob der Benutzer existiert
   */
  boolean existsByUsername(String username);

  /**
   * Gibt optional eine User-Entität zurück, wenn der angegebene Username in der Datenbank existiert.
   * @param username - der Benutzername
   * @return optional eine User-Entität
   */
  Optional<UserEntity> findByUsername(String username);
}
