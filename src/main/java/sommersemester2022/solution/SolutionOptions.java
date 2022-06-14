package sommersemester2022.solution;

import org.springframework.util.StringUtils;
import sommersemester2022.task.NotUniqueIdentification;

import javax.persistence.*;
import java.util.UUID;


/**
 * SolutionEntity ist die Entitätsklasse für die Antwortmöglichkeiten einer Lösung und hält alle dafür notwendigen
 * Eigenschaften (Attribute) und Fähigkeiten (Methoden).
 * @author Tobias Esau, Alexander Kiehl
 */
@Entity
public class SolutionOptions implements NotUniqueIdentification {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;
  private String optionName;

  /**
   * UUID -> Ist für die Unterscheidung einer Lehrer- zu einer Schülerlösung wichtig, da sie sich die gleichen Klassen teilen!
   */
  private String notUniqueId;
  private boolean checkedAnswer;

  public SolutionOptions() {}

  public SolutionOptions(String optionName, boolean checkedAnswer) {
    this.optionName = optionName;
    this.checkedAnswer = checkedAnswer;
  }

  /**
   * Generiert eine zufällige UUID (not unique ID)
   */
  @PrePersist
  private void generateRandomNotUniqueId(){
    if(!StringUtils.hasLength(this.notUniqueId)) this.notUniqueId = UUID.randomUUID().toString();
  }
  @Override
  public String getNotUniqueId() {
    return notUniqueId;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
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
