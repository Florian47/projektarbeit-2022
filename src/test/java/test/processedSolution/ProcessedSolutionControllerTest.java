package test.processedSolution;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeAll;
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
/**
 * @author Alexander Kiehl
 * In dieser Klasse werden die CRUD Operationen des ProcessedSolutionControllers getestet.
 */

public class ProcessedSolutionControllerTest extends BaseTest {

  static TrainingEntity training;
  static ProcessedTrainingEntity pTraining;
  static TaskEntity taskEntity;
  static SolutionEntity solution;

  @Test
  @BeforeAll
  static void generateDummys(){
  }


  @Test
  public void testCreateTraining() throws Exception {
    this.task = taskRepo.save(taskEntity);
    this.training=trainingRepo.save(training);
    pTraining.setStudent(admin);
    //pTraining.setOriginTraining(training);


    ProcessedTrainingEntity proc = new ProcessedTrainingEntity();
    List<TaskEntity> tasks = new ArrayList<>();
    tasks.add(task);
    //proc.setStudentId(0);
    //processedTrainingRepo.save(proc);
    proc.setProcessedSolutionTasks(tasks);
    proc.setOriginTraining(training);
    processedTrainingRepo.save(pTraining);

    String json = objectMapper.writeValueAsString(proc);
    ResponseEntity<String> result = restPost("/processedTraining/add", json);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    List <ProcessedTrainingEntity> pTrainingList = loadAll(ProcessedTrainingEntity.class);
    pTraining = pTrainingList.get(0);
    assertThat(pTraining.getStudent()).isEqualTo(0);
    //assertThat(pTraining.getOriginTraining().getName()).isEqualTo("TrainingsTraining");
    //assertThat(pTraining.getProcessedSolutionTasks().get(0).getSolution().getSolutionGaps().get(0).getSolutionOptions().get(0).getOptionName()).isEqualTo("Montag");


  }

  /**
   *  Test der Funktion "/generateProcessedTraining/" mit einer 100% richtigen Lösung und einer 100% falschen Lösung
   * @throws Exception
   */
  @Test
  public void testEvaluateTraining() throws Exception {
    this.taskEntity=taskRepo.save(taskEntity);
    pTraining.setOriginTraining(training);
    this.training=trainingRepo.save(training);
    pTraining.setStudent(admin);

    List <TrainingEntity> trainingList = loadAll(TrainingEntity.class);
    training = trainingList.get(0);

    ResponseEntity<String> result = restAuthGet("/generateProcessedTraining/"+training.getId(),getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    result = restAuthGet("/generateProcessedTraining/"+training.getId(),getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    pTraining.getProcessedSolutionTasks().get(0).setSolution(solution);

    List <ProcessedTrainingEntity> pTrainingList = loadAll(ProcessedTrainingEntity.class);
    pTraining= pTrainingList.get(0);
    int id = pTraining.getOriginTraining().getId();
    pTrainingList.get(0).getProcessedSolutionTasks().get(0).getSolution().getSolutionGaps().get(0).getSolutionOptions().get(1).setCheckedAnswer(true);
    pTrainingList.get(0).getProcessedSolutionTasks().get(0).getSolution().getSolutionGaps().get(0).getSolutionOptions().get(2).setCheckedAnswer(true);
    pTrainingList.get(0).getProcessedSolutionTasks().get(0).getSolution().getSolutionGaps().get(1).getSolutionOptions().get(1).setCheckedAnswer(true);
    pTrainingList.get(0).getProcessedSolutionTasks().get(0).getSolution().getSolutionGaps().get(1).getSolutionOptions().get(2).setCheckedAnswer(true);
    processedTrainingRepo.save(pTraining);

    String json = objectMapper.writeValueAsString(pTraining.getOriginTraining().getId());
    result = restGet("/evaluate/Training/"+id);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    List<ProcessedTrainingEntity> pEntityList = loadAll(ProcessedTrainingEntity.class);
    ProcessedTrainingEntity trainingEntity = pEntityList.get(0);
    List<ProcessedTrainingEntity> list = objectMapper.readValue(result.getBody(), new TypeReference<>() {
    });

    assertThat(list.get(0).getScore()).isEqualTo(2);
    assertThat(list.get(1).getScore()).isEqualTo(0);
  }
}
