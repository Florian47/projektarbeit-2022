package sommersemester2022.person;

import org.springframework.beans.factory.annotation.Autowired;
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
import sommersemester2022.security.services.jwt.JwtUtils;
import sommersemester2022.userroles.RoleRepo;
import java.util.List;

import static sommersemester2022.userroles.UserRole.ROLE_STUDENT;

/**
 * UserController is the control class for the user entity. It handles all activities that can be done with users and
 * gives information to the repository which saves the activities.
 * (e.g. all CRUD-Operations)
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
  PasswordEncoder encoder;
  @Autowired
  AuthenticationManager authenticationManager;
  @Autowired
  JwtUtils jwtUtils;

  /**
   * Authenticate a user to check his abilities.
   * @param person - frontend data for user
   * @return ResponseEntity
   */
  @PostMapping("/users/authenticate")
  public ResponseEntity authenticate(@RequestBody UserEntity person) {
    Authentication authentication = authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(person.getUsername(), person.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);

//    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//    List<String> roles = userDetails.getAuthorities().stream()
//      .map(item -> item.getAuthority())
//      .collect(Collectors.toList());
    return ResponseEntity.ok().header("Authorization", jwtUtils.generateJwtToken(authentication)).body(userRepo.findByUsername(person.getUsername()).get());
//    return userRepo.findByUsernameAndPassword(person.getUsername(), person.getPassword()).orElseThrow(() ->
//      new EntityNotFoundException("Username and password does not match a user"));
  }
//("/userloesung/aufgabe{id}/luecke{id}/)

  /**
   * Registers a new user in the system.
   * @param person - frontend data for user
   * @return saved user entity
   */
  //@PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER') or hasRole('ADMINISTRATOR')")
  @PostMapping("/users/register")
  public UserEntity register(@RequestBody UserEntity person) {
    encoder.encode(person.getPassword());

    person.roles.add(roleRepo.findByName(ROLE_STUDENT));
    return userRepo.save(person);
  }

  /**
   * Returns the user found by the given id..
   * @param id - user id
   * @return asked user
   */
//  @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER') or hasRole('ADMINISTRATOR')")
  @GetMapping("/users/{id}")
  public UserEntity getById(@PathVariable int id) {
    return userRepo.findById(id).get();
  }

  /**
   * Returns the updated user.
   * @param id - user id
   * @param person - frontend data for user
   * @return updated user
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
   * Tells the repository to delete the user with the given id.
   * @param id - user id
   */
  @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
  @DeleteMapping("/users/{id}")
  public void deleteUser( @RequestHeader ("Authorization")@PathVariable int id) {
    userRepo.deleteById(id);
  }

  /**
   * Returns a list of all users existing in the database.
   * @return list of asked users
   */
  @GetMapping("/users")
  public List<UserEntity> getAll() {
    return userRepo.findAll();
  }
}
