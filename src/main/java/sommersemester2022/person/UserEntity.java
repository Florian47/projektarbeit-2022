package sommersemester2022.person;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import sommersemester2022.userroles.RoleEntity;
import sommersemester2022.userroles.UserRole;

import javax.persistence.*;
import java.util.*;

/**
 * UserEntity ist die Entitätsklasse für den User und hält alle dafür notwendigen
 * Eigenschaften (Attribute) und Fähigkeiten (Methoden).
 * @author Florian Weinert, David Wiebe
 */
@Entity
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;
  private String firstName;
  private String lastName;

  /**
   * Usernamen dürfen nicht doppelt vergeben werden (daher unique)
   */
  @Column(unique = true)
  private String username;
  private String password;
  private UserRole role;

  /**
   * Das Attribut hält alle User-Rollen, welche ein User besitzt.
   */
  @LazyCollection(LazyCollectionOption.FALSE)
  @ManyToMany
//  @Cascade(org.hibernate.annotations.CascadeType.ALL)
  public List<RoleEntity> roles = new ArrayList<RoleEntity>();

  public UserEntity(String firstName, String lastName, String username, String password, List<RoleEntity> roles) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.password = password;
    this.roles = roles;
  }

  public UserEntity() {}

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setRole(UserRole role) {
    this.role = role;
  }
  public UserRole getRole(){return this.role;}

  public List<RoleEntity> getRoles() {
    return this.roles;
  }

  public void setRoles(List<RoleEntity> role) {
    this.roles = role;
  }
}
