package sommersemester2022.training;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

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

  @GetMapping("/users")
  public List<TrainingEntity> getAll() {
    return trainingRepo.findAll();
  }

}
