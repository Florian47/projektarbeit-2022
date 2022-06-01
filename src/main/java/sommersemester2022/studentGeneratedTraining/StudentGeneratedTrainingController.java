package sommersemester2022.studentGeneratedTraining;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sommersemester2022.solution.SolutionRepo;
import sommersemester2022.task.TaskController;
import sommersemester2022.task.TaskEntity;
import sommersemester2022.task.TaskRepo;
import sommersemester2022.training.TrainingController;
import sommersemester2022.training.TrainingEntity;
import sommersemester2022.training.TrainingRepo;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * StudentGeneratedTrainingController ist die Controller-Klasse für die Entität des auto-generierten Trainings.
 * Sie kontrolliert alle Aktivitäten, welche mit den auto-generierten Trainings ausgeführt werden können.
 * Sie besitzt keine Verbindung zu einem Repository und findet damit auch keine direkte Verwendung in der Datenbank.
 * @author Tobias Esau, Florian Weinert
 */
@Transactional
@RestController
public class StudentGeneratedTrainingController {
  @Autowired
  private TaskRepo taskRepo;
  @Autowired
  private TrainingRepo trainingRepo;

  /**
   * Erstellt ein auto-generiertes Training.
   * @param generatedTraining Frontend Daten für auto-generiertes Training
   * @return generiertes und gespeichertes Training
   */
  @PreAuthorize("hasRole('ROLE_STUDENT')")
  @PostMapping("/studentGeneratedTraining/add")
  public TrainingEntity createStudentGeneratedTraining(@RequestBody StudentGeneratedTrainingEntity generatedTraining) {
    List<TaskEntity> allTasks = taskRepo.findAllByCategoryAndDifficulty(generatedTraining.getTrainingCategory(), generatedTraining.getTrainingDifficulty());

    // Adds all tasks that were got from the database in the given amount to the training
    TrainingEntity training = new TrainingEntity("", null, false);
    allTasks.stream().limit(generatedTraining.getTaskAmount()).forEach(training::addTask);
    if (allTasks.size() < generatedTraining.getTaskAmount()) {
      throw new RuntimeException("Zu wenig Aufgaben");
    }
    return trainingRepo.save(training);
  }
}
