package DiffLens.back_end.domain.library.entity;

import DiffLens.back_end.domain.panel.entity.Panel;
import DiffLens.back_end.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * Library - Panel 의 중간테이블
 */
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LibraryPanel extends BaseEntity {

    // 복합키
    @EmbeddedId
    private LibraryPanelKey id;

    // 연관관계

    @MapsId("libraryId") // 복합키 클래스 필드와 동기화
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_id", insertable = false, updatable = false) // insertable, updatable false => 중복방지
    private Library library;

    @MapsId("panelId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "panel_id", insertable = false, updatable = false)
    private Panel panel;

}
