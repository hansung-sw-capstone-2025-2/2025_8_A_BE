package DiffLens.back_end.domain.search.repository;

import DiffLens.back_end.domain.search.entity.Filter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface FilterRepository extends JpaRepository<Filter, Long> {

    @Query(
            """
                SELECT f from Filter f
                WHERE f.id in :ids
            """
    )
    List<Filter> findByIds(@Param("ids") Set<Long> ids);

    @Query(
            """
                SELECT f from Filter f
                WHERE f.id in :ids
            """
    )
    List<Filter> findByIds(@Param("ids") List<Long> ids);

}
