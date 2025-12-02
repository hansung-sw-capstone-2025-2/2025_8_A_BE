package DiffLens.back_end.domain.library.service.command;

import DiffLens.back_end.domain.library.dto.LibraryCreateResult;
import DiffLens.back_end.domain.library.dto.LibraryRequestDto;
import DiffLens.back_end.domain.members.entity.Member;

public interface LibraryCommandService {
    LibraryCreateResult createLibrary(LibraryRequestDto.Create request);

    LibraryCreateResult addSearchHistoryToLibrary(Long libraryId, Long searchHistoryId);
}
