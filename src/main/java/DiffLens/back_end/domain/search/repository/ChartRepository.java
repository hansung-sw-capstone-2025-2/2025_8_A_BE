package DiffLens.back_end.domain.search.repository;

import DiffLens.back_end.domain.search.entity.Chart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChartRepository extends JpaRepository<Chart, Long> {
}
