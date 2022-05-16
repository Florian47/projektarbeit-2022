package sommersemester2022.StudentGeneratedTraining;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sommersemester2022.task.TaskController;
import sommersemester2022.task.TaskEntity;
import sommersemester2022.training.TrainingController;
import sommersemester2022.training.TrainingEntity;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StudentGeneratedTrainingController {

  private TaskController taskController = new TaskController();
  private TrainingController trainingController = new TrainingController();

  @PostMapping("/studentTraining/add")
  public void createStudentGeneratedTraining(@RequestBody StudentGeneratedTrainingEntity generatedTraining) {
    List<TaskEntity> allTasks = taskController.getAllTasksForGeneratedTraining(generatedTraining.getTrainingCategory(), generatedTraining.getTrainingDifficulty());
    List<Integer> indices = new ArrayList<>();

    // Create as many random numbers as asked from student and put into list
    for (int i = 0; i <= generatedTraining.getTaskAmount(); i++) {
      int index;
      do {
        index = (int)(Math.random() * taskController.getAll().size());
      } while (indices.contains(index));
      allTasks.get(index);
    }
    // Create the official training with given amount of tasks
    TrainingEntity training = new TrainingEntity("", null, false);
    for (int i = 0; i <= indices.size(); i++) {
      training.addTask(allTasks.get(indices.get(i)));
    }
    trainingController.createTraining(training);
  }

}
