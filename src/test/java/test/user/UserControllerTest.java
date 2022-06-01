package test.user;

import org.aspectj.lang.annotation.Before;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sommersemester2022.person.UserEntity;
import sommersemester2022.userroles.RoleEntity;
import sommersemester2022.userroles.UserRole;
import test.BaseTest;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.management.relation.Role;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class UserControllerTest extends BaseTest {

  @Test
  public void testCreate() throws Exception {
    String json = objectMapper.writeValueAsString(new UserEntity("Tim", "Nord", "user1", "pass1", null));
    ResponseEntity<String> result = restPost("/users/register", json);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    //User wird nicht erstellt da usernamen identisch sein müssen
    String json1 = objectMapper.writeValueAsString(new UserEntity("Tom", "Sawyer", "user1", "pass1", null));
    ResponseEntity<String> result1 = restPost("/users/register", json1);
    assertThat(result1.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

    List<UserEntity> entities = loadAll(UserEntity.class);
    assertThat(entities.size()).isEqualTo(5);
    UserEntity pe = entities.get(0);
    assertThat(pe.getId()).isGreaterThanOrEqualTo(1);
    assertThat(pe.getFirstName()).isEqualTo("Tim");
    assertThat(pe.getLastName()).isEqualTo("Nord");
  }


  @Test
  public void userGetById() throws Exception {
    String json = objectMapper.writeValueAsString(new UserEntity("Tim", "Nord", "user1", "pass1", null));
    restPost("/users/register", json);
    List<UserEntity> entities = loadAll(UserEntity.class);
    UserEntity user = entities.get(0);
    int id = user.getId();

    ResponseEntity<String> result = restAuthGet("/users/"+id,getJWTToken("admin"));
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

    ResponseEntity<String> result = restAuthPut("/users/"+id, json,getJWTToken("admin") );
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
    ResponseEntity<String> result = restAuthDel("/users/"+id,getJWTToken("admin"));
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

  @Test
  public void testGetAll() throws Exception {
    //TODO dont work
    List <RoleEntity> roles = new ArrayList<>();
    roles.add(new RoleEntity(UserRole.ROLE_TEACHER));
    roles.add(new RoleEntity(UserRole.ROLE_ADMINISTRATOR));
    String json = objectMapper.writeValueAsString(new UserEntity("Tim", "Nord", "user1", "pass1", null));
    restPost("/users/register", json);
    json = objectMapper.writeValueAsString(new UserEntity("Bim", "Nord", "user2", "pass1", null));
    restPost("/users/register", json);
    json = objectMapper.writeValueAsString(new UserEntity("Plim", "Nord", "user3", "pass1", null));
    restPost("/users/register", json);


    ResponseEntity<String> result = restGet("/users");
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    List<UserEntity> list = objectMapper.readValue(result.getBody(), new TypeReference<>() {});

    assertThat(list.size()).isEqualTo(4);
    assertThat(list.get(0).getFirstName().equals("admin"));
    assertThat(list.get(1).getFirstName().equals("Chris"));
    assertThat(list.get(2).getFirstName().equals("Florian"));
    assertThat(list.get(3).getFirstName().equals("Alexander"));
    assertThat(result.toString().contains("Plim"));
    assertThat(result.toString().contains("Bim"));


  }
  }



