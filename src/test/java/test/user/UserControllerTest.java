package test.user;

import com.fasterxml.jackson.core.type.TypeReference;
import org.h2.engine.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sommersemester2022.person.UserEntity;
import test.BaseTest;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alexander Kiehl
 * In dieser Klasse werden die CRUD Operationen des UserControllers getestet.
 */
public class UserControllerTest extends BaseTest {
  /**
   * Erstellt einen Nutzer mit den gegebenen Variablen
   * @throws Exception
   */
  @Test
  public void testCreate() throws Exception {
    admin.setUsername("admin2");
    admin.setId(0);
    String json = objectMapper.writeValueAsString(admin);
    ResponseEntity<String> result = restPost("/users/register", json);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    List<UserEntity> entities = loadAll(UserEntity.class);
    assertThat(entities.size()).isEqualTo(2);

    UserEntity pe = entities.stream().filter(user -> Objects.equals(user.getUsername(), "admin2")).findAny().get();
    assertThat(pe.getId()).isGreaterThanOrEqualTo(1);
  }

  /**
   * Versucht einen Nutzer mit bereits vorhanenem username zu erstellen und scheitert
   * @throws Exception
   */
  @Test
  public void testCreateSameUsernameFailing() throws Exception {
    admin.setId(0);
    String json = objectMapper.writeValueAsString(admin);
    ResponseEntity<String> result1 = restPost("/users/register", json);
    assertThat(result1.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Holt den Benutzer über die id aus der Datenbank
   * @throws Exception
   */
  @Test
  public void userGetById() throws Exception {
    ResponseEntity<String> result = restAuthGet("/users/" + admin.getId(), getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    List<UserEntity> list = loadAll((UserEntity.class));

    assertThat(list.size()).isEqualTo(1);
    assertThat(result.toString().contains("admin"));
  }

  /**
   * Ändert den Vornamen eines Nutzers über die id
   * @throws Exception
   */
  @Test
  public void testUpdate() throws Exception {
    admin.setFirstName("admin2");
    String json = objectMapper.writeValueAsString(admin);

    ResponseEntity<String> result = restAuthPut("/users/" + admin.getId(), json, getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    List<UserEntity> list = loadAll(UserEntity.class);

    assertThat(list.size()).isEqualTo(1);
    UserEntity p = list.get(0);
    assertThat(p.getFirstName()).isEqualTo("admin2");
    assertThat(p.getLastName()).isEqualTo("admin");
  }

  /**
   * Löscht einen Nutzer über eine id aus der Datenbank
   * @throws Exception
   */
  @Test
  public void testDelete() throws Exception {
    UserEntity user = new UserEntity();
    user.setUsername("Fred");
    user.setFirstName("Feuerstein");
    userRepo.save(user);
    List<UserEntity> list = loadAll((UserEntity.class));
    int id = list.get(1).getId();

    ResponseEntity<String> result = restAuthDel("/users/" + id, getJWTToken("admin"));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    list = loadAll((UserEntity.class));
    assertThat(list.size()).isEqualTo(1);
  }

//  @Test
//  @Ignore
//  public void testAuthenticate() throws Exception {
//
//    UserEntity user = new UserEntity("Tim", "Nord", "user1", "pass1", null);
//    user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12)));
//    String json = objectMapper.writeValueAsString(user);
//    restPost("/users/register", json);
//    List<UserEntity> listt = loadAll((UserEntity.class));
//    user = listt.get(0);
//
//    json = objectMapper.writeValueAsString(user);
//    ResponseEntity<String> result = restPost("/users/authenticate", json);
//    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
//    List<UserEntity> list = objectMapper.readValue(result.getBody(), new TypeReference<>() {
//    });
//
//    assertThat(list.size()).isEqualTo(1);
//    UserEntity p = list.get(0);
//    assertThat(p.getFirstName()).isEqualTo("Tim");
//    assertThat(p.getLastName()).isEqualTo("Nord");
//  }

  /**
   * Liest alle Nutzer aus der Datenbank aus
   * @throws Exception
   */
  @Test
  public void testGetAll() throws Exception {
    String json = objectMapper.writeValueAsString(new UserEntity("Tim", "Nord", "user1", "pass1", null));
    restPost("/users/register", json);
    json = objectMapper.writeValueAsString(new UserEntity("Bim", "Nord", "user2", "pass1", null));
    restPost("/users/register", json);
    json = objectMapper.writeValueAsString(new UserEntity("Plim", "Nord", "user3", "pass1", null));
    restPost("/users/register", json);


    ResponseEntity<String> result = restGet("/users");
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    List<UserEntity> list = objectMapper.readValue(result.getBody(), new TypeReference<>() {
    });

    assertThat(list.size()).isEqualTo(4);
    assertThat(list.get(0).getFirstName().equals("admin"));
    assertThat(result.toString().contains("Plim"));
    assertThat(result.toString().contains("Bim"));
  }
}



