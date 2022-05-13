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
  private List<Tasks> solutionTasks;
  @ManyToOne
  private TrainingEntity originTraining;

  public ProcessedTrainingEntity() {}

  public ProcessedTrainingEntity(List<Tasks> solutionTasks, TrainingEntity originTraining) {
    this.solutionTasks = solutionTasks;
    this.originTraining = originTraining;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public List<Tasks> getSolutionTasks() {
    return solutionTasks;
  }

  public void setSolutionTasks(List<Tasks> studentSolution) {
    this.solutionTasks = studentSolution;
  }
}


