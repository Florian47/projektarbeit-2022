package sommersemester2022.task;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

public abstract class NotUniqueIdentification {
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int notUniqueId;


  public int getNotUniqueId() {
    return notUniqueId;
  }

  public void setNotUniqueId(int notUniqueId) {
    this.notUniqueId = notUniqueId;
  }
}
