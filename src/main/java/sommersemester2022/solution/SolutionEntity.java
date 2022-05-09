package sommersemester2022.solution;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
public class SolutionEntity {

  @Id
  @GeneratedValue
  private int id;

  @ElementCollection
  private List<SolutionOptions> solution;
  //@OneToMany(cascade=CascadeType.ALL)
  //private List<Map<String, Boolean>> solution;
  private String hint;

  public SolutionEntity() {}

  public SolutionEntity(List<SolutionOptions> solution, String hint) {
    this.solution = solution;
    this.hint = hint;
  }

  public List<SolutionOptions> getSolution() {
    return solution;
  }

  public void setSolution(List<SolutionOptions> solution) {
    this.solution = solution;
  }

  public String getHint() {
    return hint;
  }

  public void setHint(String hint) {
    this.hint = hint;
  }
}

