package test.training;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sommersemester2022.person.UserEntity;
import sommersemester2022.solution.SolutionEntity;
import sommersemester2022.solution.SolutionGaps;
import sommersemester2022.solution.SolutionOptions;
import sommersemester2022.task.TaskCategory;
import sommersemester2022.task.TaskDifficulty;
import sommersemester2022.task.TaskEntity;
import sommersemester2022.task.TaskRepo;
import sommersemester2022.training.TrainingEntity;
import sommersemester2022.training.TrainingRepo;
import test.BaseTest;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author David Wiebe
 * In dieser Klasse werden die CRUD Operationen des TrainingControllers getestet.
 */
public class TrainingControllerTest extends BaseTest {

  @Autowired
  TaskRepo taskRepo;
  @Autowired
  TrainingRepo trainingRepo;


  @Test
  public void testAddTraining() throws Exception {

    String json = objectMapper.writeValueAsString(training);
    ResponseEntity<String> result = restAuthPost("/training/add", json, getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    List<TrainingEntity> entities = loadAll(TrainingEntity.class);
    assertThat(entities.size()).isEqualTo(1);
    TrainingEntity pe = entities.get(0);
    assertThat(pe).isNotNull();
    TrainingEntity te = entities.get(0);
    te.toString();
    assertThat(te.isIndividual()).isEqualTo(true);


  }
  @Test
  public void testGetById() throws Exception {
    ResponseEntity<String> result = restAuthGet("/training/" + training.getId(), getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    List<TrainingEntity> list = loadAll((TrainingEntity.class));

    assertThat(list.size()).isEqualTo(1);

    trainingRepo.deleteAll();
  }

  @Test
  public void testGetAll() throws Exception {
    ResponseEntity<String> result = restGet("/training");
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    List<TrainingEntity> list = objectMapper.readValue(result.getBody(), new TypeReference<>() {
    });
    assertThat(list.size()).isEqualTo(1);
    assertThat(list.get(0).getName().equals("Test1"));

  }

  @Test
  public void testGetAllManuelTrainings() throws Exception {
    ResponseEntity<String> result = restGet("/training/individuell");
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    List<TrainingEntity> list = objectMapper.readValue(result.getBody(), new TypeReference<>() {
    });
    assertThat(list.size()).isEqualTo(1);
  }

  @Test
  public void testDeleteTraining() throws Exception{
    ResponseEntity<String> result = restAuthDel("/training/" + training.getId(), getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    List<TrainingEntity> list = loadAll((TrainingEntity.class));
    assertThat(list.size()).isEqualTo(0);
  }

  @Test
  public void testAllTrainingsForStudent()throws Exception {

    ResponseEntity<String> result = restGet("/training/schueler/" + admin.getId());
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    List<TrainingEntity> list = loadAll((TrainingEntity.class));
    assertThat(list.size()).isEqualTo(1);
  }

  @Test
  public void testAddTasksToTraining() throws Exception {
    List<SolutionGaps> gapsList1 = new ArrayList<>();
    List<SolutionOptions> optionsList1 = new ArrayList<>();
    optionsList1.add(new SolutionOptions("Schlecht", false));
    optionsList1.add(new SolutionOptions("Müde", true));
    optionsList1.add(new SolutionOptions("gut", true));
    optionsList1.add(new SolutionOptions("Herforragend", false));
    gapsList1.add(new SolutionGaps(optionsList1));
    SolutionEntity solution1 = new SolutionEntity(gapsList1, "Lese dir den Satz in gedanken einmal laut vor");
    TaskEntity task2 = new TaskEntity();
    task2.setName("Task2");
    task2.setText("Wie geht es dir?");
    task2.setSolution(solution1);
    task2.setCategory(TaskCategory.LUECKENTEXT);
    task2.setDifficulty(TaskDifficulty.EINFACH);
    training.addTask(task2);
    String json = objectMapper.writeValueAsString(training);
    ResponseEntity<String> result = restAuthPost("/training/add", json, getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    List<TaskEntity> entities = loadAll(TaskEntity.class);
    assertThat(entities.size()).isEqualTo(2);
    TaskEntity pe = entities.get(0);
    assertThat(pe).isNotNull();
    }

  @Test
  public void testEditTraining()throws Exception {

    String json = objectMapper.writeValueAsString(training);
    ResponseEntity<String> result = restAuthGet("/training/" + training.getId(), getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    training.setId(500);
    json = objectMapper.writeValueAsString(training);
    result = restAuthPost("/training/edit/", json, getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);

    List<TrainingEntity> entities = loadAll(TrainingEntity.class);
    assertThat(entities.size()).isEqualTo(1);
    TrainingEntity pe = entities.get(0);
    assertThat(pe).hasFieldOrPropertyWithValue("id",9);
  }
}
