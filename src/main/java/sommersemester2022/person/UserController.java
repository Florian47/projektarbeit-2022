package sommersemester2022.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sommersemester2022.payload.response.JwtResponse;
import sommersemester2022.security.services.UserDetailsImpl;
import sommersemester2022.security.services.jwt.JwtUtils;


import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {
  @Autowired
  private UserRepo userRepo;

  @Autowired
  PasswordEncoder encoder;
  @Autowired
  AuthenticationManager authenticationManager;
  @Autowired
  JwtUtils jwtUtils;

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
    return ResponseEntity.ok().header("Authorization", jwtUtils.generateJwtToken(authentication)).body(person);
//    return userRepo.findByUsernameAndPassword(person.getUsername(), person.getPassword()).orElseThrow(() ->
//      new EntityNotFoundException("Username and password does not match a user"));
  }
//("/userloesung/aufgabe{id}/luecke{id}/)
  @PostMapping("/users/register")
  public UserEntity register(@RequestBody UserEntity person) {
    encoder.encode(person.getPassword());
    return userRepo.save(person);
  }

  @GetMapping("/users/{id}")
  public UserEntity getById(@PathVariable int id) {
    return userRepo.findById(id).get();
  }

  @PutMapping("/users/{id}")
  public UserEntity updateUser(@PathVariable Long id, @RequestBody UserEntity person) {
    person.setId(id);
    return userRepo.save(person);
  }

  @DeleteMapping("/users/{id}")
  public void deleteUser(@PathVariable int id) {
    userRepo.deleteById(id);
  }

  @GetMapping("/users")
  public List<UserEntity> getAll() {
    return userRepo.findAll();
  }

}
