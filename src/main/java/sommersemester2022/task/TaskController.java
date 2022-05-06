package sommersemester2022.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {
  @Autowired
  private TaskRepo taskRepo;

  @PostMapping("/aufgabe/register")
  public TaskEntity createTask(@RequestBody TaskEntity aufgabe) {
    return taskRepo.save(aufgabe);
  }

  @GetMapping("/users/{id}")
  public TaskEntity getById(@PathVariable int id) {
    return taskRepo.findById(id).get();
  }

  /*@PutMapping("/users/{id}")
  public TaskEntity updateUser(@PathVariable int id, @RequestBody TaskEntity task) {
    task.setId(id);
    return taskRepo.save(task);
  }*/

  @DeleteMapping("/users/{id}")
  public void deleteTask(@PathVariable int id) {
    taskRepo.deleteById(id);
  }

  @GetMapping("/users")
  public List<TaskEntity> getAll() {
    return taskRepo.findAll();
  }

}
