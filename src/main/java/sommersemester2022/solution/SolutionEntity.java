package sommersemester2022.solution;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.stereotype.Service;
import sommersemester2022.processedTraining.ProcessedTrainingEntity;
import sommersemester2022.task.TaskEntity;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;

@Entity
public class SolutionEntity {

  @Id
  @GeneratedValue
  private int id;
  @LazyCollection(LazyCollectionOption.FALSE)
  @OneToMany(cascade = CascadeType.ALL, fetch= FetchType.EAGER)
  private List<SolutionGaps> solutionGaps;
  private String hint;
  @OneToOne(mappedBy = "solution")
  private TaskEntity relatedTask;

  public SolutionEntity() {}

  public SolutionEntity(List<SolutionGaps> solutionGaps, String hint) {
    this.solutionGaps = solutionGaps;
    this.hint = hint;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public List<SolutionGaps> getSolutionGaps() {
    return solutionGaps;
  }

  public void setSolutionGaps(List<SolutionGaps> solutionGaps) {
    this.solutionGaps = solutionGaps;
  }

  public String getHint() {
    return hint;
  }

  public void setHint(String hint) {
    this.hint = hint;
  }
}

