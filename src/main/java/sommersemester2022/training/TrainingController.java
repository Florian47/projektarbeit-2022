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

  @PostMapping("/users/register")
  public TrainingEntity createTraining(@RequestBody TrainingEntity training) {
    return trainingRepo.save(training);
  }

  @GetMapping("/users/{id}")
  public TrainingEntity getById(@PathVariable int id) {
    return trainingRepo.findById(id).get();
  }

  @DeleteMapping("/users/{id}")
  public void deleteTraining(@PathVariable int id) {
    trainingRepo.deleteById(id);
  }

  @GetMapping("/schueler")
  public Optional<List<TrainingEntity>> getAllTrainingsForStudent(@PathVariable int id, @RequestBody UserEntity student) {
    return trainingRepo.findByStudent(id);
  }

  @PutMapping("/trainingView/add)")
  public TrainingEntity addTasksToTraining(@RequestBody TrainingEntity training, @RequestBody List<TaskEntity> tasks) {
    for (TaskEntity task: tasks) {
      training.addTask(task);
    }
    return trainingRepo.save(training);
  }

}
