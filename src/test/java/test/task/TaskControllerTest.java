package test.task;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sommersemester2022.person.UserEntity;
import sommersemester2022.solution.SolutionEntity;
import sommersemester2022.task.TaskCategory;
import sommersemester2022.task.TaskDifficulty;
import sommersemester2022.task.TaskEntity;
import sommersemester2022.userroles.UserRole;
import test.BaseTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskControllerTest extends BaseTest {

  @Test
  public void testAddTask() throws Exception {
    String json = objectMapper.writeValueAsString(new TaskEntity("Aufgabe 1", "Heute ist ...", "1234", 4, TaskCategory.GRAMMATIK, TaskDifficulty.HARD, new SolutionEntity()));
    ResponseEntity<String> result = restPost("/task/create", json);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    List<TaskEntity> entities = loadAll(TaskEntity.class);
    assertThat(entities.size()).isEqualTo(1);
    TaskEntity pe = entities.get(0);
    assertThat(pe.getId()).isGreaterThanOrEqualTo(1);
    assertThat(pe.getScore()).isEqualTo(4);
    assertThat(pe.getPicture()).isEqualTo("1234");
  }

}

