package DiffLens.back_end.domain.library.repository;

import DiffLens.back_end.domain.library.entity.Library;
import DiffLens.back_end.domain.members.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LibraryRepository extends JpaRepository<Library, Long> {
    List<Library> findByMemberOrderByCreatedDateDesc(Member member);
}