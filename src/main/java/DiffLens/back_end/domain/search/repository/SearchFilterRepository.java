package DiffLens.back_end.domain.search.repository;

import DiffLens.back_end.domain.search.entity.SearchFilter;
import DiffLens.back_end.domain.search.entity.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.function.Function;

public interface SearchFilterRepository extends JpaRepository<SearchFilter, Long> {

    @Query(
            """
                SELECT sf FROM SearchFilter sf
                WHERE sf.searchHistory in :histories
            """
    )
    List<SearchFilter> findBySearchHistory(@Param("histories") List<SearchHistory> searchHistories);

}
