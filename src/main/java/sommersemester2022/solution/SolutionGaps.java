package sommersemester2022.solution;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.stereotype.Service;
import sommersemester2022.task.NotUniqueIdentification;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Entity
public class SolutionGaps extends NotUniqueIdentification {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @LazyCollection(LazyCollectionOption.FALSE)
  @OneToMany(cascade = CascadeType.ALL)
  private List<SolutionOptions> solutionOptions;
  public SolutionGaps() {}

  public SolutionGaps(List<SolutionOptions> solutionOptions) {
    this.solutionOptions = solutionOptions;
  }

  public int getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }


  public List<SolutionOptions> getSolutionOptions() {
    return solutionOptions;
  }

  public void setSolutionOptions(List<SolutionOptions> solutionOptions) {
    this.solutionOptions = solutionOptions;
  }
}
