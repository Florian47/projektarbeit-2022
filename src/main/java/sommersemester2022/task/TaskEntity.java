package sommersemester2022.task;

import org.springframework.util.StringUtils;
import sommersemester2022.solution.SolutionEntity;
import sommersemester2022.solution.SolutionGaps;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

/**
 * TaskEntity ist die Entitätsklasse für eine Aufgabe und hält alle dafür notwendigen Eigenschaften (Attribute)
 * und Fähigkeiten (Methoden).
 * @author Tobias Esau, Alexander Kiehl
 */
@Entity
public class TaskEntity implements NotUniqueIdentification{

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;
  private String name;
  @Lob
  private String text;
  @Lob
  private String picture;
  private int score;
  @Enumerated(EnumType.STRING)
  private TaskCategory category;
  @Enumerated(EnumType.STRING)
  private TaskDifficulty difficulty;
  @OneToOne(cascade = CascadeType.ALL)
  private SolutionEntity solution;

  /**
   * UUID -> Ist für die Unterscheidung einer Lehrer- (Original) zu einer Schüleraufgabe (kopiert)
   * wichtig, da sie sich die gleichen Klassen teilen!
   */
  private String notUniqueId;

  /**
   * Gibt an, ob eine Aufgabe die "originale" ist, da sie beim Training kopiert wird.
   */
  private boolean individual;

  public TaskEntity() {}

  public TaskEntity(String name, String text, String picture, TaskCategory category, TaskDifficulty difficulty, SolutionEntity solution) {
    this.name = name;
    this.text = text;
    this.picture = picture;
    this.category = category;
    this.difficulty = difficulty;
    this.solution = solution;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public int getScore() {return score;}

  public void setScore(int score) {
    this.score = score;
  }

  public TaskCategory getCategory() {
    return category;
  }

  public void setCategory(TaskCategory category) {
    this.category = category;
  }

  public TaskDifficulty getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(TaskDifficulty difficulty) {
    this.difficulty = difficulty;
  }

  public void setId(Integer id) {
    this.id = id;
  }
  public Integer getId() {
    return id;}

  public SolutionEntity getSolution() {
    return solution;
  }

  public void setSolution(SolutionEntity solution) {
    this.solution = solution;
  }

  public void setNotUniqueId(String notUniqueId) {
    this.notUniqueId = notUniqueId;
  }

  public boolean isIndividual() {
    return individual;
  }

  public void setIndividual(boolean individual) {
    this.individual = individual;
  }

  @PrePersist
  private void generateRandomNotUniqueId(){
    if(!StringUtils.hasLength(this.notUniqueId)) this.notUniqueId = UUID.randomUUID().toString();
  }
  @Override
  public String getNotUniqueId() {
    return notUniqueId;
  }
}


