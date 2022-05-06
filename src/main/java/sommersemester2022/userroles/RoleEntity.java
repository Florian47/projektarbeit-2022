package sommersemester2022.userroles;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class RoleEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private UserRole name;

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

  public UserRole getName() {
    return this.name;
  }

  public void setName(UserRole name) {
    this.name = name;
  }
}

