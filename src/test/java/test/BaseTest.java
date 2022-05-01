package test;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.springbootdemoserver.Application;
import example.springbootdemoserver.person.PersonEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

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

  @BeforeEach
  public void setup() {
    new TransactionTemplate(tx).execute(new TransactionCallbackWithoutResult() {
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        removeAll(PersonEntity.class);
      }
    });
  }

  protected ResponseEntity<String> restPost(String url, String json) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return restTemplate.postForEntity(url, new HttpEntity<>(json, headers), String.class);
  }

  protected ResponseEntity<String> restGet(String url) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
  }

  protected <T> void removeAll(Class<T> type) {
    loadAll(type).forEach(e -> em.remove(e));
  }

  protected <T> List<T> loadAll(Class<T> type) {
    return em.createQuery("select e from " + type.getSimpleName() + " e", type).getResultList();
  }
}
