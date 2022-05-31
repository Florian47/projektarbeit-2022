package test.processedSolution;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);



    //Lösung vom Schüler
    ProcessedTrainingEntity sSolution = new ProcessedTrainingEntity();
    List<TaskEntity> sTaskList = new ArrayList<>();
    List<SolutionEntity> sSolutionEntities = new ArrayList<>();
    List<SolutionGaps> sGapsList = new ArrayList<>();
    List<SolutionOptions>sOptionsList = new ArrayList<>();
    List<SolutionOptions>sOptionsList2= new ArrayList<>();

    sOptionsList.add(new SolutionOptions("Montag", false));
    sOptionsList.add(new SolutionOptions("Dienstag", true));
    sOptionsList.add(new SolutionOptions("Mittwoch", true));
    sOptionsList.add(new SolutionOptions("Donnerstag", false));

    sOptionsList2.add(new SolutionOptions("Montag", false));
    sOptionsList2.add(new SolutionOptions("Dienstag", false));
    sOptionsList2.add(new SolutionOptions("Mittwoch", true));
    sOptionsList2.add(new SolutionOptions("Donnerstag", false));

    sGapsList.add(new SolutionGaps(sOptionsList));
    sGapsList.add(new SolutionGaps(sOptionsList2));

    sSolutionEntities.add(new SolutionEntity(sGapsList));
    sSolution.setProcessedSolutionTasks(sTaskList);

    List<TaskEntity> sTasks = new ArrayList<>();
    TaskEntity sTaskEntity = new TaskEntity();
    sTaskEntity.setName("TrainingsTask");
    sSolution.setOriginTraining(trainingEntity);
    tasks.add(sTaskEntity);
    List<TrainingEntity> tEntities = loadAll(TrainingEntity.class);
    trainingEntity = tEntities.get(0);

    json =objectMapper.writeValueAsString(trainingEntity);
    result = restPost("/generateProcessedTraining", json);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);


    List<ProcessedTrainingEntity> pEntities = loadAll(ProcessedTrainingEntity.class);
     ProcessedTrainingEntity processedTraining = pEntities.get(0);
    //processedTraining.setProcessedSolutionTasks(tasks);
    //processedTraining.setOriginTraining(trainingEntity);
    processedTraining.getProcessedSolutionTasks();

    json = objectMapper.writeValueAsString(processedTraining);
    result = restPost("/evaluate/ProcessedTraining", json);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    //List<ProcessedTrainingEntity> processedTrainingEntityList = loadAll(ProcessedTrainingEntity.class);
    assertThat(tEntities.size()).isEqualTo(1);
    //ProcessedTrainingEntity pe = processedTrainingEntityList.get(0);
   // TrainingEntity te = processedTraining.getOriginTraining();
    //assertThat(pe.getId()).isGreaterThanOrEqualTo(1);
    assertThat(trainingEntity.getScore()).isEqualTo(2);
    assertThat(processedTraining.getScore()).isEqualTo(1);
  }
}
