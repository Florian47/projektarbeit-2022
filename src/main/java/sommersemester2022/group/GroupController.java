package sommersemester2022.group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import sommersemester2022.security.services.jwt.JwtUtils;
import sommersemester2022.userroles.RoleRepo;

import javax.annotation.security.RolesAllowed;
import java.util.List;

import static sommersemester2022.userroles.UserRole.STUDENT;

@RestController @Service @Transactional
public class GroupController {
  @Autowired
  private GroupRepo groupRepo;
}
