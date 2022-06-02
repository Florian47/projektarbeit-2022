package sommersemester2022.processedTraining;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import sommersemester2022.person.UserEntity;
import sommersemester2022.security.services.UserDetailsImpl;
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
 * ProcessedTrainingController ist die Controller-Klasse für die Entität des bearbeiteten Trainings.
 * Sie kontrolliert alle Aktivitäten, welche mit den bearbeiteten Trainings ausgeführt werden können und gibt die
 * Informationen an das Repository weiter.
 * (z.B. alle CRUD-Operationen)
 * @author Tobias Esau, Alexander Kiehl, Florian Weinert
 * @see    ProcessedTrainingRepo
 */
@Transactional
@RestController
public class ProcessedTrainingController {
  @Autowired
  private ProcessedTrainingRepo processedTrainingRepo;

  @Autowired
  private TrainingRepo trainingRepo;

  //  @PreAuthorize("hasRole({'ROLE_TEACHER', 'ROLE_STUDENT'})")

  /**
   * Erstellt ein neues bearbeitetes Training
   * @param processedTraining Frontend Daten des bearbeiteten Trainings
   * @return bearbeitetes Training
   */
  @PostMapping("/processedTraining/add")
  public ProcessedTrainingEntity createTraining(@RequestBody ProcessedTrainingEntity processedTraining) {
    return processedTrainingRepo.save(processedTraining);
  }

  /**
   * Gibt das bearbeitete Training mit der angegebenen ID zurück.
   * @param id ID des bearbeiteten Trainings
   * @return bearbeitetes Training
   */
  @GetMapping("/processedTraining/{id}")
  public ProcessedTrainingEntity getById(@PathVariable int id) {
    return processedTrainingRepo.findById(id).get();
  }

  /**
   * Gibt die Information an das Repository, das bearbeitete Training mit der angegebenen ID zu löschen.
   * @param id ID des bearbeiteten Trainings
   */
  //  @PreAuthorize("hasRole('ROLE_TEACHER')")
  @DeleteMapping("/processedTraining/delete/{id}")
  public void deleteProcessedTraining(@PathVariable int id) {
    processedTrainingRepo.deleteById(id);
  }

  /**
   * Gibt das veränderte bearbeitete Training zurück.
   * @param id ID des bearbeiteten Trainings
   * @return bearbeitetes Training
   */
  //  @PreAuthorize("hasRole({'ROLE_TEACHER', 'ROLE_STUDENT'})")
  @PutMapping("/processedTraining/{id}")
  public ProcessedTrainingEntity update(@PathVariable int id, @RequestBody ProcessedTrainingEntity processedTraining) {
    int stdId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    processedTraining.setStudentId(stdId);
    processedTraining.setId(id);
    return processedTrainingRepo.save(processedTraining);
  }

  /**
   * Gibt alle in der Datenbank vorhandenen bearbeiteten Trainings zurück
   * @return Liste aller bearbeiteten Trainings
   */
  @GetMapping("/processedTrainings")
  public List<ProcessedTrainingEntity> getAll() {
    return processedTrainingRepo.findAll();
  }

  /**
   * Erstellt ein bearbeitetes Training, nachdem der Schüler das Training mit "Speichern" gesichert hat.
   * @param id ID des bearbeiteten Trainings
   * @return erstelltes bearbeitetes Training
   */
  //  @PreAuthorize("hasRole({'ROLE_TEACHER', 'ROLE_STUDENT'})")
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
   * Wertet das vom Schüler bearbeitete Training aus und nutzt dazu "evaluateTask" und "evaluateGap"
   * @param id ID des Trainings, welches ausgewertet werden soll
   * @return ausgewertetes bearbeitetes Training
   */
  @PrePersist
  @PostMapping("/evaluate/Training/{id}")
  public List<ProcessedTrainingEntity> evaluateProcessedTraining(@RequestParam Integer id) {
    //get all processedTrainings with originTraining.getId() == id
    // iterate over processed training list and evaluate ALL of them
    //return it
    return null;
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
   * Wertet eine gesamte Aufgabe des Trainings aus. Jede korrekte Lücke gibt einen Punkt. Eine Lücke ist korrekt, wenn
   * sie mit der Lösung des Lehrers übereinstimmt. Die Methode benutzt die Methode "evaluateGap".
   * @param student gesamte Aufgaben-Lösung des Schülers
   * @param teacher gesamte Aufgaben-Lösung (Musterlösung) des Lehrers
   */
  private void evaluateTask(TaskEntity student, TaskEntity teacher) {
    //evaluate max task score
    teacher.setScore(teacher.getSolution().getSolutionGaps().size());
    //evaluate reached task score
    student.setScore(teacher.getSolution().getSolutionGaps().stream().mapToInt(teacherGap -> evaluateGap(teacherGap, find(teacherGap, student.getSolution().getSolutionGaps()))).sum());
  }

  /**
   * Wertet eine einzelne Lücke aus, indem sie die Schüler-Lösung mit der Lehrer-Lösung abgleicht..
   * @param teacherGap Lehrer-Lösung (Musterlösung) der einzelnen Lücke in einer Aufgabe
   * @param studentGap Schüler-Lösung der einzelnen Lücke in einer Aufgabe
   * @return int 1 oder 0 als Wahrheitswert
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
