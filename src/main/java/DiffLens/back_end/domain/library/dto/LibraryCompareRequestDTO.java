package DiffLens.back_end.domain.library.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class LibraryCompareRequestDTO {

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Compare {

    @NotNull(message = "첫 번째 라이브러리 ID는 필수입니다")
    @JsonProperty("libraryId1")
    private Long libraryId1;

    @NotNull(message = "두 번째 라이브러리 ID는 필수입니다")
    @JsonProperty("libraryId2")
    private Long libraryId2;
  }
}
