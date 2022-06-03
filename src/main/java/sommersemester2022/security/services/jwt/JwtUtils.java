package sommersemester2022.security.services.jwt;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import sommersemester2022.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;

/**
 * @author David Wiebe
 * Die Klasse JwtUtils generiert und verwahltet den Sicherheitstoken. Diese Token ist für die Daten und Sicherheitsübermittlung
 * verantwortlich. Dazu werden einige Attribute implementiert. Dies betrifft u.a. den jwtSecret Key oder auch die
 * jwtExpirationMS Zeit. Die jwtExpirationMS Zeit gibt dem Webclient die Zeit wie lange das Token gültig ist.
 */
@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
  @Value("${bezkoder.app.jwtSecret}")
  private String jwtSecret;
  @Value("${bezkoder.app.jwtExpirationMs}")
  private int jwtExpirationMs;

  /**
   * Die Methode generateJwtToken extrahiert die Benutzeranmeldedaten aus das authetication Objekt und setzt die Benutzerdaten in ein
   * UserDetailsImpl Objekt. Im Return wird der Benutzername, an die zweite generateJwtToken Methode übergeben
   * @param authentication Das Anmelde Objekt
   * @return generateJwtToken(userPrincipal.getUsername())
   */
  public String generateJwtToken(Authentication authentication) {
    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
    return generateJwtToken(userPrincipal.getUsername());
  }

  /**
   * Die Methode generateJwtToken bildet den Sicherheitstoken. Alle Attribute die für diesen Sicherheitstoken notwendig sind,
   * werden über den Jwts.builder() zusammengefügt und wiedergegeben.
   * @param user Benutzername aus der Anmeldung
   * @return Jwts.builder().setSubject(user).setIssuedAt(new Date()).setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(SignatureAlgorithm.HS512, jwtSecret).compact()
   */
  public String generateJwtToken(String user) {
    return Jwts.builder()
      .setSubject(user)
      .setIssuedAt(new Date())
      .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
      .signWith(SignatureAlgorithm.HS512, jwtSecret)
      .compact();
  }

  /**
   * Die Methode getUserNameFromJwtTokenextrahiert den Benutzernamen aus dem Sicherheitstoken.
   * @param token Sicherheitstoken
   * @return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject()
   */
  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
  }

  /**
   * Die Methode validateJwtToken prüft ob der Sicherheitstoken valide ist. In dem der Bereich, der für die Identifikation
   * des Sicherheitstokens wichtig ist, überprüft wird. Falls der Sicherheitstoken nicht identifiziert werden kann, wird eine Fehlermeldung
   * ausgelöst.
   * @param authToken Identifikations Objekt
   * @return boolean
   */
  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
      return true;
    } catch (SignatureException e) {
      logger.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }
    return false;
  }
}
