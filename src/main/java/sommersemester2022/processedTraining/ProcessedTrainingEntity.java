package sommersemester2022.processedTraining;

import sommersemester2022.training.TrainingEntity;

import javax.persistence.*;
import java.util.List;

@Entity
public class ProcessedTrainingEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;
  private int score;
  @OneToMany
  private List<ProcessedSolutionTasks> processedSolutionTasks;
  @ManyToOne
  private TrainingEntity originTraining;

  public ProcessedTrainingEntity() {}

  public ProcessedTrainingEntity(List<ProcessedSolutionTasks> processedSolutionTasks, TrainingEntity originTraining) {
    this.processedSolutionTasks = processedSolutionTasks;
    this.originTraining = originTraining;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public List<ProcessedSolutionTasks> getSolutionTasks() {
    return processedSolutionTasks;
  }

  public void setSolutionTasks(List<ProcessedSolutionTasks> studentSolution) {
    this.processedSolutionTasks = studentSolution;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public List<ProcessedSolutionTasks> getProcessedSolutionTasks() {
    return processedSolutionTasks;
  }

  public void setProcessedSolutionTasks(List<ProcessedSolutionTasks> processedSolutionTasks) {
    this.processedSolutionTasks = processedSolutionTasks;
  }

  public TrainingEntity getOriginTraining() {
    return originTraining;
  }

  public void setOriginTraining(TrainingEntity originTraining) {
    this.originTraining = originTraining;
  }
}


