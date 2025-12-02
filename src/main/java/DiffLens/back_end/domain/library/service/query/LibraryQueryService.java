package DiffLens.back_end.domain.library.service.query;

import DiffLens.back_end.domain.library.dto.LibraryResponseDTO;
import DiffLens.back_end.domain.members.entity.Member;

public interface LibraryQueryService {
    LibraryResponseDTO.ListResult getLibrariesByMember();

    LibraryResponseDTO.LibraryDetail getLibraryDetail(Long libraryId);

    LibraryResponseDTO.LibraryPanels getLibraryPanels(Long libraryId, Integer pageNum, Integer size);
}
