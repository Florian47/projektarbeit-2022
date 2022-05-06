package sommersemester2022.solution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SolutionController {
  @Autowired
  private SolutionRepo solutionRepo;

  @PostMapping("/aufgabe/register")
  public SolutionEntity createSolution(@RequestBody SolutionEntity solution) {
    return solutionRepo.save(solution);
  }

  @GetMapping("/users/{id}")
  public SolutionEntity getById(@PathVariable int id) {
    return solutionRepo.findById(id).get();
  }

  /*@PutMapping("/users/{id}")
  public TaskEntity updateUser(@PathVariable int id, @RequestBody TaskEntity task) {
    task.setId(id);
    return taskRepo.save(task);
  }*/

  @DeleteMapping("/users/{id}")
  public void deleteSolution(@PathVariable int id) {
    solutionRepo.deleteById(id);
  }

  @GetMapping("/users")
  public List<SolutionEntity> getAll() {
    return solutionRepo.findAll();
  }

}
