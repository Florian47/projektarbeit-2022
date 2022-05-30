package sommersemester2022.processedTraining;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sommersemester2022.solution.SolutionGaps;
import sommersemester2022.solution.SolutionOptions;
import sommersemester2022.task.NotUniqueIdentification;
import sommersemester2022.task.TaskEntity;
import sommersemester2022.training.HibernateProxyTypeAdapter;
import sommersemester2022.training.TrainingEntity;
import sommersemester2022.training.TrainingRepo;

import javax.persistence.PrePersist;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Transactional
@RestController
public class ProcessedTrainingController {
  @Autowired
  private ProcessedTrainingRepo processedTrainingRepo;

  @Autowired
  private TrainingRepo trainingRepo;
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
  @PreAuthorize("hasRole('ROLE_TEACHER')")
  @PutMapping("/processedTraining/{id}")
  public ProcessedTrainingEntity update(@PathVariable int id, @RequestBody ProcessedTrainingEntity processedTraining) {
    processedTraining.setId(id);
    return processedTrainingRepo.save(processedTraining);
  }

  @GetMapping("/processedTrainings")
  public List<ProcessedTrainingEntity> getAll() {
    return processedTrainingRepo.findAll();
  }


  @GetMapping("/generateProcessedTraining/{id}")
  public ProcessedTrainingEntity createProcessedTraining(@PathVariable int id) throws JsonProcessingException {
    ProcessedTrainingEntity processedTraining = new ProcessedTrainingEntity();
    TrainingEntity training = trainingRepo.getById(id);
    processedTraining.setOriginTraining(training);
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
    Gson gson = gsonBuilder.create();
    TrainingEntity copyTraining = gson.fromJson(gson.toJson(training), TrainingEntity.class);
    for (TaskEntity task : copyTraining.getTasks()
    ) {

      task.setId(null);
      task.getSolution().setId(null);
      task.setIndividual(true);
      for (SolutionGaps gap : task.getSolution().getSolutionGaps()
      ) {
        gap.setId(null);
        for (SolutionOptions option :
          gap.getSolutionOptions()) {
          option.setId(null);
          option.setCheckedAnswer(false);
        }
      }
    }
    processedTraining.setProcessedSolutionTasks(copyTraining.getTasks());
    return processedTrainingRepo.save(processedTraining);
  }
  @PrePersist
  @PostMapping("/evaluate/ProcessedTraining")
  public ProcessedTrainingEntity evaluateProcessedTraining(@RequestBody ProcessedTrainingEntity processedTraining) {

    TrainingEntity teacherTraining = processedTraining.getOriginTraining();

    processedTraining.getProcessedSolutionTasks()
      .forEach(studentTask -> evaluateTask(studentTask, find(studentTask, teacherTraining.getTasks())));

    //evaluate max training score
    int sum = teacherTraining.getTasks().stream().mapToInt(TaskEntity::getScore).sum();
    teacherTraining.setScore(sum);
    trainingRepo.saveAndFlush(teacherTraining);
    //trainingRepo.save(teacherTraining);
    //evaluate reached training score
    int sum1 = processedTraining.getProcessedSolutionTasks().stream().mapToInt(TaskEntity::getScore).sum();
    processedTraining.setScore(sum1);

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
    student.setScore(teacher.getSolution().getSolutionGaps().stream().mapToInt(teacherGap -> evaluateGap(teacherGap, find(teacherGap, student.getSolution().getSolutionGaps()))).sum());
  }

  private int evaluateGap(SolutionGaps teacherGap, SolutionGaps studentGap) {
    boolean allOptionsMatch = teacherGap.getSolutionOptions().stream()
      .allMatch(teacherOption -> find(teacherOption, studentGap.getSolutionOptions()).isCheckedAnswer() == teacherOption.isCheckedAnswer());
    return allOptionsMatch ? 1 : 0;
  }

  private <T extends NotUniqueIdentification> T find(T t1, List<T> list) {
    //TODO: improve exception//Was not able to identifiy T.getClass.getSimpleName() with uniqueName ... in list  {...}
    return list.stream().filter(t2 -> Objects.equals(t1.getNotUniqueId(), t2.getNotUniqueId())).findAny().orElseThrow(() -> new RuntimeException("Was not able to find %s in list".formatted(t1.getNotUniqueId())));
  }

}
