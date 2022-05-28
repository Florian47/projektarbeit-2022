package sommersemester2022.solution;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.stereotype.Service;
import sommersemester2022.task.NotUniqueIdentification;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
public class SolutionGaps implements NotUniqueIdentification {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @LazyCollection(LazyCollectionOption.FALSE)
  @OneToMany(cascade = CascadeType.ALL)
  private List<SolutionOptions> solutionOptions;
  private String notUniqueId;

  public SolutionGaps() {}
  public SolutionGaps(List<SolutionOptions> solutionOptions) {
    this.solutionOptions = solutionOptions;
  }

  @PrePersist
  private void generateRandomNotUniqueId(){
    if(this.notUniqueId == null) this.notUniqueId = UUID.randomUUID().toString();
  }
  @Override
  public String getNotUniqueId() {
    return notUniqueId;
  }
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }


  public List<SolutionOptions> getSolutionOptions() {
    return solutionOptions;
  }

  public void setSolutionOptions(List<SolutionOptions> solutionOptions) {
    this.solutionOptions = solutionOptions;
  }
}
