package sommersemester2022.userroles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sommersemester2022.person.UserEntity;

import java.util.List;

@RestController
@Service
public class RoleController {
  @Autowired
  private RoleRepo roleRepo;

  @GetMapping("/roles")
  public List<RoleEntity> getAll() {
    return roleRepo.findAll();
  }
}
