package sommersemester2022.processedTraining;

import javax.persistence.*;
import java.util.List;

@Entity
public class Gaps {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @ElementCollection
  List<Boolean> gapsBooleans;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
