package test.processedSolution;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sommersemester2022.processedTraining.ProcessedTrainingEntity;
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

public class ProcessedTrainingControllerTest extends BaseTest {

  static TrainingEntity training;
  static ProcessedTrainingEntity pTraining;

  /**
   * Test um ein ProcessedTraining zu erstelle ohne vorher die IDs zu löschen. Schlägt fehl da die Duplikate nicht gespeichert werden können
   * @throws Exception
   */
  @Test
  public void testCreateProcessedTraining() throws Exception {
    ProcessedTrainingEntity proc = new ProcessedTrainingEntity();
    List<TaskEntity> tasks = new ArrayList<>();
    tasks.add(task);
    proc.setProcessedSolutionTasks(tasks);
    proc.setOriginTraining(training);
    String json = objectMapper.writeValueAsString(proc);
    ResponseEntity<String> result = restPost("/processedTraining/add", json);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   *  Testet die auswertung aller Trainings mit der gegebenen ID
   * @throws Exception
   */
  @Test
  public void testEvaluateTrainings() throws Exception {
    List <TrainingEntity> trainingList = loadAll(TrainingEntity.class);
    training = trainingList.get(0);
    ResponseEntity<String> result = restAuthGet("/generateProcessedTraining/"+training.getId(),getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    result = restAuthGet("/generateProcessedTraining/"+training.getId(),getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    List <ProcessedTrainingEntity> pTrainingList = loadAll(ProcessedTrainingEntity.class);
    pTraining=pTrainingList.get(0);
    int id = pTraining.getOriginTraining().getId();
    pTrainingList.get(0).getProcessedSolutionTasks().get(0).getSolution().getSolutionGaps().get(0).getSolutionOptions().get(1).setCheckedAnswer(true);
    pTrainingList.get(0).getProcessedSolutionTasks().get(0).getSolution().getSolutionGaps().get(0).getSolutionOptions().get(2).setCheckedAnswer(true);
    processedTrainingRepo.save(pTraining);
    String json = objectMapper.writeValueAsString(pTraining.getOriginTraining().getId());
    result = restGet("/evaluate/Training/"+id);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    List<ProcessedTrainingEntity> pEntityList = loadAll(ProcessedTrainingEntity.class);
    List<ProcessedTrainingEntity> list = objectMapper.readValue(result.getBody(), new TypeReference<>() {
    });
    assertThat(list.get(0).getScore()).isEqualTo(1);
    assertThat(list.get(1).getScore()).isEqualTo(0);
  }

  /**
   * Holt ein processedTraining mit der ID aus der Datenbank
   * @throws Exception
   */
  @Test
  public void testGetTraining() throws Exception {
    List <TrainingEntity> trainingList = loadAll(TrainingEntity.class);
    training = trainingList.get(0);
    ResponseEntity<String> result = restAuthGet("/generateProcessedTraining/"+training.getId(),getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    List <ProcessedTrainingEntity> pTrainingList = loadAll(ProcessedTrainingEntity.class);
    pTraining=pTrainingList.get(0);
    int id = pTraining.getId();
    result = restGet("/processedTraining/"+id);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.toString().contains("Test1"));
  }
  /**
   * LÖscht ein ProcessedTraining aus der Datenbank
   * @throws Exception
   */
  @Test
  public void testDelTraining() throws Exception {
    List <TrainingEntity> trainingList = loadAll(TrainingEntity.class);
    training = trainingList.get(0);
    ResponseEntity<String> result = restAuthGet("/generateProcessedTraining/"+training.getId(),getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    List <ProcessedTrainingEntity> pTrainingList = loadAll(ProcessedTrainingEntity.class);
    pTraining=pTrainingList.get(0);
    int id = pTraining.getId();
    result = restDel("/processedTraining/delete/"+id);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    pTrainingList = loadAll(ProcessedTrainingEntity.class);
    assertThat(pTrainingList.size()).isEqualTo(0);
  }
  /**
   * Ändert eine Training mit der angegebenen ID
   * @throws Exception
   */
  @Test
  public void testUpdateTraining() throws Exception {

    List <TrainingEntity> trainingList = loadAll(TrainingEntity.class);
    training = trainingList.get(0);
    ResponseEntity<String> result = restAuthGet("/generateProcessedTraining/"+training.getId(),getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    List <ProcessedTrainingEntity> pTrainingList = loadAll(ProcessedTrainingEntity.class);
    pTraining=pTrainingList.get(0);
    int id = pTraining.getId();
    pTraining.getOriginTraining().setName("Test2");
    String json = objectMapper.writeValueAsString(pTraining);
    result = restAuthPut("/processedTraining/"+id, json,getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    pTrainingList = loadAll(ProcessedTrainingEntity.class);
    assertThat(pTrainingList.get(0).getOriginTraining().getName()).isEqualTo("Test1");
  }

  /**
   * Lädt alle ProcessedTrainingEntites aus der DB
   * @throws Exception
   */
  @Test
  public void testGetAllProcessedTrainings() throws Exception {
    List<TrainingEntity> trainingList = loadAll(TrainingEntity.class);
    training = trainingList.get(0);
    ResponseEntity<String> result = restAuthGet("/generateProcessedTraining/" + training.getId(), getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    result = restGet("/processedTrainings");
    assertThat(result.toString()).contains("Test1");
  }

  /**
   * Erzeugt ein ProcessedTraining aus einem Training
   * @throws Exception
   */

  @Test
  public void testGenerateProcessedTraining() throws Exception {
    List<TrainingEntity> trainingList = loadAll(TrainingEntity.class);
    training = trainingList.get(0);
    ResponseEntity<String> result = restAuthGet("/generateProcessedTraining/" + training.getId(), getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    List <ProcessedTrainingEntity> pTrainingList = loadAll(ProcessedTrainingEntity.class);
    assertThat(pTrainingList.size()).isEqualTo(1);
  }

  /**
   * Überprüft ob die IDs beim generieren eines ProcessedTrainings verändert werden
   * @throws Exception
   */
  @Test
  public void testGenerateProcessedTrainingGetsAltered() throws Exception {
    List<TrainingEntity> trainingList = loadAll(TrainingEntity.class);
    training = trainingList.get(0);
    ResponseEntity<String> result = restAuthGet("/generateProcessedTraining/" + training.getId(), getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    List <ProcessedTrainingEntity> pTrainingList = loadAll(ProcessedTrainingEntity.class);
    pTraining = pTrainingList.get(0);
    assertThat(pTraining.getProcessedSolutionTasks().get(0).getId()).isNotEqualTo(training.getTasks().get(0).getId());
  }
  /**
   *  Wertet ein processedTraining mit der gegebene ID aus
   * @throws Exception
   */
  @Test
  public void testEvaluateTraining() throws Exception {
    List <TrainingEntity> trainingList = loadAll(TrainingEntity.class);
    training = trainingList.get(0);
    ResponseEntity<String> result = restAuthGet("/generateProcessedTraining/"+training.getId(),getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    List <ProcessedTrainingEntity> pTrainingList = loadAll(ProcessedTrainingEntity.class);
    pTraining=pTrainingList.get(0);
    int id = pTraining.getOriginTraining().getId();
    pTrainingList.get(0).getProcessedSolutionTasks().get(0).getSolution().getSolutionGaps().get(0).getSolutionOptions().get(1).setCheckedAnswer(true);
    pTrainingList.get(0).getProcessedSolutionTasks().get(0).getSolution().getSolutionGaps().get(0).getSolutionOptions().get(2).setCheckedAnswer(true);
    processedTrainingRepo.save(pTraining);
    String json = objectMapper.writeValueAsString(pTraining);
    result = restPost("/evaluate/ProcessedTraining",json);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    List<ProcessedTrainingEntity> pEntityList = loadAll(ProcessedTrainingEntity.class);
    assertThat(pEntityList.get(0).getScore()).isEqualTo(1);
  }
}



