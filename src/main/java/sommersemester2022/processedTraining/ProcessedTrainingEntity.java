package sommersemester2022.processedTraining;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.transaction.annotation.Transactional;
import sommersemester2022.person.UserEntity;
import sommersemester2022.task.TaskEntity;
import sommersemester2022.training.TrainingEntity;

import javax.persistence.*;
import java.util.List;

/**
 * ProcessedTrainingEntity ist die Entitätsklasse für das bearbeitete Training und hält alle dafür notwendigen
 * Eigenschaften (Attribute) und Fähigkeiten (Methoden).
 * @author Tobias Esau, Alexander Kiehl
 */
@Entity
@Transactional
public class ProcessedTrainingEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  private int score;

  public UserEntity getStudent() {
    return student;
  }

  public void setStudent(UserEntity student) {
    this.student = student;
  }

  @ManyToOne
  private UserEntity student;
  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  private List<TaskEntity> processedSolutionTasks;

  /**
   * originTraining beschreibt das zugehörige Training, welches vom Dozent erstellt wurde.
   */
  @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  private TrainingEntity originTraining;


  public ProcessedTrainingEntity() {
  }

  public ProcessedTrainingEntity(List<TaskEntity> processedSolutionTasks, TrainingEntity originTraining, UserEntity user) {
    this.processedSolutionTasks = processedSolutionTasks;
    this.originTraining = originTraining;
    this.student = user;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public List<TaskEntity> getProcessedSolutionTasks() {
    return processedSolutionTasks;
  }

  public void setProcessedSolutionTasks(List<TaskEntity> processedSolutionTasks) {
    this.processedSolutionTasks = processedSolutionTasks;
  }

  public TrainingEntity getOriginTraining() {
    return originTraining;
  }

  public void setOriginTraining(TrainingEntity originTraining) {
    this.originTraining = originTraining;
  }

}


