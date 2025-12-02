package DiffLens.back_end.domain.library.repository;

import DiffLens.back_end.domain.library.entity.LibraryPanel;
import DiffLens.back_end.domain.library.entity.LibraryPanelKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LibraryPanelRepository extends JpaRepository<LibraryPanel, LibraryPanelKey> {

    @Modifying
    @Query("DELETE FROM LibraryPanel lp WHERE lp.library.id = :libraryId")
    void deleteByLibraryId(@Param("libraryId") Long libraryId);

    @Query("SELECT COUNT(lp) FROM LibraryPanel lp WHERE lp.library.id = :libraryId")
    int countByLibraryId(@Param("libraryId") Long libraryId);
}