package test.solution;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sommersemester2022.solution.SolutionEntity;
import sommersemester2022.solution.SolutionGaps;
import sommersemester2022.solution.SolutionOptions;
import sommersemester2022.task.TaskCategory;
import sommersemester2022.task.TaskDifficulty;
import sommersemester2022.task.TaskEntity;
import test.BaseTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SolutionControllerTest extends BaseTest {

  @Test
  public void testJSONSolution() throws Exception {
    SolutionEntity entity = new SolutionEntity();
    List<SolutionGaps> gapsList = new ArrayList<>();
    List<SolutionOptions> optionsList = new ArrayList<>();
    List<SolutionOptions> optionsList2 = new ArrayList<>();

    optionsList.add(new SolutionOptions("Montag", false));
    optionsList.add(new SolutionOptions("Dienstag", true));
    optionsList.add(new SolutionOptions("Mittwoch", true));
    optionsList.add(new SolutionOptions("Donnerstag", false));

    optionsList2.add(new SolutionOptions("Montag", false));
    optionsList2.add(new SolutionOptions("Dienstag", true));
    optionsList2.add(new SolutionOptions("Mittwoch", true));
    optionsList2.add(new SolutionOptions("Donnerstag", false));

    gapsList.add(new SolutionGaps(optionsList));
    gapsList.add(new SolutionGaps(optionsList2));

    entity.setSolutionGaps(gapsList);


    String json = objectMapper.writeValueAsString(entity);
    ResponseEntity<String> result = restPost("/solution/add", json);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    List<SolutionEntity> entities = loadAll(SolutionEntity.class);
    //assertThat(entities.size()).isEqualTo(1);
    SolutionEntity pe = entities.get(0);
    assertThat(pe.getId()).isGreaterThanOrEqualTo(1);
    assertThat(pe.getSolutionGaps().get(0).getSolutionOptions().get(0).getOptionName()).isEqualTo("Montag");

  }

}
