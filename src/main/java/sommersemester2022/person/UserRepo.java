package sommersemester2022.person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * UserRepo implements JpaRepository and connects to database and handles all CRUD-operations for the user entities.
 * @author Florian Weinert
 */
@Transactional
@Service
public interface UserRepo extends JpaRepository<UserEntity, Integer> {
  /**
   * Returns optionally an user entity in case it finds the given combination of username and password in the database.
   * @param username
   * @param password
   * @return optional an user entity
   */
  Optional<UserEntity> findByUsernameAndPassword(String username, String password);

  /**
   * Returns the information whether the user name is already exisiting in the database.
   * @param username
   * @return boolean if the username already exists
   */
  boolean existsByUsername(String username);

  /**
   * Returns optionally an user entity in case the user name is exisiting in the database.
   * @param username
   * @return optional an user entity
   */
  Optional<UserEntity> findByUsername(String username);
}
