package sommersemester2022.person;

import sommersemester2022.userroles.Role;
import java.util.Set;
import java.util.HashSet;


import javax.persistence.*;

@Entity
public class UserEntity {
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(	name = "user_roles",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();
  @Id
  @GeneratedValue
  private long id;

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

  public Set<Role> getRoles() {
    return roles;
  }
  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  private String firstName;
  private String lastName;
  @Column(unique = true)
  private String username;
  private String password;

  public UserEntity(String firstName, String lastName, String username, String password) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.password = password;
  }

  public UserEntity() {}

}
