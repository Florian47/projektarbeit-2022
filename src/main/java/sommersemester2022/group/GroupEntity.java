package sommersemester2022.group;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import sommersemester2022.person.UserEntity;
import sommersemester2022.userroles.RoleEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class GroupEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;
  private String name;
  @ManyToMany
  private List<UserEntity> students;

  public GroupEntity(String name) {
    this.name = name;
  }

  public GroupEntity() {}

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<UserEntity> getStudents() {
    return students;
  }

  public void setStudents(List<UserEntity> students) {
    this.students = students;
  }
}
