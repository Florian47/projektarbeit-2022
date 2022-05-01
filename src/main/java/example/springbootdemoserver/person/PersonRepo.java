package example.springbootdemoserver.person;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepo extends JpaRepository<PersonEntity, Long> {
  List<PersonEntity> findByFirstName(String name);
}
