package sommersemester2022.studentGeneratedTraining;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sommersemester2022.task.TaskController;
import sommersemester2022.task.TaskEntity;
import sommersemester2022.task.TaskRepo;
import sommersemester2022.training.TrainingController;
import sommersemester2022.training.TrainingEntity;
import sommersemester2022.training.TrainingRepo;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StudentGeneratedTrainingController {
  @Autowired
  private TaskRepo taskRepo;
  @Autowired
  private TrainingRepo trainingRepo;

  @PostMapping("/studentGeneratedTraining/add")
  public TrainingEntity createStudentGeneratedTraining(@RequestBody StudentGeneratedTrainingEntity generatedTraining) {
    List<TaskEntity> allTasks = taskRepo.findAllByCategoryAndDifficulty( generatedTraining.getTrainingCategory(), generatedTraining.getTrainingDifficulty() );
    List<Integer> indices = new ArrayList<>();

    // Create as many random numbers as asked from student and put into list
    for (int i = 0; i < generatedTraining.getTaskAmount(); i++) {
      int index;
      do {
        index = (int) (Math.random() * taskRepo.count());
      } while (indices.contains(index));
      indices.add(index);
    }
    // Create the official training with given amount of tasks
    TrainingEntity training = new TrainingEntity("", null, false);
    for (int i = 0; i < indices.size(); i++) {
      training.addTask(allTasks.get(indices.get(i)));
    }
    return trainingRepo.save(training);
    // Token zur Identifizierung, Creator muss hinzugefügt werden
  }

}
