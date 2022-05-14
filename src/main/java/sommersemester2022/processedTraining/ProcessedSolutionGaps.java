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
  @ElementCollection
  private List<String> checkedOptions;
  @ElementCollection
  private List<String> uncheckedOptions;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public List<String> getAnswer() {
    return checkedOptions;
  }

  public void setCheckedOptions(List<String> answer) {
    this.checkedOptions = checkedOptions;
  }

  public ProcessedSolutionTasks getTask() {
    return task;
  }

  public void setTask(ProcessedSolutionTasks task) {
    this.task = task;
  }

  public List<String> getCheckedOptions() {
    return checkedOptions;
  }

  public List<String> getUncheckedOptions() {
    return uncheckedOptions;
  }

  public void setUncheckedOptions(List<String> uncheckedOptions) {
    this.uncheckedOptions = uncheckedOptions;
  }
}
