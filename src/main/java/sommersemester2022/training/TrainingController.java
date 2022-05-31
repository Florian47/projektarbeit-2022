package sommersemester2022.training;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sommersemester2022.person.UserEntity;
import sommersemester2022.person.UserRepo;
import sommersemester2022.processedTraining.ProcessedTrainingRepo;
import sommersemester2022.task.TaskEntity;

import java.util.List;
import java.util.Optional;

@RestController
public class TrainingController {
  @Autowired
  private TrainingRepo trainingRepo;
  @Autowired
  private UserRepo userRepo;

  @Autowired
  private ProcessedTrainingRepo processedTrainingRepo;
  //@PreAuthorize("hasRole('ROLE_TEACHER')")
  //TODO undo comment
  @PostMapping("/training/add")
  public TrainingEntity createTraining(@RequestBody TrainingEntity training) {
    return trainingRepo.save(training);
  }

  @GetMapping("/training/{id}")
  public TrainingEntity getById(@PathVariable int id) {
    return trainingRepo.findById(id).get();
  }

  @GetMapping("/training")
  public List<TrainingEntity> getAll() {
    return trainingRepo.findAll();
  }

  @GetMapping("/training/individuell")
  public List<TrainingEntity> getAllManuelTrainings() {
    return trainingRepo.findByIndividualTrue();
  }
  @PreAuthorize("hasRole('ROLE_TEACHER')")
  @DeleteMapping("/training/{id}")
  public void deleteTraining(@PathVariable int id) {
    trainingRepo.deleteById(id);
  }

  @GetMapping("/training/schueler/{id}")
  public List<TrainingEntity> getAllTrainingsForStudent(@PathVariable int id) {
    Optional<UserEntity> user = userRepo.findById(id);
    return trainingRepo.findByStudentsAndIndividualTrue(user).get();
  }

  @GetMapping("/schueler/all")
  public List<UserEntity> getAllUsersForTraining(@RequestBody TrainingEntity training) {
    return training.getStudents();
  }
  @PreAuthorize("hasRole('ROLE_TEACHER')")
  @PutMapping("/training/add")
  public TrainingEntity addTasksToTraining(@RequestBody TrainingEntity training, @RequestBody List<TaskEntity> tasks) {
    for (TaskEntity task : tasks) {
      training.addTask(task);
    }
    return trainingRepo.save(training);
  }
  @PreAuthorize("hasRole('ROLE_TEACHER')")
  @PutMapping("/training/edit/{id}")
  public TrainingEntity editTraining(@PathVariable int id, @RequestBody TrainingEntity training) {
    training.setId(id);
    return trainingRepo.save(training);
  }
}

