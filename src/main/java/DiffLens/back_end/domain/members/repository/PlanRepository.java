package DiffLens.back_end.domain.members.repository;

import DiffLens.back_end.domain.members.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Long> {
}
