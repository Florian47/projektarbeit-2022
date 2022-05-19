package sommersemester2022.userroles;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "roles")
public class RoleEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Enumerated(EnumType.STRING)
  public UserRole name;

  public RoleEntity() {
  }

  public RoleEntity(UserRole name) {
    this.name = name;
  }

  public long getId() {
    return this.id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public  UserRole getName() {
    return this.name;
  }
  public void setName(UserRole name) {
    this.name = name;
  }
  }



