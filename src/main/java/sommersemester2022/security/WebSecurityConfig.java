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

import javax.servlet.http.HttpServletResponse;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
/*
Die Klasse WebSecurityConfig ist eine Klasse die die Zugriffsrechte auf den Webclient regelt.
Die Annotation @EnableWebSecurity ermöglicht es Spring, automatisch auf die Klasse und somit auf die globale
Websicherheit zuzugreifen. @EnableGlobalMethodeSecurity ermöglicht den Zugriff auf diverse weitere Annotationen um den Rolen
die nötigen Rechte zuzuweisen.
Die Klasse wurde von David Wiebe erstellt.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, proxyTargetClass = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
  @Autowired
  UserDetailsServiceImpl userDetailsService;
  @Autowired
  private AuthEntryPointJwt unauthorizedHandler;
  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter();
  }
  @Override
  /*
  Diese Methode bindet die Benutzer Details die für die Authenfizierung relevant sind an den AuthenticationManager.
   */
  public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
    authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  }
  @Bean
  @Override
  // Der AuthenticationManager bildet die Grundlage für die Authentifizierung bei der Anmeldung der Benutzer.
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
  // Der PasswordEncoder encoded das verschlüsselte Passwort.
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler() {
    DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
    defaultWebSecurityExpressionHandler.setDefaultRolePrefix("");
    return defaultWebSecurityExpressionHandler;
  }
  /*
  Die Methode configure(HttpSecurity http) ist das Herzstück der Klasse. Hier werden die Zugriffe auf den Webclient geregelt.
  Vor allem wird festelegt, wann eine Authentifizierung nötig ist. Desweiteren wird festgelegt wann der Exception Handler
  (AuthEntryPonitJwt) eingreifen soll. Am Ende der Methode wird ein AuthTokenFilter gesetzt.
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

