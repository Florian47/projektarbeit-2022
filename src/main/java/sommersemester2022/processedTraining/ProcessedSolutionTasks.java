package sommersemester2022.processedTraining;

import sommersemester2022.solution.SolutionGaps;

import javax.persistence.*;
import java.util.List;

@Entity
public class ProcessedSolutionTasks {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;
  @OneToMany(fetch = FetchType.EAGER)
  private List<SolutionGaps> gaps;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public List<SolutionGaps> getGaps() {
    return gaps;
  }

  public void setGaps(List<SolutionGaps> gaps) {
    this.gaps = gaps;
  }
}
