package sommersemester2022.userroles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * @author David Wiebe
 * Eine Controller Klasse für die Entityklasse RoleEntity
 * @see RoleEntity
 */
@RestController
@Service
public class RoleController {
  @Autowired
  private RoleRepo roleRepo;

  /**
   * Eine Datenbankabfrage um alle Rollen aus der Datenbank zu erhalten
   * @return Liste aus RoleEntity Objekten
   */
  @GetMapping("/roles")
  public List<RoleEntity> getAll() {
    return roleRepo.findAll();
  }
}
