package test.user;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sommersemester2022.person.UserEntity;
import sommersemester2022.userroles.RoleEntity;
import sommersemester2022.userroles.UserRole;
import test.BaseTest;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserControllerTest extends BaseTest {



  @Test
  public void testCreate() throws Exception {
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

  @Test
  public void testRead() throws Exception {
    String json = objectMapper.writeValueAsString(new UserEntity("Tim", "Nord", "user1", "pass1", null));
    restPost("/users/register", json);
    List<UserEntity> entities = loadAll(UserEntity.class);
    UserEntity user = entities.get(0);
    int id = user.getId();

    ResponseEntity<String> result = restGet("/users/"+id);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    List<UserEntity> list = loadAll((UserEntity.class));

    assertThat(list.size()).isEqualTo(1);
    assertThat(result.toString().contains("Tim"));
  }
  @Test
  public void testUpdate() throws Exception {
    //TODO dont work
    UserEntity user = new UserEntity("Tim", "Nord", "user1", "pass1", null);
    String json = objectMapper.writeValueAsString(user);
    restPost("/users/register", json);
    List<UserEntity> entities = loadAll(UserEntity.class);
    user = entities.get(0);
    int id = user.getId();
    user.setFirstName("Phillip");
    json = objectMapper.writeValueAsString(user);

    ResponseEntity<String> result = restPut("/users/"+id, json);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    List<UserEntity> list = objectMapper.readValue(result.getBody(), new TypeReference<>() {});

    assertThat(list.size()).isEqualTo(1);
    UserEntity p = list.get(0);
    assertThat(p.getFirstName()).isEqualTo("Phillip");
    assertThat(p.getLastName()).isEqualTo("Nord");
  }

  @Test
  public void testDelete() throws Exception {
    String json = objectMapper.writeValueAsString(new UserEntity("Tim", "Nord", "user1", "pass1", null));
    restPost("/users/register", json);
    List<UserEntity> entities = loadAll(UserEntity.class);
    UserEntity user = entities.get(0);
    int id = user.getId();
    ResponseEntity<String> result = restDel("/users/"+id);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    List<UserEntity> list = loadAll((UserEntity.class));
    assertThat(list.size()).isEqualTo(0);
  }
    @Test
    public void testAuthenticate() throws Exception {
      //TODO dont work
      List <RoleEntity> roles = new ArrayList<>();
      roles.add(new RoleEntity(UserRole.ROLE_TEACHER));
      roles.add(new RoleEntity(UserRole.ROLE_ADMINISTRATOR));
      String json = objectMapper.writeValueAsString(new UserEntity("Tim", "Nord", "user1", "pass1", roles));
      restPost("/users/register", json);


      ResponseEntity<String> result = restPost("/users/authenticate",json);
      assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
      List<UserEntity> list = objectMapper.readValue(result.getBody(), new TypeReference<>() {});

      assertThat(list.size()).isEqualTo(1);
      UserEntity p = list.get(0);
      assertThat(p.getFirstName()).isEqualTo("Tim");
      assertThat(p.getLastName()).isEqualTo("Nord");
    }
  }



