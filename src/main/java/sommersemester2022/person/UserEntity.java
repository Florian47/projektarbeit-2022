package sommersemester2022.person;

import sommersemester2022.userroles.RoleEntity;
import sommersemester2022.userroles.UserRole;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;
  private String firstName;
  private String lastName;
  @Column(unique = true)
  private String username;
  private String password;
  @Enumerated(EnumType.STRING)
  private UserRole role;
  @ManyToMany
  private Set<RoleEntity> roles = new HashSet<>();

  public UserEntity(String firstName, String lastName, String username, String password, UserRole role) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.password = password;
    this.role = role;
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

  public UserRole getRole() {
    return this.role;
  }

  public void setRole(UserRole role) {
    this.role = role;
  }

  public Set<RoleEntity> getRoles() {
    return this.roles;
  }

  public void setRoles(Set<RoleEntity> roles) {
    this.roles = roles;
  }
}
