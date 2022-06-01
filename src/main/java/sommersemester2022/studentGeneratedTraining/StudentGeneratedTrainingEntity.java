package sommersemester2022.studentGeneratedTraining;

import sommersemester2022.task.TaskCategory;
import sommersemester2022.task.TaskDifficulty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


/**
 * StudentGeneratedTrainingEntity ist die Entitätsklasse für das auto-generierte Training und hält alle dafür
 * notwendigen Eigenschaften (Attribute) und Fähigkeiten (Methoden).
 * @author Tobias Esau, Alexander Kiehl
 */
@Entity
public class StudentGeneratedTrainingEntity {
  @Id
  @Column(name = "id", nullable = false)
  private Long id;
  private int taskAmount;
  private TaskDifficulty trainingDifficulty;
  private TaskCategory trainingCategory;

  private StudentGeneratedTrainingEntity(int taskAmount, TaskDifficulty trainingDifficulty, TaskCategory trainingCategory) {
    this.taskAmount = taskAmount;
    this.trainingDifficulty = trainingDifficulty;
    this.trainingCategory = trainingCategory;
  }

  public StudentGeneratedTrainingEntity() {

  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getTaskAmount() {
    return taskAmount;
  }

  public void setTaskAmount(int taskAmount) {
    this.taskAmount = taskAmount;
  }

  public TaskDifficulty getTrainingDifficulty() {
    return trainingDifficulty;
  }

  public void setTrainingDifficulty(TaskDifficulty trainingDifficulty) {
    this.trainingDifficulty = trainingDifficulty;
  }

  public TaskCategory getTrainingCategory() {
    return trainingCategory;
  }

  public void setTrainingCategory(TaskCategory trainingCategory) {
    this.trainingCategory = trainingCategory;
  }
}
