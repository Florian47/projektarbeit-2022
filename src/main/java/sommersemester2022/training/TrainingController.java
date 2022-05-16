package sommersemester2022.training;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sommersemester2022.person.UserEntity;
import sommersemester2022.task.TaskEntity;

import java.util.List;
import java.util.Optional;

@RestController
public class TrainingController {
  @Autowired
  private TrainingRepo trainingRepo;

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

  @DeleteMapping("/training/{id}")
  public void deleteTraining(@PathVariable int id) {
    trainingRepo.deleteById(id);
  }

  @GetMapping("/schueler/{id}")
  public List<TrainingEntity> getAllTrainingsForStudent(@PathVariable int id, @RequestBody UserEntity student) {
    return trainingRepo.findByStudents(id).get();
  }

  @GetMapping("/schueler/all")
  public List<UserEntity> getAllUsersForTraining(@RequestBody TrainingEntity training) {
    return training.getStudents();
  }

  @PutMapping("/training/add")
  public TrainingEntity addTasksToTraining(@RequestBody TrainingEntity training, @RequestBody List<TaskEntity> tasks) {
    for (TaskEntity task: tasks) {
      training.addTask(task);
    }
    return trainingRepo.save(training);
  }

}
