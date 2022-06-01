package sommersemester2022.solution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sommersemester2022.processedTraining.ProcessedTrainingRepo;

import java.util.List;
import java.util.Optional;

/**
 * SolutionController ist die Controller-Klasse für die Entität der Lösung für eine Aufgabe.
 * Sie kontrolliert alle Aktivitäten, welche mit den Lösungen ausgeführt werden können und gibt die
 * Informationen an das Repository weiter.
 * (z.B. alle CRUD-Operationen)
 * @author Tobias Esau, Alexander Kiehl
 * @see    SolutionRepo
 */
@RestController
public class SolutionController {
  @Autowired
  private SolutionRepo solutionRepo;

  /**
   * Erstellt eine neue Lösung für eine Aufgabe.
   * @param solution Frontend Daten für die Lösung
   * @return die erstellte Lösungs-Entität
   */
  @PreAuthorize("hasRole('ROLE_TEACHER')")
  @PostMapping("/solution/add")
  public SolutionEntity createSolution(@RequestBody SolutionEntity solution) {
    return solutionRepo.save(solution);
  }

  /**
   * Gibt die Lösung mit der jeweiligen ID aus der Datenbank zurück.
   * @param id ID der Lösung
   * @return optional eine Lösungs-Entität (bei Existenz in der Datenbank)
   */
  @GetMapping("/solution/{id}")
  public Optional<SolutionEntity> getById(@PathVariable int id) {
    return solutionRepo.findById(id);
  }

  /**
   * Bearbeitet die LÖösung einer Aufgabe.
   * @param id ID der Lösung
   * @param solution Frontend Daten für die Lösung
   * @return die bearbeitete Lösungs-Entität
   */
  @PreAuthorize("hasRole('ROLE_TEACHER')")
  @PutMapping("/solution/{id}")
  public SolutionEntity updateSolution(@PathVariable int id, @RequestBody SolutionEntity solution) {
    solution.setId(id);
    return solutionRepo.save(solution);
  }

  /**
   * Gibt dem Repository die Anweisung, die Lösung mit der angegebenen ID aus der Datenbank zu entfernen.
   * @param id ID der Lösung
   */
  @PreAuthorize("hasRole('ROLE_TEACHER')")
  @DeleteMapping("/solution/delete/{id}")
  public void deleteSolution(@PathVariable int id) {
    solutionRepo.deleteById(id);
  }

  /**
   * Gibt alle in der Datenbank vorhandenen Entitäten der Lösung in einer Liste zurück.
   * @return Liste mit den Lösungs-Entitäten
   */
  @GetMapping("/solutions")
  public List<SolutionEntity> getAll() {
    return solutionRepo.findAll();
  }
}
