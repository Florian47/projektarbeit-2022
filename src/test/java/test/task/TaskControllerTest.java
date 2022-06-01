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

public class TaskControllerTest extends BaseTest {

  @Test
  public void testAddTask() throws Exception {
    List<SolutionGaps> gapsList = new ArrayList<>();
    List<SolutionOptions> optionsList = new ArrayList<>();

    optionsList.add(new SolutionOptions("Montag", false));
    optionsList.add(new SolutionOptions("Dienstag", true));
    optionsList.add(new SolutionOptions("Mittwoch", true));
    optionsList.add(new SolutionOptions("Donnerstag", false));

    gapsList.add(new SolutionGaps(optionsList));
    String json = objectMapper.writeValueAsString(new TaskEntity("Aufgabe 1", "Heute ist ...", "1234", TaskCategory.GRAMMATIK, TaskDifficulty.EINFACH, new SolutionEntity(gapsList)));
    ResponseEntity<String> result = restAuthPost("/task/add", json,getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    List<TaskEntity> entities = loadAll(TaskEntity.class);
    assertThat(entities.size()).isEqualTo(1);
    TaskEntity pe = entities.get(0);
    //assertThat(pe.getId()).isGreaterThanOrEqualTo(1);
    //assertThat(pe.getScore()).isEqualTo(4);
    assertThat(pe.getPicture()).isEqualTo("1234");
    assertThat(pe.getSolution().getSolutionGaps().get(0).getSolutionOptions().get(0).getOptionName()).isEqualTo("Montag");
  }

  @Test
  public void testGetByIdTask() throws Exception {
    List<SolutionGaps> gapsList = new ArrayList<>();
    List<SolutionOptions> optionsList = new ArrayList<>();

    optionsList.add(new SolutionOptions("Montag", false));
    optionsList.add(new SolutionOptions("Dienstag", true));
    optionsList.add(new SolutionOptions("Mittwoch", true));
    optionsList.add(new SolutionOptions("Donnerstag", false));

    gapsList.add(new SolutionGaps(optionsList));
    String json = objectMapper.writeValueAsString(new TaskEntity("Aufgabe 1", "Heute ist ...", "1234", TaskCategory.GRAMMATIK, TaskDifficulty.EINFACH, new SolutionEntity(gapsList)));
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
    assertThat(pe.getPicture()).isEqualTo("1234");
  }

  @Test
  public void testUpdateTask() throws Exception {
    //TODO doesn't work
    List<SolutionGaps> gapsList = new ArrayList<>();
    List<SolutionOptions> optionsList = new ArrayList<>();

    optionsList.add(new SolutionOptions("Montag", false));
    optionsList.add(new SolutionOptions("Dienstag", true));
    optionsList.add(new SolutionOptions("Mittwoch", true));
    optionsList.add(new SolutionOptions("Donnerstag", false));

    gapsList.add(new SolutionGaps(optionsList));
    TaskEntity task = new TaskEntity("Aufgabe 1", "Heute ist ...", "1234", TaskCategory.GRAMMATIK, TaskDifficulty.EINFACH, new SolutionEntity(gapsList));
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
    assertThat(pe.getPicture()).isEqualTo("1234");
  }

  @Test
  public void testGetAllTasks() throws Exception {
    List<SolutionGaps> gapsList = new ArrayList<>();
    List<SolutionOptions> optionsList = new ArrayList<>();

    optionsList.add(new SolutionOptions("Montag", false));
    optionsList.add(new SolutionOptions("Dienstag", true));
    optionsList.add(new SolutionOptions("Mittwoch", true));
    optionsList.add(new SolutionOptions("Donnerstag", false));

    gapsList.add(new SolutionGaps(optionsList));
    TaskEntity task = new TaskEntity("Aufgabe 1", "Heute ist ...", "1234", TaskCategory.GRAMMATIK, TaskDifficulty.EINFACH, new SolutionEntity(gapsList));
    String json = objectMapper.writeValueAsString(task);
    ResponseEntity<String> result = restAuthPost("/task/add", json,getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    gapsList = new ArrayList<>();
    optionsList = new ArrayList<>();

    optionsList.add(new SolutionOptions("Montag", false));
    optionsList.add(new SolutionOptions("Dienstag", true));
    optionsList.add(new SolutionOptions("Mittwoch", true));
    optionsList.add(new SolutionOptions("Donnerstag", false));

    gapsList.add(new SolutionGaps(optionsList));
    task = new TaskEntity("Aufgabe 2", "Heute ist ...", "1234", TaskCategory.GRAMMATIK, TaskDifficulty.EINFACH, new SolutionEntity(gapsList));
    json = objectMapper.writeValueAsString(task);
    result = restAuthPost("/task/add", json,getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    List<TaskEntity> entities = loadAll(TaskEntity.class);
    task = entities.get(0);



    result = restAuthGet("/task",getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    List<TaskEntity> list = loadAll((TaskEntity.class));

    assertThat(list.size()).isEqualTo(2);
    assertThat(result.toString().contains("Aufgabe 2"));
    assertThat(result.toString().contains("Aufgabe 1"));


    TaskEntity pe = list.get(0);
    //assertThat(pe.getId()).isGreaterThanOrEqualTo(1);
    //assertThat(pe.getScore()).isEqualTo(4);
    assertThat(pe.getPicture()).isEqualTo("1234");
  }


  @Test
  public void testDeleteTask() throws Exception {
    List<SolutionGaps> gapsList = new ArrayList<>();
    List<SolutionOptions> optionsList = new ArrayList<>();

    optionsList.add(new SolutionOptions("Montag", false));
    optionsList.add(new SolutionOptions("Dienstag", true));
    optionsList.add(new SolutionOptions("Mittwoch", true));
    optionsList.add(new SolutionOptions("Donnerstag", false));

    gapsList.add(new SolutionGaps(optionsList));
    TaskEntity task = new TaskEntity("Aufgabe 1", "Heute ist ...", "1234", TaskCategory.GRAMMATIK, TaskDifficulty.EINFACH, new SolutionEntity(gapsList));
    String json = objectMapper.writeValueAsString(task);
    ResponseEntity<String> result = restAuthPost("/task/add", json,getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    List<TaskEntity> entities = loadAll(TaskEntity.class);
    task = entities.get(0);
    int id =task.getId();

    ResponseEntity<String> result1 = restAuthDel("/task/"+id,getJWTToken("admin"));
    assertThat(result1.getStatusCode()).isEqualTo(HttpStatus.OK);

    List<TaskEntity> list = loadAll((TaskEntity.class));
    assertThat(list.size()).isEqualTo(0);
    ;
  }

}

