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

/**
 * Mainklasse die den Server startet
 */
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

  /**
   * Mainmethode die den Server startet
   * @param args Java command line arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);}
}

