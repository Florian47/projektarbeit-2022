package example.springbootdemoserver.person;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "person")
public class PersonEntity {
  @Id
  @GeneratedValue
  private Long id = null;
  private Instant createTs = Instant.now();
  private String firstName;
  private String lastName;

  public PersonEntity(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public PersonEntity() {}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Instant getCreateTs() {
    return createTs;
  }

  public void setCreateTs(Instant createTs) {
    this.createTs = createTs;
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
}
