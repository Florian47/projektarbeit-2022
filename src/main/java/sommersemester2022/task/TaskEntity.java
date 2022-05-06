package sommersemester2022.task;

import sommersemester2022.person.UserEntity;
import sommersemester2022.solution.SolutionEntity;

import javax.persistence.*;

@Entity
public class TaskEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  private String text;
  private String picture;
  private int score;
  @Enumerated(EnumType.STRING)
  private TaskCategory category;
  @Enumerated(EnumType.STRING)
  private TaskDifficulty difficulty;
  @OneToOne(cascade = CascadeType.PERSIST)
  private SolutionEntity solution;
  @ManyToOne
  private UserEntity creator;

  public TaskEntity() {}

  public TaskEntity(String text, String picture, int score, TaskCategory category, TaskDifficulty difficulty, SolutionEntity solution, UserEntity creator) {
    this.text = text;
    this.picture = picture;
    this.score = score;
    this.category = category;
    this.difficulty = difficulty;
    this.solution = solution;
    this.creator = creator;
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
}


