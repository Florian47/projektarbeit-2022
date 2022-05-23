package sommersemester2022.solution;

import org.springframework.stereotype.Service;

import javax.persistence.*;

@Entity
public class SolutionOptions {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;
  private String optionName;
  private boolean checkedAnswer;

  public SolutionOptions() {}

  public SolutionOptions(String optionName, boolean checkedAnswer) {
    this.optionName = optionName;
    this.checkedAnswer = checkedAnswer;
  }

  public int getId() {
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
