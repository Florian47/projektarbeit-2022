package test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import sommersemester2022.Application;
import sommersemester2022.person.UserEntity;
import sommersemester2022.person.UserRepo;
import sommersemester2022.security.services.jwt.JwtUtils;
import sommersemester2022.solution.SolutionEntity;
import sommersemester2022.solution.SolutionGaps;
import sommersemester2022.solution.SolutionOptions;
import sommersemester2022.task.TaskCategory;
import sommersemester2022.task.TaskDifficulty;
import sommersemester2022.task.TaskEntity;
import sommersemester2022.training.TrainingEntity;
import sommersemester2022.training.TrainingRepo;
import sommersemester2022.userroles.RoleEntity;
import sommersemester2022.userroles.RoleRepo;
import sommersemester2022.userroles.UserRole;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static sommersemester2022.userroles.UserRole.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
  classes = Application.class
)
public class BaseTest {
  @Autowired
  protected TestRestTemplate restTemplate;
  @Autowired
  protected ObjectMapper objectMapper;
  @Autowired
  protected PlatformTransactionManager tx;
  @Autowired
  protected EntityManager em;
  @Autowired
  protected JwtUtils jwtUtils;

  @Autowired
  protected UserRepo userRepo;

  @Autowired
  protected RoleRepo roleRepository;
  @Autowired
  protected TrainingRepo trainingRepo;

  protected UserEntity admin;
  protected TrainingEntity training;

  @BeforeEach
  public void setup() {
    userRepo.deleteAll();

    RoleEntity student = new RoleEntity();
    student.setName(ROLE_STUDENT);
    if(!roleRepository.existsByName(ROLE_STUDENT)) roleRepository.save(student);
    RoleEntity teacher = new RoleEntity();
    teacher.setName(UserRole.ROLE_TEACHER);
    if(!roleRepository.existsByName(UserRole.ROLE_TEACHER)) roleRepository.save(teacher);
    RoleEntity admin = new RoleEntity();
    admin.setName(ROLE_ADMINISTRATOR);
    if(!roleRepository.existsByName(UserRole.ROLE_ADMINISTRATOR)) roleRepository.save(admin);

    UserEntity user = new UserEntity();
    user.setFirstName("admin");
    user.setLastName("admin");
    user.setUsername("admin");
    user.setPassword("admin1");
    user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12)));
    user.roles.add(roleRepository.findByName(ROLE_ADMINISTRATOR));
    user.roles.add(roleRepository.findByName(ROLE_TEACHER));
    user.roles.add(roleRepository.findByName(ROLE_STUDENT));
    this.admin = userRepo.save(user);


    List<SolutionGaps> gapsList = new ArrayList<>();
    List<SolutionOptions> optionsList = new ArrayList<>();
    optionsList.add(new SolutionOptions("Montag", false));
    optionsList.add(new SolutionOptions("Dienstag", true));
    optionsList.add(new SolutionOptions("Mittwoch", true));
    optionsList.add(new SolutionOptions("Donnerstag", false));
    gapsList.add(new SolutionGaps(optionsList));
    SolutionEntity solution1 = new SolutionEntity(gapsList, "Lese dir den Satz in gedanken einmal laut vor");
    TaskEntity task1 = new TaskEntity();
    task1.setName("Task1");
    task1.setText("Welcher Tag ist heute?");
    task1.setSolution(solution1);
    task1.setCategory(TaskCategory.LUECKENTEXT);
    task1.setDifficulty(TaskDifficulty.EINFACH);
    List<UserEntity> userList = new ArrayList<>();
    userList.add(this.admin);
    List<TaskEntity> taskList = new ArrayList<>();
    taskList.add(task1);
    this.training = new TrainingEntity();
    this.training.setName("Test1");
    this.training.setTasks(taskList);
    this.training.setIndividual(true);
    this.training.setStudents(userList);
    this.training = trainingRepo.save(training);
  }


  protected String getJWTToken(String user) {
    return jwtUtils.generateJwtToken(user);
  }

  protected ResponseEntity<String> restPost(String url, String json) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return restTemplate.postForEntity(url, new HttpEntity<>(json, headers), String.class);
  }

  protected ResponseEntity<String> restAuthPost(String url, String json, String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);
    return restTemplate.postForEntity(url, new HttpEntity<>(json, headers), String.class);
  }

  protected ResponseEntity<String> restGet(String url) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
  }

  protected ResponseEntity<String> restAuthGet(String url, String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);
    return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
  }

  protected ResponseEntity<String> restDel(String url) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(headers), String.class);
  }

  protected ResponseEntity<String> restAuthDel(String url, String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);
    return restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(headers), String.class);
  }

  protected ResponseEntity<String> restPut(String url, String json) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(json,headers), String.class);
  }

  protected ResponseEntity<String> restAuthPut(String url, String json, String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);
    return restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(json,headers), String.class);
  }

  protected <T> void removeAll(Class<T> type) {
    loadAll(type).forEach(e -> em.remove(e));
  }

  protected <T> List<T> loadAll(Class<T> type) {
    return em.createQuery("select e from " + type.getSimpleName() + " e", type).getResultList();
  }

}
