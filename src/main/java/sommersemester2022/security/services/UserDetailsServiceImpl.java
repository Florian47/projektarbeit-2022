package sommersemester2022.security.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sommersemester2022.person.UserEntity;
import sommersemester2022.person.UserRepo;

/**@author David Wiebe
 * Die Klasse UserDetailsServiceImpl ist eine Art Repository Klasse für die Klasse UserDetailsImpl.
 * Diese sucht den Benutzer über den Usernamen. Indem der Datenbankaufruf über Die UserRepository Klasse aufgerufen wird.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  UserRepo userRepository;
  /**
   * Die Methode loadUserByUsername lädt die Benutzerdaten aus der Datenbank und gibt sie an die Methode Build der Klasse
   * UserDetailsImpl weiter.
   * @param username the username identifying the user whose data is required.
   * @return UserDetailsImpl.build(user)
   * @throws UsernameNotFoundException
   */
  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity user = userRepository.findByUsername(username)
      .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
    return UserDetailsImpl.build(user);
  }
}

