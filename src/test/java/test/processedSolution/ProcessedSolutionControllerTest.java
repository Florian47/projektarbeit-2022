package test.processedSolution;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.config.Task;
import sommersemester2022.person.UserEntity;
import sommersemester2022.processedTraining.ProcessedTrainingEntity;
import sommersemester2022.solution.SolutionEntity;
import sommersemester2022.solution.SolutionGaps;
import sommersemester2022.solution.SolutionOptions;
import sommersemester2022.task.TaskEntity;
import sommersemester2022.training.TrainingEntity;
import sommersemester2022.training.TrainingRepo;
import test.BaseTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class ProcessedSolutionControllerTest extends BaseTest {

  static TrainingEntity training;

  static ProcessedTrainingEntity pTraining;
  static TaskEntity taskEntity;

  @Test
  @BeforeAll
  static void generateDummys(){
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
    taskEntity = new TaskEntity();
    taskEntity.setName("TrainingsTask");
    taskEntity.setSolution(solution);
    tasks.add(taskEntity);
    training = new TrainingEntity();
    training.setName("TrainingsTraining");
    training.setTasks(tasks);
    training.setIndividual(false);

    pTraining = new ProcessedTrainingEntity();
    pTraining.setProcessedSolutionTasks(tasks);
    pTraining.setStudentId(0);



  }

  //Musterlösung vom Lehrer
  @Test
  public void testJSONSolution() throws Exception {
    this.taskEntity=taskRepo.save(taskEntity);
    pTraining.setOriginTraining(training);
    this.training=trainingRepo.save(training);

    List <TrainingEntity> trainingList = loadAll(TrainingEntity.class);
    training = trainingList.get(0);

    ResponseEntity<String> result = restAuthGet("/generateProcessedTraining/"+training.getId(),getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);


    List <ProcessedTrainingEntity> pTrainingList = loadAll(ProcessedTrainingEntity.class);
    pTraining= pTrainingList.get(0);
    int id = pTraining.getId();


    String json = objectMapper.writeValueAsString(pTraining);
    result = restPost("/evaluate/Training/"+id,json);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    List<UserEntity> list = loadAll((UserEntity.class));
    List<ProcessedTrainingEntity> pEntityList = loadAll(ProcessedTrainingEntity.class);
    ProcessedTrainingEntity trainingEntity = pEntityList.get(0);


    //List<ProcessedTrainingEntity> processedTrainingEntityList = loadAll(ProcessedTrainingEntity.class);
    //ProcessedTrainingEntity pe = processedTrainingEntityList.get(0);
    // TrainingEntity te = processedTraining.getOriginTraining();
    //assertThat(pe.getId()).isGreaterThanOrEqualTo(1);
    assertThat(trainingEntity.getScore()).isEqualTo(2);

  }
}
