package sommersemester2022.processedTraining;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProcessedTrainingController {
  @Autowired
  private ProcessedTrainingRepo processedTrainingRepo;
  @PreAuthorize("hasRole('ROLE_TEACHER')")
  @PostMapping("/processedTraining/add")
  public ProcessedTrainingEntity createTraining(@RequestBody ProcessedTrainingEntity processedTraining) {
    return processedTrainingRepo.save(processedTraining);
  }

  @GetMapping("/processedTraining/{id}")
  public ProcessedTrainingEntity getById(@PathVariable int id) {
    return processedTrainingRepo.findById(id).get();
  }
  @PreAuthorize("hasRole('ROLE_TEACHER')")
  @DeleteMapping("/processedTraining/delete/{id}")
  public void deleteProcessedTraining(@PathVariable int id) {
    processedTrainingRepo.deleteById(id);
  }

  @GetMapping("/processedTrainings")
  public List<ProcessedTrainingEntity> getAll() {
    return processedTrainingRepo.findAll();
  }

}
