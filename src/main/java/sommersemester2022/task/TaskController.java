package sommersemester2022.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {
  @Autowired
  private TaskRepo taskRepo;
  @PreAuthorize("hasRole('ROLE_TEACHER')")
  @PostMapping("/task/add")
  public TaskEntity addTask(@RequestBody TaskEntity task) {
    return taskRepo.save(task);
  }
  @PreAuthorize("hasRole('ROLE_TEACHER')")
  @GetMapping("/task/{id}")
  public TaskEntity getTaskById(@PathVariable int id) {
    return taskRepo.findById(id).get();
  }
  @PreAuthorize("hasRole('ROLE_TEACHER')")
  @PutMapping("/task/edit/{id}")
  public TaskEntity updateTask(@PathVariable int id, @RequestBody TaskEntity task) {
    task.setId(id);
    return taskRepo.save(task);
  }
  @PreAuthorize("hasRole('ROLE_TEACHER')")
  @GetMapping("/task")
  public List<TaskEntity> getAll() {
    return taskRepo.findAll();
  }
  @PreAuthorize("hasRole('ROLE_TEACHER')")
  @DeleteMapping("/task/{id}")
  public void deleteTask(@PathVariable int id) {
    taskRepo.deleteById(id);
  }
  @PreAuthorize("hasRole('ROLE_TEACHER')")
  public List<TaskEntity> getAllTasksForGeneratedTraining(TaskCategory category, TaskDifficulty difficulty) {
    return taskRepo.findAllByCategoryAndDifficulty(category, difficulty);
  }
}
