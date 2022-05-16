package test.training;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sommersemester2022.person.UserEntity;
import sommersemester2022.training.TrainingEntity;
import test.BaseTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TrainingControllerTest extends BaseTest {

  List<UserEntity> studentList = new ArrayList<>();


  @Test
  public void testAddTraining() throws Exception {
    String json = objectMapper.writeValueAsString(new TrainingEntity("Training1",studentList,false));
    ResponseEntity<String> result = restPost("/training/add", json);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    List<TrainingEntity> entities = loadAll(TrainingEntity.class);
    assertThat(entities.size()).isEqualTo(1);
    TrainingEntity pe = entities.get(0);
    assertThat(pe.getId()).isGreaterThanOrEqualTo(1);

  }
}
