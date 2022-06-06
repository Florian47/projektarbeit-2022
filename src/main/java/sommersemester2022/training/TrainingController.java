package sommersemester2022.training;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sommersemester2022.person.UserEntity;
import sommersemester2022.person.UserRepo;
import sommersemester2022.processedTraining.ProcessedTrainingEntity;
import sommersemester2022.processedTraining.ProcessedTrainingRepo;
import sommersemester2022.task.TaskEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * TrainingController ist die Controller-Klasse für die Entität des Trainings.
 * Sie kontrolliert alle Aktivitäten, welche mit den Trainings ausgeführt werden können und gibt die
 * Informationen an das Repository weiter.
 *
 * @author Tobias Esau, Florian Weinert
 * @see TrainingRepo
 */
@RestController
public class TrainingController {
  @Autowired
  private TrainingRepo trainingRepo;
  @Autowired
  private UserRepo userRepo;
  @Autowired
  private ProcessedTrainingRepo processedTrainingRepo;

  /**
   * Erstellt ein neues Training.
   *
   * @param training Frontend Daten für das Training
   * @return erstelltes Training
   */
  @PreAuthorize("hasRole('ROLE_TEACHER')")
  @PostMapping("/training/add")
  public TrainingEntity createTraining(@RequestBody TrainingEntity training) {
    return trainingRepo.save(training);
  }

  /**
   * Gibt das Training mit der angegebenen ID aus der Datenbank zurück.
   *
   * @param id ID des Training
   * @return Training mit der angegebenen ID
   */
  @GetMapping("/training/{id}")
  public TrainingEntity getById(@PathVariable int id) {
    return trainingRepo.findById(id).get();
  }

  /**
   * Gibt alle Trainings aus der Datenbank zurück.
   *
   * @return Liste aller im System existierenden Trainings
   */
  @GetMapping("/training")
  public List<TrainingEntity> getAll() {
    return trainingRepo.findAll();
  }

  /**
   * Gibt alle Trainings zurück, welche von einem Dozenten erstellt wurden. Nur diese erscheinen in der Trainingsübersicht,
   * nicht die auto-generierten des Schülers.
   *
   * @return Liste aller Trainings, die von einem Dozenten erstellt wurden.
   */
  @GetMapping("/training/individuell")
  public List<TrainingEntity> getAllManuelTrainings() {
    return trainingRepo.findByIndividualTrue();
  }

  /**
   * Löscht das Training mit der angegebenen ID.
   *
   * @param id ID des Trainings
   */
  @PreAuthorize("hasRole('ROLE_TEACHER')")
  @DeleteMapping("/training/{id}")
  public void deleteTraining(@PathVariable int id) {
    List<ProcessedTrainingEntity> entities;
    entities = processedTrainingRepo.findAll();
    List<ProcessedTrainingEntity> relevantEntities = new ArrayList<>();
    int x = entities.size();
    for (int i = 0; i<x;i++)
    {
      if(entities.get(i).getOriginTraining().getId()==id) {
        processedTrainingRepo.delete(entities.get(i));
      }
    }
    trainingRepo.deleteById(id);
  }

  /**
   * Gibt alle Trainings für den jeweiligen Schüler mit der angegebenen ID zurück.
   *
   * @param id ID des Schülers
   * @return Liste aller Trainings für den Schüler
   */
  @GetMapping("/training/schueler/{id}")
  public List<TrainingEntity> getAllTrainingsForStudent(@PathVariable int id) {
    Optional<UserEntity> user = userRepo.findById(id);
    List<TrainingEntity> allAllowedTrainings = trainingRepo.findByStudentsAndIndividualTrue(user).get();
    List<ProcessedTrainingEntity> processedTrainings = processedTrainingRepo.findByStudentId(id);
    /**
     * Der folgende Ausdruck filtert alle Trainings für den Benutzer auf die, welche nicht bearbeitet wurden, damit
     * diese nicht mehr in der Trainingsübersicht angezeigt werden.
     */
    return allAllowedTrainings
      .stream()
      .filter(training -> processedTrainings
        .stream()
        .noneMatch(processedTraining -> Objects.equals(training.getId(), processedTraining.getOriginTraining().getId())))
      .collect(Collectors.toList());
  }

  /**
   * Gibt alle Trainings zurück, welche mindestens einmal bearbeitet wurden (für die Auswertung benötigt).
   * @return Liste der Trainings, die mindestens einmal bearbeitet wurden
   */
  @GetMapping("/training/processed")
  public List<TrainingEntity> getAllProcessedTrainings() {
    List<TrainingEntity> allTrainings = trainingRepo.findByIndividualTrue();
    List<ProcessedTrainingEntity> allProcessedTrainings = processedTrainingRepo.findAll();
    /**
     * Der folgende Ausdruck filtert alle existierenden Trainings auf die, welche min. einmal bearbeitet wurden, damit
     * diese in der Auswertung angezeigt werden können.
     */
    return allTrainings
      .stream()
      .filter(training -> allProcessedTrainings
        .stream()
        .anyMatch(processedTraining -> Objects.equals(training.getId(), processedTraining.getOriginTraining().getId())))
      .collect(Collectors.toList());
  }

  /**
   * Gibt für ein Training alle freigegebenen Schüler zurück.
   *
   * @param training Frontend Daten des Trainings
   * @return Liste aller User, die für das Training freigegeben sind.
   */
  @GetMapping("/schueler/all")
  public List<UserEntity> getAllUsersForTraining(@RequestBody TrainingEntity training) {
    return training.getStudents();
  }

  /**
   * Fügt zu einem bestehenden Training Aufgaben hinzu.
   *
   * @param training Vorhandenes Training
   * @param tasks    Liste der hinzuzufügenden Aufgaben
   * @return Training mit aktualisierter Aufgabenliste
   */
  @PreAuthorize("hasRole('ROLE_TEACHER')")
  @PutMapping("/training/add")
  public TrainingEntity addTasksToTraining(@RequestBody TrainingEntity training, @RequestBody List<TaskEntity> tasks) {
    for (TaskEntity task : tasks) {
      training.addTask(task);
    }
    return trainingRepo.save(training);
  }

  /**
   * Bearbeitet das Training mit der angegebenen ID.
   *
   * @param id       ID des Trainings
   * @param training Frontend Daten des Trainings
   * @return aktualisiertes Training
   */
  @PreAuthorize("hasRole('ROLE_TEACHER')")
  @PutMapping("/training/edit/{id}")
  public TrainingEntity editTraining(@PathVariable int id, @RequestBody TrainingEntity training) {
    training.setId(id);
    return trainingRepo.save(training);
  }
}

