package sommersemester2022.training;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import sommersemester2022.person.UserEntity;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public interface TrainingRepo extends JpaRepository<TrainingEntity, Integer> {
  Optional<List<TrainingEntity>> findByStudentsAndIndividualTrue(Optional<UserEntity> user);
  List<TrainingEntity> findByIndividualTrue();
}
