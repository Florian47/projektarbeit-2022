package sommersemester2022.processedTraining;

import javax.persistence.*;
import java.util.List;

@Entity
public class Tasks {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @OneToMany
  List<Gaps> gaps;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

}
