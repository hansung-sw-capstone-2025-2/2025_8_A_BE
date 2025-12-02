package DiffLens.back_end.domain.library.entity;

import DiffLens.back_end.domain.search.entity.SearchHistory;
import DiffLens.back_end.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchHistoryLibrary extends BaseEntity {

    @EmbeddedId
    private SearchHistoryLibraryKey id;

    @MapsId("libraryId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_id", insertable = false, updatable = false)
    private Library library;

    @MapsId("historyId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "history_id", insertable = false, updatable = false)
    private SearchHistory history;
}
