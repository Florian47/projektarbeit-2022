package sommersemester2022.solution;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;
import java.util.Map;

@Entity
public class SolutionEntity {

  @Id
  @GeneratedValue
  private long id;

 // @ElementCollection
 // private List<Helper> solution;
//  private List<Map<String, Boolean>> solution;
  private String hint;

  public SolutionEntity() {}

  public SolutionEntity(List<Map<String, Boolean>> solution, String hint) {
    this.solution = solution;
    this.hint = hint;
  }

  public List<Map<String, Boolean>> getSolution() {
    return solution;
  }

  public void setSolution(List<Map<String, Boolean>> solution) {
    this.solution = solution;
  }

  public String getHint() {
    return hint;
  }

  public void setHint(String hint) {
    this.hint = hint;
  }
}

