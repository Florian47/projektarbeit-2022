package sommersemester2022.processedTraining;

import javax.persistence.*;
import java.util.List;

@Entity
public class ProcessedSolutionTasks {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @OneToMany(fetch = FetchType.EAGER)
  List<ProcessedSolutionGaps> gaps;

  public ProcessedSolutionTasks() {}

  public ProcessedSolutionTasks(List <ProcessedSolutionGaps> gaps) {
    this.gaps = gaps;
  }
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public List<ProcessedSolutionGaps> getGaps() {
    return gaps;
  }

  public void setGaps(List<ProcessedSolutionGaps> gaps) {
    this.gaps = gaps;
  }

  public int getGapsLength(){return gaps.size();}
}
