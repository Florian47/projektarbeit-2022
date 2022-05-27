package sommersemester2022.solution;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.stereotype.Service;
import sommersemester2022.processedTraining.ProcessedTrainingEntity;
import sommersemester2022.task.NotUniqueIdentification;
import sommersemester2022.task.TaskEntity;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Entity
public class SolutionEntity implements NotUniqueIdentification {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @LazyCollection(LazyCollectionOption.FALSE)
  @OneToMany(cascade = CascadeType.ALL)
  private List<SolutionGaps> solutionGaps;
  private String hint;
  @OneToOne(mappedBy = "solution",cascade = CascadeType.MERGE)
  private TaskEntity relatedTask;
  private String notUniqueId;

  public SolutionEntity() {}
  public SolutionEntity(List<SolutionGaps> solutionGaps, String hint) {
    this.solutionGaps = solutionGaps;
    this.hint = hint;
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
    return this.id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public List<SolutionGaps> getSolutionGaps() {
    return solutionGaps;
  }

  public void setSolutionGaps(List<SolutionGaps> solutionGaps) {
    this.solutionGaps = solutionGaps;
  }

  public String getHint() {
    return hint;
  }

  public void setHint(String hint) {
    this.hint = hint;
  }

  public int getMaxScore()
  {
    int maxScore=0;
    for(SolutionGaps s : solutionGaps)
    {
      int a = solutionGaps.indexOf(s);
      for (SolutionOptions q : s.getSolutionOptions())
      {
        int b = s.getSolutionOptions().indexOf(q);
        if (getSolutionGaps().get(a).getSolutionOptions().get(b).isRightAnswer()== true)  {
            maxScore++;
          }
        }
      }
    return maxScore;
  }
}

