package sommersemester2022.task;

import sommersemester2022.person.UserEntity;
import sommersemester2022.solution.SolutionEntity;
import sommersemester2022.solution.SolutionGaps;

import javax.persistence.*;
import java.util.List;

@Entity
public class TaskEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;
  private String name;
  private String text;
  private String picture;
  private int score;
  @Enumerated(EnumType.STRING)
  private TaskCategory category;
  @Enumerated(EnumType.STRING)
  private TaskDifficulty difficulty;
  @OneToOne(cascade = CascadeType.PERSIST)
  private SolutionEntity solution;

  public TaskEntity() {}

  public TaskEntity(String name, String text, String picture, TaskCategory category, TaskDifficulty difficulty, SolutionEntity solution) {
    this.name = name;
    this.text = text;
    this.picture = picture;
    this.category = category;
    this.difficulty = difficulty;
    this.solution = solution;

    // Score = Amount of gaps
    List<SolutionGaps> amount = solution.getSolutionGaps();
    this.score = amount.size();
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

  public int getScore() {
    return score;
  }

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

  public void setId(int id) {
    this.id = id;
  }
  public int getId() {
    return this.id;
  }

  public SolutionEntity getSolution() {
    return solution;
  }

  public void setSolution(SolutionEntity solution) {
    this.solution = solution;
  }
}


