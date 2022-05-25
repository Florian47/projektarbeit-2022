package sommersemester2022;

import org.springframework.security.crypto.bcrypt.BCrypt;
import sommersemester2022.person.UserEntity;
import sommersemester2022.person.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sommersemester2022.solution.SolutionEntity;
import sommersemester2022.solution.SolutionGaps;
import sommersemester2022.solution.SolutionOptions;
import sommersemester2022.task.TaskCategory;
import sommersemester2022.task.TaskDifficulty;
import sommersemester2022.task.TaskEntity;
import sommersemester2022.training.TrainingEntity;
import sommersemester2022.userroles.RoleEntity;
import sommersemester2022.userroles.RoleRepo;
import sommersemester2022.task.TaskRepo;
import sommersemester2022.training.TrainingRepo;
import sommersemester2022.userroles.UserRole;

import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;

import static sommersemester2022.userroles.UserRole.*;

@SpringBootApplication
public class Application {
  @Autowired
  private UserRepo userRepository;
  @Autowired
  private TaskRepo taskRepository;
  @Autowired
  private TrainingRepo trainingRepository;
  @Autowired
  private RoleRepo roleRepository;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @PostConstruct
  private void initDb() {
    RoleEntity student = new RoleEntity();
    student.setName(ROLE_STUDENT);
    if(!roleRepository.existsByName(ROLE_STUDENT)) roleRepository.save(student);
    RoleEntity teacher = new RoleEntity();
    teacher.setName(UserRole.ROLE_TEACHER);
    if(!roleRepository.existsByName(UserRole.ROLE_TEACHER)) roleRepository.save(teacher);
    RoleEntity admin = new RoleEntity();
    admin.setName(ROLE_ADMINISTRATOR);
    if(!roleRepository.existsByName(UserRole.ROLE_ADMINISTRATOR)) roleRepository.save(admin);

    // Add admin user
    UserEntity user = new UserEntity();
    user.setFirstName("admin");
    user.setLastName("admin");
    user.setUsername("admin");
    user.setPassword("admin1");
    user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12)));
    user.roles.add(roleRepository.findByName(ROLE_ADMINISTRATOR));
    user.roles.add(roleRepository.findByName(ROLE_TEACHER));
    user.roles.add(roleRepository.findByName(ROLE_STUDENT));
    if(!userRepository.existsByUsername(user.getUsername()))userRepository.save(user);

//     Add 3 test users
    UserEntity user1 = new UserEntity();
    user1.setFirstName("Chris");
    user1.setLastName("Brinkhoff");
    user1.setUsername("cbrinkhoff");
    user1.setPassword("chrisbrinkhoff");
    user1.setPassword(BCrypt.hashpw(user1.getPassword(), BCrypt.gensalt(12)));
    user1.roles.add(roleRepository.findByName(ROLE_STUDENT));

    if(!userRepository.existsByUsername(user1.getUsername()))userRepository.save(user1);

    UserEntity user2 = new UserEntity();
    user2.setFirstName("Florian");
    user2.setLastName("Weinert");
    user2.setUsername("fweinert");
    user2.setPassword("florianweinert");
    user2.setPassword(BCrypt.hashpw(user2.getPassword(), BCrypt.gensalt(12)));
    user2.roles.add(roleRepository.findByName(ROLE_STUDENT));

    if(!userRepository.existsByUsername(user2.getUsername()))userRepository.save(user2);

    UserEntity user3 = new UserEntity();
    user3.setFirstName("Alexander");
    user3.setLastName("Kiehl");
    user3.setUsername("akiehl");
    user3.setPassword("alexanderkiehl");
    user3.setPassword(BCrypt.hashpw(user3.getPassword(), BCrypt.gensalt(12)));
    user3.roles.add(roleRepository.findByName(ROLE_STUDENT));

    if(!userRepository.existsByUsername(user3.getUsername()))userRepository.save(user3);
/**
    //// Tasks with solutions (without pictures)
    // Task 1 - EINFACH, GRAMMATIK
    List<SolutionOptions> options1 = new ArrayList<>();
    options1.add(new SolutionOptions("schneller", true));
    options1.add(new SolutionOptions("schnella", false));
    options1.add(new SolutionOptions("schnellt", false));

    List<SolutionGaps> gaps1 = new ArrayList<>();
    gaps1.add(new SolutionGaps(options1));
    SolutionEntity solution1 = new SolutionEntity(gaps1, "Denk an letzte Stunde");

    TaskEntity task1 = new TaskEntity();
    task1.setName("Aufgabe 1 - Grammatik");
    task1.setText("Was ist die Steigerung von schnell?");
    task1.setCategory(TaskCategory.GRAMMATIK);
    task1.setDifficulty(TaskDifficulty.EINFACH);
    task1.setSolution(solution1);
    taskRepository.save(task1);

    // Task 2 - EINFACH, GRAMMATIK
    List<SolutionOptions> options2 = new ArrayList<>();
    options2.add(new SolutionOptions("guter", false));
    options2.add(new SolutionOptions("bessa", false));
    options2.add(new SolutionOptions("besser", true));

    List<SolutionGaps> gaps2 = new ArrayList<>();
    gaps2.add(new SolutionGaps(options2));
    SolutionEntity solution2 = new SolutionEntity(gaps2, "Denk an letzte Stunde");

    TaskEntity task2 = new TaskEntity();
    task2.setName("Aufgabe 2 - Grammatik");
    task2.setText("Was ist die Steigerung von gut?");
    task2.setCategory(TaskCategory.GRAMMATIK);
    task2.setDifficulty(TaskDifficulty.EINFACH);
    task2.setSolution(solution2);
    taskRepository.save(task2);

    // Task 3 - MITTEL, LÜCKENTEXT
    List<SolutionOptions> options3 = new ArrayList<>();
    options3.add(new SolutionOptions("Mittwoch", false));
    options3.add(new SolutionOptions("Montag", false));
    options3.add(new SolutionOptions("Donnerstag", true));

    List<SolutionGaps> gaps3 = new ArrayList<>();
    gaps3.add(new SolutionGaps(options3));
    SolutionEntity solution3 = new SolutionEntity(gaps3, "Guck notfalls in den Kalender");

    TaskEntity task3 = new TaskEntity();
    task3.setName("Aufgabe 3 - Lückentext");
    task3.setText("Heute ist ...?");
    task3.setCategory(TaskCategory.LUECKENTEXT);
    task3.setDifficulty(TaskDifficulty.EINFACH);
    task3.setSolution(solution3);
    taskRepository.save(task3);

    // Training 1
    List<TaskEntity> forTraining1 = new ArrayList<>();
    forTraining1.add(task1);
    forTraining1.add(task2);
    TrainingEntity training1 = new TrainingEntity();
    training1.setName("Training 1");
    training1.setIndividual(false);
    training1.setTasks(forTraining1);
    trainingRepository.save(training1);

    // Training 2
    List<TaskEntity> forTraining2 = new ArrayList<>();
    forTraining2.add(task2);
    forTraining2.add(task3);
    TrainingEntity training2 = new TrainingEntity();
    training2.setName("Training 2 - Für Florian Weinert");
    training2.setIndividual(true);
    training2.setTasks(forTraining2);
    training2.addStudent(userRepository.findByUsername("fweinert").get());
    trainingRepository.save(training2);
    **/
  }

}

