package DiffLens.back_end.domain.search.repository;

import DiffLens.back_end.domain.members.entity.Member;
import DiffLens.back_end.domain.search.entity.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {

    @Query(
            """
                SELECT sh FROM SearchHistory sh
                WHERE sh.member = :member
            """
    )
    List<SearchHistory> findByMember(@Param("member") Member member);

}
