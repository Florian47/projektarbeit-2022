package sommersemester2022.solution;

import org.springframework.stereotype.Service;
import sommersemester2022.task.NotUniqueIdentification;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.UUID;

@Entity
public class SolutionOptions implements NotUniqueIdentification {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  private String optionName;
  private boolean rightAnswer;
  private String notUniqueId;
  private boolean checkedAnswer;

  public SolutionOptions() {}

  public SolutionOptions(String optionName, boolean checkedAnswer) {
    this.optionName = optionName;
    this.checkedAnswer = checkedAnswer;
  }

  @PrePersist
  private void generateRandomNotUniqueId(){
    if(this.notUniqueId == null) this.notUniqueId = UUID.randomUUID().toString();
  }
  @Override
  public String getNotUniqueId() {
    return notUniqueId;
  }

  public Integer getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public boolean isCheckedAnswer() {
    return checkedAnswer;
  }

  public void setCheckedAnswer(boolean rightAnswer) {
    this.checkedAnswer = rightAnswer;
  }

  public String getOptionName() {
    return optionName;
  }

  public void setOptionName(String optionName) {
    this.optionName = optionName;
  }
}
