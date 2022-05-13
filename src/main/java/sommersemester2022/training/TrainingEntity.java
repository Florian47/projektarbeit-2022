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
  @OneToMany
  private List<UserEntity> students;
  private boolean individual;
  @ManyToMany
  private List<TaskEntity> tasks;
  @ManyToOne
  private UserEntity creator;

  public TrainingEntity() {}

  public TrainingEntity(List<UserEntity> students, boolean individual, UserEntity creator) {
    this.students = students;
    this.individual = individual;
    this.creator = creator;
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


