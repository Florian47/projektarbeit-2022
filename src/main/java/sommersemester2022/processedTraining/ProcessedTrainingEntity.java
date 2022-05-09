package sommersemester2022.processedTraining;

import sommersemester2022.person.UserEntity;
import sommersemester2022.training.TrainingEntity;

import javax.persistence.*;
import java.util.List;

@Entity
public class ProcessedTrainingEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;
  private int score;
  @ElementCollection
  private List<Tasks> studentSolution;
  @ManyToOne
  private TrainingEntity originTraining;

  public ProcessedTrainingEntity() {}

  public ProcessedTrainingEntity(List<Tasks> studentSolution, TrainingEntity originTraining) {
    this.studentSolution = studentSolution;
    this.originTraining = originTraining;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public List<Tasks> getStudentSolution() {
    return studentSolution;
  }

  public void setStudentSolution(List<Tasks> studentSolution) {
    this.studentSolution = studentSolution;
  }
}


