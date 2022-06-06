package sommersemester2022.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sommersemester2022.security.services.UserDetailsImpl;
import sommersemester2022.security.services.jwt.JwtUtils;
import sommersemester2022.training.TrainingEntity;
import sommersemester2022.training.TrainingRepo;
import sommersemester2022.userroles.RoleRepo;

import java.util.List;
import java.util.Optional;

import static sommersemester2022.userroles.UserRole.ROLE_STUDENT;

/**
 * UserController ist die Controller-Klasse für die User-Entität. Sie kontrolliert alle Aktivitäten, welche mit den
 * Usern ausgeführt werden können und gibt die Informationen an das Repository weiter.
 * (z.B. alle CRUD-Operationen)
 * @author Florian Weinert, David Wiebe
 * @see    UserRepo
 */

@RestController @Service @Transactional
public class UserController {
  @Autowired
  private UserRepo userRepo;
  @Autowired
  private RoleRepo roleRepo;
  @Autowired
  private TrainingRepo trainingRepo;
  @Autowired
  PasswordEncoder encoder;
  @Autowired
  AuthenticationManager authenticationManager;
  @Autowired
  JwtUtils jwtUtils;

  UserDetailsImpl userDetails;

  /**
   * Authentifiziert einen User (Login).
   * @param person Frontend Daten für den User
   * @return ResponseEntity
   */
  @PostMapping("/users/authenticate")
  public ResponseEntity authenticate(@RequestBody UserEntity person) {
    Authentication authentication = authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(person.getUsername(), person.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);
    person.roles = roleRepo.findALLRoles(authentication);

//    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//    List<String> roles = userDetails.getAuthorities().stream()
//      .map(item -> item.getAuthority())
//      .collect(Collectors.toList());
    return ResponseEntity.ok().header("Authorization", jwtUtils.generateJwtToken(authentication)).body(userRepo.findByUsername(person.getUsername()).get());
//    return userRepo.findByUsernameAndPassword(person.getUsername(), person.getPassword()).orElseThrow(() ->
//      new EntityNotFoundException("Username and password does not match a user"));

  }

//("/userloesung/aufgabe{id}/luecke{id}/)
  //@PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER') or hasRole('ADMINISTRATOR')")t
  /**
   * Registriert einen neuen Benutzer im System
   * @param person Frontend Daten für den User
   * @return gespeicherte User-Entität
   */
  @PostMapping("/users/register")
  public UserEntity register(@RequestBody UserEntity person) {
//    encoder.encode(person.getPassword());
    person.roles.add(roleRepo.findByName(ROLE_STUDENT));
    UserEntity save = userRepo.save(person);
    return save;
  }

  /**
   * Gibt den User mit der gesuchten ID zurück.
   * @param id User ID
   * @return angefragter User
   */
  @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER') or hasRole('ADMINISTRATOR')")
  @GetMapping("/users/{id}")
  public UserEntity getById(@PathVariable int id) {
    return userRepo.findById(id).get();
  }

  /**
   * Gibt den geänderten User zurück.
   * @param id User ID
   * @param person - Frontend Daten für den User
   * @return geänderter User
   */
  @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
  @PutMapping("/users/{id}")
  public UserEntity updateUser(@PathVariable int id, @RequestBody UserEntity person) {
//    encoder.encode(person.getPassword());

    person.setId(id);
    return userRepo.save(person);

//    return ResponseEntity.ok().body(userRepo.save(person));
  }


    /**
     * Gibt dem Repository den Auftrag, den User mit der entsprechenden ID zu löschen. Die geschieht nur, wenn der User
     * nicht mehr für ein Training freigegeben ist.
     * @param id User ID
     */
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @DeleteMapping("/users/{id}")
    public void deleteUser (@RequestHeader("Authorization") @PathVariable int id) {
      Optional<List<TrainingEntity>> trainingsFromUser = trainingRepo.findByStudentsAndIndividualTrue(userRepo.findById(id));
      if (trainingsFromUser.get().isEmpty()) {
        userRepo.deleteById(id);
      } else {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
          "Der User kann nicht gelöscht werden, da er noch für ein Training freigegeben ist.");
      }
    }

  /**
   * Gibt eine Liste aller bestehenden User des System zurück.
   * @return Liste aller User
   */
  @GetMapping("/users")
  public List<UserEntity> getAll() {
    return userRepo.findAll();
  }
}
