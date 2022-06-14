package sommersemester2022.solution;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sommersemester2022.task.NotUniqueIdentification;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * SolutionEntity ist die Entitätsklasse für die einzelnen Lücken der Lösung und hält alle dafür notwendigen
 * Eigenschaften (Attribute) und Fähigkeiten (Methoden).
 * @author Tobias Esau, Alexander Kiehl
 */
@Entity
public class SolutionGaps implements NotUniqueIdentification {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  /**
   * Das sind die Antwortmöglichkeiten einer Lücke
   */
  @LazyCollection(LazyCollectionOption.FALSE)
  @OneToMany(cascade = CascadeType.ALL)
  private List<SolutionOptions> solutionOptions;

  /**
   * UUID -> Ist für die Unterscheidung einer Lehrer- zu einer Schülerlösung wichtig, da sie sich die gleichen Klassen teilen!
   */
  private String notUniqueId;

  public SolutionGaps() {}

  public SolutionGaps(List<SolutionOptions> solutionOptions) {
    this.solutionOptions = solutionOptions;
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

  public List<SolutionOptions> getSolutionOptions() {
    return solutionOptions;
  }

  public void setSolutionOptions(List<SolutionOptions> solutionOptions) {
    this.solutionOptions = solutionOptions;
  }
}
