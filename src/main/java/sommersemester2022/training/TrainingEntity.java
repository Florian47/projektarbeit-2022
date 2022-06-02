package sommersemester2022.training;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import sommersemester2022.person.UserEntity;
import sommersemester2022.task.TaskEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TrainingEntity ist die Entitätsklasse für ein Training und hält alle dafür notwendigen Eigenschaften (Attribute)
 * und Fähigkeiten (Methoden).
 * @author Tobias Esau, Alexander Kiehl
 */
@Entity
public class TrainingEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  private String name;

  @LazyCollection(LazyCollectionOption.FALSE)
  @ManyToMany(cascade = CascadeType.MERGE)
  private List<UserEntity> students;

  /**
   * gibt an, ob ein Training individuell erstellt wurde
   */
  private boolean individual;

  @LazyCollection(LazyCollectionOption.FALSE)
  @ManyToMany
  private List<TaskEntity> tasks;

  private int score;

  public TrainingEntity() {}

  public TrainingEntity(String name, List<UserEntity> students, boolean individual) {
    this.name = name;
    this.students = students;
    this.individual = individual;
  }

  public TrainingEntity(TrainingEntity that) {
      this(that.getName(),that.getStudents(),that.isIndividual());
  }

  public int getScore() {
    return this.score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public Integer getId(){
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isIndividual() {
    return individual;
  }

  public void setIndividual(boolean individual) {
    this.individual = individual;
  }

  public List<TaskEntity> getTasks() {
    return tasks;
  }

  public void setTasks(List<TaskEntity> tasks) {
    this.tasks = tasks;
  }

  public void addTask(TaskEntity task) {
    if (this.tasks == null) tasks = new ArrayList<>();
    this.tasks.add(task);
  }

  public void addStudent(UserEntity student) {
    if (this.students == null) students = new ArrayList<>();
    this.students.add(student);
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public List<UserEntity> getStudents() {
    return students;
  }

  public void setStudents(List<UserEntity> students) {
    this.students = students;
  }
}


