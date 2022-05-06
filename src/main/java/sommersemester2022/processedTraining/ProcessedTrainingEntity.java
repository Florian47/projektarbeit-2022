package sommersemester2022.processedTraining;

import sommersemester2022.person.UserEntity;
import sommersemester2022.training.TrainingEntity;

import javax.persistence.*;
import java.util.List;

@Entity
public class ProcessedTrainingEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  private int score;
  private List<List<List<Boolean>>> studentSolution;
  @ManyToOne
  private TrainingEntity originTraining;

  public ProcessedTrainingEntity() {}

  public ProcessedTrainingEntity(List<List<List<Boolean>>> studentSolution, TrainingEntity originTraining) {
    this.studentSolution = studentSolution;
    this.originTraining = originTraining;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public List<List<List<Boolean>>> getStudentSolution() {
    return studentSolution;
  }

  public void setStudentSolution(List<List<List<Boolean>>> studentSolution) {
    this.studentSolution = studentSolution;
  }
}


