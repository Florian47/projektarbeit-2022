package sommersemester2022.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
public class UserController {
  @Autowired
  private UserRepo userRepo;

  @PostMapping("/users/authenticate")
  public UserEntity authenticate(@RequestBody UserEntity person) {
    return userRepo.findByUsernameAndPassword(person.getUsername(), person.getPassword()).orElseThrow(() ->
      new EntityNotFoundException("Username and password does not match a user"));
  }
//("/userloesung/aufgabe{id}/luecke{id}/)
  @PostMapping("/users/register")
  public UserEntity register(@RequestBody UserEntity person) {
    return userRepo.save(person);
  }

  @GetMapping("/users/{id}")
  public UserEntity getById(@PathVariable int id) {
    return userRepo.findById(id).get();
  }

  @PutMapping("/users/{id}")
  public UserEntity updateUser(@PathVariable int id, @RequestBody UserEntity person) {
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
