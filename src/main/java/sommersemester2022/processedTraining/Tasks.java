package sommersemester2022.processedTraining;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import java.util.List;

@Embeddable
public class Tasks {

  @ElementCollection
  List<Gaps> tasks;
}
