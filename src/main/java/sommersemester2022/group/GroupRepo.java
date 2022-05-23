package sommersemester2022.group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Service
public interface GroupRepo extends JpaRepository<GroupEntity, Integer> {
}
