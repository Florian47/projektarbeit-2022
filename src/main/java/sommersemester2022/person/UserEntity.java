package sommersemester2022.person;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import sommersemester2022.security.services.UserDetailsImpl;
import sommersemester2022.userroles.RoleEntity;
import sommersemester2022.userroles.UserRole;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * UserEntity ist die Entitätsklasse für den User und hält alle dafür notwendigen
 * Eigenschaften (Attribute) und Fähigkeiten (Methoden).
 * @author Florian Weinert, David Wiebe
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonProperty("id")
  private int id;
  @JsonProperty("firstname")
  private String firstName;
  @JsonProperty("lastname")
  private String lastName;

  /**
   * Usernamen dürfen nicht doppelt vergeben werden (daher unique)
   */
  @Column(unique = true)
  @JsonProperty("username")
  private String username;
  @JsonProperty("password")
  private String password;

  /**
   * Das Attribut hält alle User-Rollen, welche ein User besitzt.
   */
  @LazyCollection(LazyCollectionOption.FALSE)
  @ManyToMany
  @JsonProperty("roles")
  public List<RoleEntity> roles = new ArrayList<>();

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

  public List<RoleEntity> getRoles() {
    return this.roles;
  }

  public void setRoles(List<RoleEntity> role) {
    this.roles = role;
  }
}
