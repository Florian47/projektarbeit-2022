package sommersemester2022.studentGeneratedTraining;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sommersemester2022.task.TaskController;
import sommersemester2022.task.TaskEntity;
import sommersemester2022.task.TaskRepo;
import sommersemester2022.training.TrainingController;
import sommersemester2022.training.TrainingEntity;
import sommersemester2022.training.TrainingRepo;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@RestController
public class StudentGeneratedTrainingController {
  @Autowired
  private TaskRepo taskRepo;
  @Autowired
  private TrainingRepo trainingRepo;
  @PreAuthorize("hasRole('ROLE_STUDENT')")
  @PostMapping("/studentGeneratedTraining/add")
  public TrainingEntity createStudentGeneratedTraining(@RequestBody StudentGeneratedTrainingEntity generatedTraining) {
    List<TaskEntity> allTasks = taskRepo.findAllByCategoryAndDifficulty(generatedTraining.getTrainingCategory(), generatedTraining.getTrainingDifficulty());

    TrainingEntity training = new TrainingEntity("", null, false);
    allTasks.stream().limit(generatedTraining.getTaskAmount()).forEach(training::addTask);
    if (allTasks.size() < generatedTraining.getTaskAmount()) {
      throw new RuntimeException("Zu wenig Aufgaben");
    }
    return trainingRepo.save(training);
    // Token zur Identifizierung, Creator muss hinzugefügt werden
  }

}
