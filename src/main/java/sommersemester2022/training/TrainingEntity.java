package sommersemester2022.training;

import sommersemester2022.person.UserEntity;
import sommersemester2022.task.TaskEntity;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
public class TrainingEntity {

  @Id
  @GeneratedValue
  private int id;
  private String name;
  @OneToMany
  private List<UserEntity> students;
  private boolean individual;
  @ManyToMany
  private List<TaskEntity> tasks;

  public TrainingEntity() {}

  public TrainingEntity(String name, List<UserEntity> students, boolean individual) {
    this.name = name;
    this.students = students;
    this.individual = individual;
  }

  public int getId() {
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
    this.tasks.add(task);
  }

  public void addStudent(UserEntity student) {
    this.students.add(student);
  }

  public void setId(int id) {
    this.id = id;
  }

  public List<UserEntity> getStudents() {
    return students;
  }

  public void setStudents(List<UserEntity> students) {
    this.students = students;
  }
}


