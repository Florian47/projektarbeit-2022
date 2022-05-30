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

/**
 * ProcessedTrainingController is the control class for the processed training entity. It handles all activities that
 * can be done with completed trainings and gives information to the repository which saves the activities.
 * (e.g. all CRUD-Operations)
 * @author Tobias Esau, Alexander Kiehl
 * @see    ProcessedTrainingRepo
 */
@Transactional
@RestController
public class ProcessedTrainingController {
  @Autowired
  private ProcessedTrainingRepo processedTrainingRepo;

  @Autowired
  private TrainingRepo trainingRepo;

  /**
   * Creates a new processed training.
   * @param processedTraining - frontend data
   * @return processed training
   */
  @PreAuthorize("hasRole('ROLE_TEACHER')")
  @PostMapping("/processedTraining/add")
  public ProcessedTrainingEntity createTraining(@RequestBody ProcessedTrainingEntity processedTraining) {
    return processedTrainingRepo.save(processedTraining);
  }

  /**
   * Returns the processed training found by the given id.
   * @param id - training id
   * @return processed training
   */
  @GetMapping("/processedTraining/{id}")
  public ProcessedTrainingEntity getById(@PathVariable int id) {
    return processedTrainingRepo.findById(id).get();
  }

  /**
   * Tells the repository to delete the processed training with the given id.
   * @param id - training id
   */
  @PreAuthorize("hasRole('ROLE_TEACHER')")
  @DeleteMapping("/processedTraining/delete/{id}")
  public void deleteProcessedTraining(@PathVariable int id) {
    processedTrainingRepo.deleteById(id);
  }

  /**
   * Returns the updated processed training.
   * @param id - training id
   * @return processed training
   */
  @PreAuthorize("hasRole('ROLE_TEACHER')")
  @PutMapping("/processedTraining/{id}")
  public ProcessedTrainingEntity update(@PathVariable int id, @RequestBody ProcessedTrainingEntity processedTraining) {
    processedTraining.setId(id);
    return processedTrainingRepo.save(processedTraining);
  }

  /**
   * Returns all processed trainings existing in the database
   * @return list of processed trainings
   */
  @GetMapping("/processedTrainings")
  public List<ProcessedTrainingEntity> getAll() {
    return processedTrainingRepo.findAll();
  }

  /**
   * Creates a new processed training if the student saves his passed training.
   * @param id - processed training id
   * @return created processed training
   * @throws JsonProcessingException - Exception
   */
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

  /**
   * Evaluates the training by using methods "evaluateTask" and "evaluateGap"
   * @param processedTraining - frontend data for processed training
   * @return evaluated processed training
   */
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
   * Evaluates a task of the processed training. Every correct gap provides 1 point. A gap is correct if every option is
   * identical to the teachers given optimal solution. Uses "evaluateGap".
   * @param student - whole task solution of student who passed the task
   * @param teacher - whole task solution of teacher who created the task and its solution
   */
  private void evaluateTask(TaskEntity student, TaskEntity teacher) {
    //evaluate max task score
    teacher.setScore(teacher.getSolution().getSolutionGaps().size());
    //evaluate reached task score
    student.setScore(teacher.getSolution().getSolutionGaps().stream().mapToInt(teacherGap -> evaluateGap(teacherGap, find(teacherGap, student.getSolution().getSolutionGaps()))).sum());
  }

  /**
   * Evaluates a single gap. Checks whether the solutions of the gaps are matching.
   * @param teacherGap - teachers solution of single gap in the task
   * @param studentGap - students solution of single gap in the task
   * @return int 1 or 0 to check if true or false
   */
  private int evaluateGap(SolutionGaps teacherGap, SolutionGaps studentGap) {
    boolean allOptionsMatch = teacherGap.getSolutionOptions().stream()
      .allMatch(teacherOption -> find(teacherOption, studentGap.getSolutionOptions()).isCheckedAnswer() == teacherOption.isCheckedAnswer());
    return allOptionsMatch ? 1 : 0;
  }

  private <T extends NotUniqueIdentification> T find(T t1, List<T> list) {
    //TODO: improve exception//Was not able to identify T.getClass.getSimpleName() with uniqueName ... in list  {...}
    return list.stream().filter(t2 -> Objects.equals(t1.getNotUniqueId(), t2.getNotUniqueId())).findAny().orElseThrow(() -> new RuntimeException("Was not able to find %s in list".formatted(t1.getNotUniqueId())));
  }

}
