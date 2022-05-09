package sommersemester2022.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sommersemester2022.person.UserEntity;
import sommersemester2022.training.TrainingEntity;

import java.util.List;

@RestController
public class TaskController {
  @Autowired
  private TaskRepo taskRepo;

  @PostMapping("/taskView/add")
  public TaskEntity createTask(@RequestBody TaskEntity task) {
    return taskRepo.save(task);
  }

  @GetMapping("/users/{id}")
  public TaskEntity getById(@PathVariable int id) {
    return taskRepo.findById(id).get();
  }

  @PutMapping("/taskView/edit/{id}")
  public TaskEntity updateTask(@PathVariable int id, @RequestBody TaskEntity task) {
    task.setId(id);
    return taskRepo.save(task);
  }

  @PutMapping("/trainingView/edit/{id}")
  public TaskEntity updateTaskInTraining(@PathVariable int id, @RequestBody TaskEntity task) {
    task.setId(id);
    return taskRepo.save(task);
  }
  @GetMapping("/taskView")
  public List<TaskEntity> getAll() {
    return taskRepo.findAll();
  }

  @DeleteMapping("/users/{id}")
  public void deleteTask(@PathVariable int id) {
    taskRepo.deleteById(id);
  }

}
