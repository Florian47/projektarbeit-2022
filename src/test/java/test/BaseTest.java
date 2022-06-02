package test;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import sommersemester2022.Application;
import sommersemester2022.person.UserEntity;
import sommersemester2022.person.UserRepo;
import sommersemester2022.processedTraining.ProcessedTrainingEntity;
import sommersemester2022.processedTraining.ProcessedTrainingRepo;
import sommersemester2022.security.services.jwt.JwtUtils;
import sommersemester2022.solution.SolutionEntity;
import sommersemester2022.solution.SolutionGaps;
import sommersemester2022.solution.SolutionOptions;
import sommersemester2022.solution.SolutionRepo;
import sommersemester2022.task.TaskEntity;
import sommersemester2022.task.TaskRepo;
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
  protected SolutionRepo solutionRepo;
  @Autowired
  protected TaskRepo taskRepo;
  @Autowired
  protected UserRepo userRepo;
  @Autowired
  protected TrainingRepo trainingRepo;
  @Autowired
  protected ProcessedTrainingRepo processedTrainingRepo;
  @Autowired
  protected RoleRepo roleRepository;
  protected UserEntity testUser;
  protected TaskEntity task;

  @BeforeEach
  public void setup() {
    if (userRepo != null){userRepo.deleteAll();}
    //if (solutionRepo != null){solutionRepo.deleteAll();}
    if (taskRepo != null){taskRepo.deleteAll();}
    //if (trainingRepo != null){trainingRepo.deleteAll();}
    //if (processedTrainingRepo != null){processedTrainingRepo.deleteAll();}

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
    this.testUser = userRepo.save(user);
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
