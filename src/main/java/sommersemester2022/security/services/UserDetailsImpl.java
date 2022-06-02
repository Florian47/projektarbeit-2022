package sommersemester2022.security.services;
import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import sommersemester2022.person.UserEntity;
/**
 * @author David Wiebe
 * Die Klasse UserDetailImpl verhält sich ähnlich wie die UserEntity Klasse. Es Werden diverse Benutzer Attribute gespeichert
 * Diese Attribute sind für die Kommunikation zwischen Webclient und Server wichtig. So können diese Daten sowohl auf der Server
 * Seite, als auch auf der Webclient seite abgefragt und verwendet werden.
 */
public class UserDetailsImpl implements UserDetails {

  @Serial
  private static final long serialVersionUID = 1L;

  private final int id;

  private final String username;

  @JsonIgnore
  private final String password;

  private final Collection<? extends GrantedAuthority> authorities;
  /**
   * Die Methode UserDetailsImpl ist die Konstruktor Methode der Klasse UserDetailsImpl.
   * @param id
   * @param username
   * @param password
   * @param authorities
   */
  public UserDetailsImpl(int id, String username, String password,
                         Collection<? extends GrantedAuthority> authorities)
  {
    this.id = id;
    this.username = username;
    this.password = password;
    this.authorities = authorities;
  }
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities()
  {
    return authorities;
  }
  /**
   * Die Methode build erstellt den Body der über die Authentifizierung mit dem JWT Token an den Webclient geschickt wird.
   * Hier bei wird ein UserEntity Objekt übergeben. Die Rollen des Benutzerobjektes werden in eine GrandtAuthority
   * Collection gespeichert. Diese werden zusammen mit der Benutzer ID, dem Benutzernamen und dem Passwort
   * als UserDetailsImpl Objekt zurückgegeben.
   * @param user
   * @return UserDetailsImpl(user.getId (),user.getUsername(),user.getPassword(),authorities
   */
  public static UserDetailsImpl build(UserEntity user)
  {
    List<GrantedAuthority> authorities = user.getRoles().stream()
      .map(role -> new SimpleGrantedAuthority(role.getName().name()))
      .collect(Collectors.toList());
    return new UserDetailsImpl(
      user.getId(),
      user.getUsername(),
      user.getPassword(),
      authorities);
  }

  public int getId()
  {
    return id;
  }

  @Override
  public String getPassword()
  {
    return password;
  }

  @Override
  public String getUsername()
  {
    return username;
  }

  @Override
  public boolean isAccountNonExpired()
  {
    return true;
  }
  @Override
  public boolean isAccountNonLocked()
  {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired()
  {
    return true;
  }

  @Override
  public boolean isEnabled()
  {
    return true;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    UserDetailsImpl user = (UserDetailsImpl) o;
    return Objects.equals(id, user.id);
  }

}
