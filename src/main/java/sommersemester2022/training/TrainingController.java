package sommersemester2022.training;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sommersemester2022.person.UserEntity;
import sommersemester2022.person.UserRepo;
import sommersemester2022.processedTraining.ProcessedTrainingEntity;
import sommersemester2022.processedTraining.ProcessedTrainingRepo;
import sommersemester2022.solution.SolutionEntity;
import sommersemester2022.solution.SolutionGaps;
import sommersemester2022.solution.SolutionOptions;
import sommersemester2022.task.NotUniqueIdentification;
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
  @PreAuthorize("hasRole('ROLE_TEACHER')")
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
  @PreAuthorize("hasRole('ROLE_TEACHER')")
  @DeleteMapping("/training/{id}")
  public void deleteTraining(@PathVariable int id) {
    trainingRepo.deleteById(id);
  }

  @GetMapping("/training/schueler/{id}")
  public List<TrainingEntity> getAllTrainingsForStudent(@PathVariable int id) {
    Optional<UserEntity> user = userRepo.findById(id);
    return trainingRepo.findByStudents(user).get();
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

  @PostMapping("/generateProcessedTraining/{id}")
  public ProcessedTrainingEntity createProcessedTraining(@RequestBody TrainingEntity training) {
    ProcessedTrainingEntity processedTraining = new ProcessedTrainingEntity();
    processedTraining.setOriginTraining(training);
    //TODO: deep!!! copy before changing id values
    for (TaskEntity task : training.getTasks()
    ) {
      task.setId(null);
      task.getSolution().setId(null);
      for (SolutionGaps gap : task.getSolution().getSolutionGaps()
      ) {
        gap.setId(null);
        for (SolutionOptions option :
          gap.getSolutionOptions()) {
          option.setId(null);
          option.setRightAnswer(false);
        }
      }
    }
    processedTraining.setProcessedSolutionTasks(training.getTasks());
    return processedTrainingRepo.save(processedTraining);
  }

  @PostMapping("/evaluate/ProcessedTraining/{id}")
  public ProcessedTrainingEntity evaluateProcessedTraining(@RequestBody ProcessedTrainingEntity processedTraining) {

    TrainingEntity teacherTraining = processedTraining.getOriginTraining();

    processedTraining.getProcessedSolutionTasks()
      .forEach(task -> evaluateTask(task, find(task, teacherTraining.getTasks())));

    //evaluate max training score
    teacherTraining.setScore(teacherTraining.getTasks().stream().mapToInt(TaskEntity::getScore).sum());
    //evaluate reached training score
    processedTraining.setScore(processedTraining.getProcessedSolutionTasks().stream().mapToInt(TaskEntity::getScore).sum());

    return processedTrainingRepo.save(processedTraining);
  }

  /**
   * every correct gap provides 1 point. A gap is correct if every option is identical to teacher option.
   * @param student
   * @param teacher
   */
  private void evaluateTask(TaskEntity student, TaskEntity teacher) {
    //evaluate max task score
    teacher.setScore(teacher.getSolution().getSolutionGaps().size());
    //evaluate reached task score
    student.setScore(teacher.getSolution().getSolutionGaps().stream().mapToInt(gap -> evaluateGap(gap, find(gap, student.getSolution().getSolutionGaps()))).sum());
  }

  private int evaluateGap(SolutionGaps teacherGap, SolutionGaps studentGap) {
    boolean allOptionsMatch = teacherGap.getSolutionOptions().stream()
      .filter(SolutionOptions::isRightAnswer)
      .allMatch(teacherOption -> find(teacherOption, studentGap.getSolutionOptions()).isRightAnswer() == teacherOption.isRightAnswer());
    return allOptionsMatch ? 1 : 0;
  }

  private <T extends NotUniqueIdentification> T find(T t1, List<T> list) {
    //TODO: improve exception
    return list.stream().filter(t2 -> t1.getNotUniqueId() == t2.getNotUniqueId()).findAny().orElseThrow(() -> new RuntimeException("Was not able to find %s in list".formatted(t1.getNotUniqueId())));
  }

}

