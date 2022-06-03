package sommersemester2022.security.services.jwt;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import sommersemester2022.security.services.UserDetailsServiceImpl;

/**
 * @author David Wiebe
 *Die Klasse AuthTokenFilter, Filter den Sicherheitstoken. Zuerst wird festgestellt ob es eine Anfrage an den Webclient
 * ist oder ob es eine Antwort ist. Dementsprechend wird der Sicherheitstoken gefiltert.
 * @see JwtUtils
 */
public class AuthTokenFilter extends OncePerRequestFilter {
  @Autowired
  private JwtUtils jwtUtils;
  @Autowired
  private UserDetailsServiceImpl userDetailsService;
  private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

  /**
   * Die Methode diFilterInternal überprüft den Sicherheitstoken mit einigen Hilfsmethoden.
   * Dazu wird der Benutzername exthrahiert um die Methode der Klasse UserDetailServiceImpl aufgerufen.
   * @see UserDetailsServiceImpl
   * Dann werden die übermittelten Daten in ein Authetication Objekt gelegt. Wo bei in diesem Fall nur der Benutzername,
   * das Passwort und die Rollen des Benutzers gespeichert werden.
   * Anschließend werden die Daten der Webclientanfrage in das authentication Objekt gespeichert.
   * Um Serverseitig auf die Daten zugreifen zu können, wird das authentication Objekt in den SecuritxContextHolder gespeichert.
   * @see SecurityContextHolder
   * Um den zugriff auf individuell erstellte Header zu gewährleisten werden diese in der Methode authorisiert.
   * @param request Anfrage des Webclients
   * @param response Antwort des Servers
   * @param filterChain Filter Reihenfolge
   * @throws ServletException
   * @throws IOException
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {
    try {
      String jwt = parseJwt(request);
      if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
          userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (Exception e) {
      logger.error("Cannot set user authentication: {}", e);
    }
    response.setHeader("Access-Control-Allow-Headers", "Authorization, Access-Control-Allow-Headers, Access-Control-Expose-Headers, Origin, Accept, X-Requested-With, " +
      "Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
    response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization, Access-Control-Allow-Headers, Access-Control-Expose-Headers, Origin, Accept, X-Requested-With, " +
      "Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");

    filterChain.doFilter(request, response);
  }

  /**
   * Die Methode parseJwt überprüft die Anfrage des Webclients ob der Sicherheitstoken über den Authorization Header
   * mitgegeben wurde. Wenn dies zutrifft, wird ein bestimmter Teil des Sicherheitstokens weitergegeben.
   * @param request Anfrage des Webclients
   * @return headerAuth.substring(7, headerAuth.length()) or null
   */
  private String parseJwt(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
      return headerAuth.substring(7, headerAuth.length());
    }
    return null;
  }
}
