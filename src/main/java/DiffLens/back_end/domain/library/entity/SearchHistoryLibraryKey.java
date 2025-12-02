package DiffLens.back_end.domain.library.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class SearchHistoryLibraryKey implements Serializable {
  @Column(name = "history_id")
  private Long historyId;

  @Column(name = "library_id")
  private Long libraryId;
}
