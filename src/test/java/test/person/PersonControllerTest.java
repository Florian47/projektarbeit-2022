package test.person;

import com.fasterxml.jackson.core.type.TypeReference;
import example.springbootdemoserver.person.PersonDto;
import example.springbootdemoserver.person.PersonEntity;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import test.BaseTest;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonControllerTest extends BaseTest {
  @Test
  public void testAddPerson() throws Exception {
    String json = objectMapper.writeValueAsString(new PersonDto("Tim", "Nord"));
    ResponseEntity<String> result = restPost("/api/person", json);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    List<PersonEntity> entities = loadAll(PersonEntity.class);
    assertThat(entities.size()).isEqualTo(1);
    PersonEntity pe = entities.get(0);
    assertThat(pe.getId()).isGreaterThanOrEqualTo(0);
    assertThat(pe.getCreateTs()).isAfter(Instant.now().minusMillis(3000));
    assertThat(pe.getFirstName()).isEqualTo("Tim");
    assertThat(pe.getLastName()).isEqualTo("Nord");
  }

  @Test
  public void testPersonList() throws Exception {
    String json = objectMapper.writeValueAsString(new PersonDto("Tim", "Nord"));
    restPost("/api/person", json);

    ResponseEntity<String> result = restGet("/api/person/list");
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    List<PersonDto> list = objectMapper.readValue(result.getBody(), new TypeReference<>() {});

    assertThat(list.size()).isEqualTo(1);
    PersonDto p = list.get(0);
    assertThat(p.getFirstName()).isEqualTo("Tim");
    assertThat(p.getLastName()).isEqualTo("Nord");
  }
}
