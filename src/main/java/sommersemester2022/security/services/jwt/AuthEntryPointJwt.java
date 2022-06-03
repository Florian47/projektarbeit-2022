package sommersemester2022.security.services.jwt;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import sommersemester2022.security.WebSecurityConfig;

/**
 * @author David Wiebe
 * Die Klasse AuthEntryPointJwt ist eine Fehlermanager. Dieser löst aus, wenn die Authentifizierung des Benutzers fehlschlägt.
 * @see WebSecurityConfig
 */
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
  private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

  /**
   * Wenn die Authentifizierung des Benutzers fehlschlägt, wird ein Unauthorized error in den Logmanager des
   * Servers geschrieben.
   * @param request Anfrage des Webclients
   * @param response Antwort des Servers
   * @param authException Definition des Fehlers
   * @throws IOException
   * @throws ServletException
   */
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
                       AuthenticationException authException) throws IOException, ServletException {
    logger.error("Unauthorized error: {}", authException.getMessage());
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
  }
}
