package test.processedSolution;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sommersemester2022.processedTraining.ProcessedSolutionGaps;
import sommersemester2022.processedTraining.ProcessedSolutionTasks;
import sommersemester2022.processedTraining.ProcessedTrainingEntity;
import sommersemester2022.solution.SolutionEntity;
import sommersemester2022.solution.SolutionGaps;
import sommersemester2022.solution.SolutionOptions;
import sommersemester2022.task.TaskEntity;
import sommersemester2022.training.TrainingEntity;
import test.BaseTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProcessedSolutionControllerTest extends BaseTest {

  @Test
  //Musterlösung vom Lehrer
  public void testJSONSolution() throws Exception {
    SolutionEntity solution = new SolutionEntity();
    List<SolutionGaps> gapsList = new ArrayList<>();
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

    gapsList.add(new SolutionGaps(optionsList));
    gapsList.add(new SolutionGaps(optionsList2));

    solution.setSolutionGaps(gapsList);

    List<TaskEntity> tasks = new ArrayList<>();
    TaskEntity taskEntity = new TaskEntity();
    taskEntity.setName("TrainingsTask");
    taskEntity.setSolution(solution);
    tasks.add(taskEntity);

    TrainingEntity trainingEntity = new TrainingEntity();
    trainingEntity.setTasks(tasks);
    String json = objectMapper.writeValueAsString(trainingEntity);
    ResponseEntity<String> result = restPost("/training/add", json);
    List<TrainingEntity> entities = loadAll(TrainingEntity.class);
    trainingEntity = entities.get(0);
    //Lösung vom Schüler
    ProcessedTrainingEntity processedTraining = new ProcessedTrainingEntity();
    processedTraining.setProcessedSolutionTasks(tasks);
    processedTraining.setOriginTraining(trainingEntity);
    processedTraining.getProcessedSolutionTasks();

    json = objectMapper.writeValueAsString(processedTraining);
    result = restPost("/evaluate/ProcessedTraining", json);


    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    List<ProcessedTrainingEntity> processedTrainingEntityList = loadAll(ProcessedTrainingEntity.class);
    assertThat(entities.size()).isEqualTo(1);
    ProcessedTrainingEntity pe = processedTrainingEntityList.get(0);
    List<TrainingEntity> trainingEntityList = loadAll(TrainingEntity.class);
    TrainingEntity te = trainingEntityList.get(0);
    //assertThat(pe.getId()).isGreaterThanOrEqualTo(1);
    //assertThat(pe.getSolutionGaps().get(0).getSolutionOptions().get(0).getOptionName()).isEqualTo("Montag");
    assertThat(te.getScore()).isEqualTo(2);
    assertThat(pe.getScore()).isEqualTo(1);
  }
}
