package sommersemester2022.processedTraining;

import javax.persistence.*;
import java.util.List;

@Entity
public class ProcessedSolutionGaps {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;
  @ManyToOne
  private ProcessedSolutionTasks task;
  private String optionName;
  private boolean rightAnswer;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public ProcessedSolutionTasks getTask() {
    return task;
  }

  public void setTask(ProcessedSolutionTasks task) {
    this.task = task;
  }
}
