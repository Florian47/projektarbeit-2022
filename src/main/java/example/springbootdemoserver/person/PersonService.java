package example.springbootdemoserver.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class PersonService {
  @Autowired
  private PersonRepo personRepo;

  public void addPerson(String firstName, String lastName) {
    personRepo.save(new PersonEntity(firstName, lastName));
  }

  public List<PersonDto> list() {
    return personRepo.findAll().stream().map(e -> new PersonDto(e.getFirstName(), e.getLastName())).toList();
  }

  public List<PersonEntity> entities() {
    return personRepo.findAll();
  }
}
