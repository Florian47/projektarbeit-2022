package sommersemester2022.processedTraining;

import javax.persistence.*;
import java.util.List;

@Entity
public class ProcessedSolutionGaps {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;


  String answer;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }
}
