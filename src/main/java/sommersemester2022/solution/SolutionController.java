package sommersemester2022.solution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class SolutionController {
  @Autowired
  private SolutionRepo solutionRepo;

  @PreAuthorize("hasRole('ROLE_TEACHER')")
  @PostMapping("/solution/add")
  public SolutionEntity createSolution(@RequestBody SolutionEntity solution) {
    return solutionRepo.save(solution);
  }

  @GetMapping("/solution/{id}")
  public Optional<SolutionEntity> getById(@PathVariable int id) {
    return solutionRepo.findSolutionEntityByRelatedTask_Id(id);
  }
  @PreAuthorize("hasRole('ROLE_TEACHER')")
  @PutMapping("/solution/{id}")
  public SolutionEntity updateUser(@PathVariable int id, @RequestBody SolutionEntity solution) {
    solution.setId(id);
    return solutionRepo.save(solution);
  }
  @PreAuthorize("hasRole('ROLE_TEACHER')")
  @DeleteMapping("/solution/delete/{id}")
  public void deleteSolution(@PathVariable int id) {
    solutionRepo.deleteById(id);
  }

  @GetMapping("/solutions")
  public List<SolutionEntity> getAll() {
    return solutionRepo.findAll();
  }

}
