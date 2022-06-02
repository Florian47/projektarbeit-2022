package sommersemester2022.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sommersemester2022.security.services.jwt.AuthEntryPointJwt;
import sommersemester2022.security.services.jwt.AuthTokenFilter;
import sommersemester2022.security.services.UserDetailsServiceImpl;


/**
 * @author David Wiebe
 * Die Klasse WebSecurityConfig regelt die Zugriffsrechte auf den Webclient.
 * @see EnableWebSecurity ermöglicht Spring einen automatischen Zugriff auf diese Klasse, dies Unterstützt eine globale
 * Websichertheit.
 * @see EnableGlobalMethodSecurity ermöglicht den Zugriff auf Anotationen mit denen Zugriffe auf einzelne Methoden
 * eingeschränkt werden können.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, proxyTargetClass = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
  @Autowired
  UserDetailsServiceImpl userDetailsService;
  @Autowired
  private AuthEntryPointJwt unauthorizedHandler;

  /**
   * Es wird ein AuthTokenFilter gesetzt der für die JWT Token authentifizierung notwendig ist.
   * @return AuthTokenFilter
   */
  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter();
  }
  @Override
  /**
   * Die Methode configure fügt über den Authentic
   * ationManagerBuilder den UserDetailService und den PsswordEncoder zusammen.
   * Der AuthenticationManager bildet die Grundlage der Authentifizierung.
   * @Param AuthenticationBuilder
   * @throws Exception
   */
  public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
    authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  }
  @Bean
  @Override
  /**
   * Diese Methode wird überschrieben um die Methode configure(AuthenticationManagerBuilder) als Bean zur verfügung
   * zu stellen.
   * @Return authenticationManagerBean
   * @throws Exception
   */
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  /**
   * Der PasswordEncoder koodiert die Passwörter. Dies ist besonders wichtig die Sicherheit der Datenübermittlung zwischen Server
   * und WebClient zu gewährleisten.
   * @return BCryptPasswordEncoder
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  /**
   * Der DefaultWebSecurityExpressionHandler übersetzt die Anotatione, die an den Methoden der einzelnen Controller
   * notiert werden,
   * @return defaultWebsecurityExpressionHandler
   */
  @Bean
  public DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler() {
    DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
    defaultWebSecurityExpressionHandler.setDefaultRolePrefix("");
    return defaultWebSecurityExpressionHandler;
  }

  /**
   * Die Methode configure ist eine Sicherheitskonfiguration. In dieser Methode werden erste Einschränkungen im Bezug
   * auf den Serverzugriff implementiert. Desweiteren wird ein Exception Handler "AuthenticationEntryPoint" eingebunden.
   * Es werden diverse SIcherheitsmaßnahmen getroffen wie das SessionManagment und der eingeschränkte authorisierte Zugriff
   * auf den Webclient. Dieser ist nur über eine Benutzername und Passwort authentifizierung möglich.
   * @param http the {@link HttpSecurity} to modify
   * @throws Exception
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .csrf().disable();
    http.headers().frameOptions().disable();
    http.cors().and().csrf().disable()
      .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
      .authorizeRequests()
        .antMatchers("/api/**").permitAll()
        .antMatchers("/**").permitAll()
        .anyRequest().authenticated()
      ;
    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
  }
}

