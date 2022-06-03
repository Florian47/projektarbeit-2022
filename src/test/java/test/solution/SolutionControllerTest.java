package test.solution;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import sommersemester2022.solution.SolutionEntity;
import sommersemester2022.solution.SolutionGaps;
import sommersemester2022.solution.SolutionOptions;
import sommersemester2022.training.TrainingEntity;
import test.BaseTest;
import java.util.ArrayList;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author David Wiebe
 * Das ist die Testklasse für die SolutionContorller Klasse.
 * @see sommersemester2022.solution.SolutionController
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SolutionControllerTest extends BaseTest {

  @Test
  public void testCreateSolution() throws Exception {
    String json = objectMapper.writeValueAsString(solution1);
    ResponseEntity<String> result =  restAuthPost("/solution/add", json, getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    List<SolutionEntity> entities = loadAll(SolutionEntity.class);
    assertThat(entities.size()).isEqualTo(1);
    SolutionEntity pe = entities.get(0);
    assertThat(pe.getId()).isGreaterThanOrEqualTo(1);
    assertThat(pe.getSolutionGaps().get(0).getSolutionOptions().get(0).getOptionName()).isEqualTo("Montag");
  }

  @Test
  public void testGetById() throws Exception {
    ResponseEntity<String> result = restAuthGet("/solution/" + solution1.getId(), getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    List<SolutionEntity> list = loadAll((SolutionEntity.class));
    assertThat(list.size()).isEqualTo(1);
    assertThat(list.get(0).getHint().equals("Lese dir den Satz in gedanken einmal laut vor"));

  }
  @Test
  public void testGetByFalseId() throws Exception {
    ResponseEntity<String> result = restAuthGet("/solution/" + "hint", getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    List<SolutionEntity> list = loadAll((SolutionEntity.class));
    assertThat(list.size()).isEqualTo(1);
  }

  @Test
  public void testUpdateSolution() throws Exception {
    List<SolutionGaps> gapsList1 = new ArrayList<>();
    List<SolutionOptions> optionsList1 = new ArrayList<>();
    optionsList1.add(new SolutionOptions("Schlecht", false));
    optionsList1.add(new SolutionOptions("Müde", true));
    optionsList1.add(new SolutionOptions("gut", true));
    optionsList1.add(new SolutionOptions("Herforragend", false));
    gapsList1.add(new SolutionGaps(optionsList1));
    SolutionEntity solution2 = new SolutionEntity(gapsList1, "Wie fühlst du dich?");

    ResponseEntity<String> result = restGet("/solution/" + solution1.getId());
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    solution1.setHint(solution2.getHint());
    String json = objectMapper.writeValueAsString(solution1);
    result = restAuthPost("/solution/add", json, getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    List<SolutionEntity> entities = loadAll(SolutionEntity.class);
    assertThat(entities.size()).isEqualTo(1);
    SolutionEntity pe = entities.get(0);
    assertThat(pe).isNotNull();
    assertThat(entities.get(0).getHint().equals("Wie fühlst du dich?"));

  }
//  @Test
//  public void testDeleteTraining() throws Exception{
//    ResponseEntity<String> result = restAuthDel("/solution/delete" + solution1.getId(), getJWTToken("admin"));
//    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
//    List<SolutionEntity> list = loadAll((SolutionEntity.class));
//    assertThat(list.size()).isEqualTo(0);
//
//  }

  @Test
  public void testGetAll() throws Exception {
    ResponseEntity<String> result = restGet("/solutions");
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    List<SolutionEntity> list = objectMapper.readValue(result.getBody(), new TypeReference<>() {
    });
    assertThat(list.size()).isEqualTo(1);
    assertThat(list.get(0).getHint().equals("Lese dir den Satz in gedanken einmal laut vor"));
  }
}
