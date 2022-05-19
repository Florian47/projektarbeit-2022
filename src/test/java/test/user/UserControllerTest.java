package test.user;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sommersemester2022.person.UserEntity;
import sommersemester2022.userroles.UserRole;
import test.BaseTest;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserControllerTest extends BaseTest {

  @Test
  public void testAddPerson() throws Exception {
    String json = objectMapper.writeValueAsString(new UserEntity("Tim", "Nord", "user1", "pass1", null));
    ResponseEntity<String> result = restPost("/users/register", json);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    List<UserEntity> entities = loadAll(UserEntity.class);
    assertThat(entities.size()).isEqualTo(1);
    UserEntity pe = entities.get(0);
    assertThat(pe.getId()).isGreaterThanOrEqualTo(1);
    assertThat(pe.getFirstName()).isEqualTo("Tim");
    assertThat(pe.getLastName()).isEqualTo("Nord");
  }

  //@Test
  public void testPersonList() throws Exception {
    String json = objectMapper.writeValueAsString(new UserEntity("Tim", "Nord", "user1", "pass1", null));
    restPost("/users/register", json);

    ResponseEntity<String> result = restGet("/users");
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    List<UserEntity> list = objectMapper.readValue(result.getBody(), new TypeReference<>() {});

    assertThat(list.size()).isEqualTo(1);
    UserEntity p = list.get(0);
    assertThat(p.getFirstName()).isEqualTo("Tim");
    assertThat(p.getLastName()).isEqualTo("Nord");
  }
}
