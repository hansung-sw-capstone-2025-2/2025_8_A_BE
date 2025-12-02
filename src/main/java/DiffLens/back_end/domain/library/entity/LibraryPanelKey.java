package DiffLens.back_end.domain.library.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

/**
 * LibraryPanel에 대한 복합키 클래스
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class LibraryPanelKey implements Serializable {

    @Column(name = "panel_id")
    private String panelId;

    @Column(name = "library_id")
    private Long libraryId;

}
