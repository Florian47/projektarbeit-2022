package test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import sommersemester2022.Application;
import sommersemester2022.security.services.jwt.JwtUtils;

import javax.persistence.EntityManager;
import java.util.List;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
  classes = Application.class
)
public class BaseTest {
  @Autowired
  protected TestRestTemplate restTemplate;
  @Autowired
  protected ObjectMapper objectMapper;
  @Autowired
  protected PlatformTransactionManager tx;
  @Autowired
  protected EntityManager em;
  @Autowired
  protected JwtUtils jwtUtils;

  //
//  @BeforeEach
//  public void setup() {
//    new TransactionTemplate(tx).execute(new TransactionCallbackWithoutResult() {
//      protected void doInTransactionWithoutResult(TransactionStatus status) {
//        removeAll(UserEntity.class);
//      }
//    });
//  }


  protected String getJWTToken(String user) {
    return jwtUtils.generateJwtToken(user);
  }

  protected ResponseEntity<String> restPost(String url, String json) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return restTemplate.postForEntity(url, new HttpEntity<>(json, headers), String.class);
  }

  protected ResponseEntity<String> restAuthPost(String url, String json, String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);
    return restTemplate.postForEntity(url, new HttpEntity<>(json, headers), String.class);
  }

  protected ResponseEntity<String> restGet(String url) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
  }

  protected ResponseEntity<String> restAuthGet(String url,String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);
    return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
  }

  protected ResponseEntity<String> restDel(String url) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(headers), String.class);
  }

  protected ResponseEntity<String> restAuthDel(String url,String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);
    return restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(headers), String.class);
  }

  protected ResponseEntity<String> restPut(String url, String json) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(headers), String.class);
  }

  protected ResponseEntity<String> restAuthPut(String url,String json,String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);
    return restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(headers), String.class);
  }

  protected <T> void removeAll(Class<T> type) {
    loadAll(type).forEach(e -> em.remove(e));
  }

  protected <T> List<T> loadAll(Class<T> type) {
    return em.createQuery("select e from " + type.getSimpleName() + " e", type).getResultList();
  }
}
