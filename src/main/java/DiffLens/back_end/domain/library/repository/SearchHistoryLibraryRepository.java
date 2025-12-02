package DiffLens.back_end.domain.library.repository;

import DiffLens.back_end.domain.library.entity.SearchHistoryLibrary;
import DiffLens.back_end.domain.library.entity.SearchHistoryLibraryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SearchHistoryLibraryRepository extends JpaRepository<SearchHistoryLibrary, SearchHistoryLibraryKey> {

    @Modifying
    @Query("DELETE FROM SearchHistoryLibrary shl WHERE shl.library.id = :libraryId")
    void deleteByLibraryId(@Param("libraryId") Long libraryId);

    @Query("SELECT shl FROM SearchHistoryLibrary shl WHERE shl.library.id = :libraryId")
    List<SearchHistoryLibrary> findByLibraryId(@Param("libraryId") Long libraryId);
}