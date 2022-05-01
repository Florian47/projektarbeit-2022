package example.springbootdemoserver.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonController {
  @Autowired
  private PersonService personService;

  @PostMapping("/api/person")
  public void addPerson(@RequestBody PersonDto person) {
    personService.addPerson(person.getFirstName(), person.getLastName());
  }

  @GetMapping("/api/person/list")
  public List<PersonDto> persons() {
    return personService.list();
  }

  @GetMapping("/api/person/entities")
  public List<PersonEntity> personEntities() {
    return personService.entities();
  }
}
