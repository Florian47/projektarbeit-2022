package sommersemester2022.studentGeneratedTraining;

import sommersemester2022.task.TaskCategory;
import sommersemester2022.task.TaskDifficulty;

public class StudentGeneratedTrainingEntity {

  private int taskAmount;
  private TaskDifficulty trainingDifficulty;
  private TaskCategory trainingCategory;

  private StudentGeneratedTrainingEntity(int taskAmount, TaskDifficulty trainingDifficulty, TaskCategory trainingCategory) {
    this.taskAmount = taskAmount;
    this.trainingDifficulty = trainingDifficulty;
    this.trainingCategory = trainingCategory;
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
