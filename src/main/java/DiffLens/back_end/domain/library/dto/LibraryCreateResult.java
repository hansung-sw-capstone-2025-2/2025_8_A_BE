package DiffLens.back_end.domain.library.dto;

import DiffLens.back_end.domain.library.entity.Library;

// 라이브러리 생성 결과를 담는 내부 레코드
public record LibraryCreateResult(
        Library library,
        int panelCount
) {

    public static LibraryCreateResult of(Library library, int panelCount) {
        return new LibraryCreateResult(library, panelCount);
    }

}