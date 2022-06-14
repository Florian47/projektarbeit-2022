package sommersemester2022.solution;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sommersemester2022.processedTraining.ProcessedTrainingEntity;
import sommersemester2022.task.NotUniqueIdentification;
import sommersemester2022.task.TaskEntity;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

/**
 * SolutionEntity ist die Entitätsklasse für die Lösung und hält alle dafür notwendigen
 * Eigenschaften (Attribute) und Fähigkeiten (Methoden).
 * @author Tobias Esau, Alexander Kiehl
 */
@Entity
public class SolutionEntity implements NotUniqueIdentification {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @LazyCollection(LazyCollectionOption.FALSE)
  @OneToMany(cascade = CascadeType.ALL)
  private List<SolutionGaps> solutionGaps;
  private String hint;

  /**
   * UUID -> Ist für die Unterscheidung einer Lehrer- zu einer Schülerlösung wichtig, da sie sich die gleichen Klassen teilen!
   */
  private String notUniqueId;

  public SolutionEntity() {}

  public SolutionEntity(List<SolutionGaps> solutionGaps){
    this.solutionGaps = solutionGaps;
  }
  public SolutionEntity(List<SolutionGaps> solutionGaps, String hint) {
    this.solutionGaps = solutionGaps;
    this.hint = hint;
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
    return this.id;
  }
  public void setId(Integer id) {
    this.id = id;
  }
  public List<SolutionGaps> getSolutionGaps() {
    return solutionGaps;
  }
  public void setSolutionGaps(List<SolutionGaps> solutionGaps) {
    this.solutionGaps = solutionGaps;
  }
  public String getHint() {
    return hint;
  }
  public void setHint(String hint) {
    this.hint = hint;
  }
  }


