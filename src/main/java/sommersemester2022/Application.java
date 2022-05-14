package sommersemester2022;

import sommersemester2022.person.UserEntity;
import sommersemester2022.person.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sommersemester2022.userroles.UserRole;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class Application {
  @Autowired
  private UserRepo userRepository;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @PostConstruct
  private void initDb() {
    UserEntity user = new UserEntity();
    user.setFirstName("admin");
    user.setLastName("admin");
    user.setUsername("admin");
    user.setPassword("admin1");
    user.setRole(UserRole.ADMIN);
    if(!userRepository.existsByUsername(user.getUsername()))userRepository.save(user);
  }
}

