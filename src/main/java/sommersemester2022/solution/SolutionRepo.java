package sommersemester2022.solution;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public interface SolutionRepo extends JpaRepository<SolutionEntity, Integer> {

}
