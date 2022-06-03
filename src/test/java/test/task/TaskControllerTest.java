package test.task;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import sommersemester2022.person.UserEntity;
import sommersemester2022.solution.SolutionEntity;
import sommersemester2022.solution.SolutionGaps;
import sommersemester2022.solution.SolutionOptions;
import sommersemester2022.task.TaskCategory;
import sommersemester2022.task.TaskDifficulty;
import sommersemester2022.task.TaskEntity;
import sommersemester2022.userroles.UserRole;
import test.BaseTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
/**
 * @author Alexander Kiehl
 * In dieser Klasse werden die CRUD Operationen des TaskControllers getestet.
 */
public class TaskControllerTest extends BaseTest {

  /**
   * Erstellt einen Task
   * @throws Exception
   */
  @Test
  public void testAddTask() throws Exception {

    String json = objectMapper.writeValueAsString(task);
    ResponseEntity<String> result = restAuthPost("/task/add", json,getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    List<TaskEntity> entities = loadAll(TaskEntity.class);
    assertThat(entities.size()).isEqualTo(1);
    TaskEntity pe = entities.get(0);
    //assertThat(pe.getId()).isGreaterThanOrEqualTo(1);
    //assertThat(pe.getScore()).isEqualTo(4);
    assertThat(pe.getPicture()).isEqualTo(null);
    assertThat(pe.getSolution().getSolutionGaps().get(0).getSolutionOptions().get(0).getOptionName()).isEqualTo("Montag");
  }
  /**
   * Holt einen Task über die id aus der Datenbank
   * @throws Exception
   */
  @Test
  public void testGetByIdTask() throws Exception {
    String json = objectMapper.writeValueAsString(task);
    ResponseEntity<String> result = restAuthPost("/task/add", json,getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    List<TaskEntity> entities = loadAll(TaskEntity.class);
    TaskEntity task = entities.get(0);
    int id = task.getId();

    result = restAuthGet("/task/"+id,getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    List<TaskEntity> list = loadAll((TaskEntity.class));

    assertThat(list.size()).isEqualTo(1);
    assertThat(result.toString().contains("Aufgabe 1"));

    entities = loadAll(TaskEntity.class);
    assertThat(entities.size()).isEqualTo(1);
    TaskEntity pe = entities.get(0);
    //assertThat(pe.getId()).isGreaterThanOrEqualTo(1);
    //assertThat(pe.getScore()).isEqualTo(4);
    assertThat(pe.getPicture()).isEqualTo(null);
  }

  /**
   * Ändert einen Task über die id
   * @throws Exception
   */
  @Test
  public void testUpdateTask() throws Exception {
    String json = objectMapper.writeValueAsString(task);
    ResponseEntity<String> result = restAuthPost("/task/add", json,getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    List<TaskEntity> entities = loadAll(TaskEntity.class);
    task = entities.get(0);
    int id = task.getId();
    task.setName("Aufgabe 2");


    result = restAuthPut("/task/edit/"+id,json,getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    List<TaskEntity> list = loadAll((TaskEntity.class));

    assertThat(list.size()).isEqualTo(1);
    assertThat(result.toString().contains("Aufgabe 2"));

    entities = loadAll(TaskEntity.class);
    assertThat(entities.size()).isEqualTo(1);
    TaskEntity pe = entities.get(0);
    //assertThat(pe.getId()).isGreaterThanOrEqualTo(1);
    //assertThat(pe.getScore()).isEqualTo(4);
    assertThat(pe.getPicture()).isEqualTo(null);
  }

  /**
   * Holt alle Tasks aus der Datenbank
   * @throws Exception
   */
  @Test
  public void testGetAllTasks() throws Exception {
    String json = objectMapper.writeValueAsString(task);
    ResponseEntity<String> result = restAuthPost("/task/add", json,getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    task.setName("Aufgabe 2");
    task.setId(70);
    json = objectMapper.writeValueAsString(task);
    result = restAuthPost("/task/add", json,getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    result = restAuthGet("/task",getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    List<TaskEntity> list = loadAll((TaskEntity.class));

    assertThat(list.size()).isEqualTo(2);
    assertThat(result.toString().contains("Aufgabe 2"));
    assertThat(result.toString().contains("Aufgabe 1"));
    TaskEntity pe = list.get(0);
    assertThat(pe.getPicture()).isEqualTo(null);
  }

  /**
   * Löscht einen Task über die id
   * @throws Exception
   */
  @Test
  public void testDeleteTask() throws Exception {
    task = new TaskEntity();
    String json = objectMapper.writeValueAsString(task);
    ResponseEntity<String> result = restAuthPost("/task/add", json,getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    List<TaskEntity> entities = loadAll(TaskEntity.class);
    List<TaskEntity> list = loadAll((TaskEntity.class));
    assertThat(list.size()).isEqualTo(2);
    task = entities.get(1);
    int id =task.getId();

    ResponseEntity<String> result1 = restAuthDel("/task/"+id,getJWTToken("admin"));
    assertThat(result1.getStatusCode()).isEqualTo(HttpStatus.OK);

    list = loadAll((TaskEntity.class));
    assertThat(list.size()).isEqualTo(1);

  }

}

