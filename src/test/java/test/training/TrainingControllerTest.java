package test.training;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sommersemester2022.solution.SolutionEntity;
import sommersemester2022.solution.SolutionGaps;
import sommersemester2022.solution.SolutionOptions;
import sommersemester2022.task.TaskEntity;
import sommersemester2022.training.TrainingEntity;
import test.BaseTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TrainingControllerTest extends BaseTest {

  static TrainingEntity training;
  static SolutionEntity solution;
  @BeforeAll
  static void generateDummys(){
    List<SolutionOptions> optionsList = new ArrayList<>();
    List<SolutionOptions> optionsList2 = new ArrayList<>();

    optionsList.add(new SolutionOptions("Montag", false));
    optionsList.add(new SolutionOptions("Dienstag", true));
    optionsList.add(new SolutionOptions("Mittwoch", true));
    optionsList.add(new SolutionOptions("Donnerstag", false));

    optionsList2.add(new SolutionOptions("Montag", false));
    optionsList2.add(new SolutionOptions("Dienstag", true));
    optionsList2.add(new SolutionOptions("Mittwoch", true));
    optionsList2.add(new SolutionOptions("Donnerstag", false));

    List<SolutionGaps> gapsList = new ArrayList<>();
    gapsList.add(new SolutionGaps(optionsList));
    gapsList.add(new SolutionGaps(optionsList2));

    solution = new SolutionEntity();
    solution.setSolutionGaps(gapsList);

    List<TaskEntity> tasks = new ArrayList<>();
    TaskEntity taskEntity = new TaskEntity();
    taskEntity.setName("TrainingsTask");
    taskEntity.setSolution(solution);
    tasks.add(taskEntity);

    training = new TrainingEntity();
    training.setTasks(tasks);
  }



  @Test
  public void testAddTraining() throws Exception {
    String json = objectMapper.writeValueAsString(training);

    ResponseEntity<String> result = restPost("/training/add", json);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    List<TrainingEntity> entities = loadAll(TrainingEntity.class);
    assertThat(entities.size()).isEqualTo(1);
    TrainingEntity pe = entities.get(0);
    assertThat(pe).isNotNull();
  }
}
