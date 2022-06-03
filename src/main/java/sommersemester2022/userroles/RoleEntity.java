package sommersemester2022.userroles;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author David Wiebe, Tobias Esau
 * Die Klass RoleEntity ist die Entityklasse für die Rollen des Benutzers. Hier wurden alle Attribute und
 * notwendige Getter und Setter Methoden implementiert.
 */
@Entity
@Table(name = "roles")
public class RoleEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  /**
   * Die UserRolen mit dem Hinweis Enumeration
   * @see UserRole
   */
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



