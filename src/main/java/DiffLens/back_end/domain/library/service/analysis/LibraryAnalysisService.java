package DiffLens.back_end.domain.library.service.analysis;

import DiffLens.back_end.domain.library.dto.LibraryCompareRequestDTO;
import DiffLens.back_end.domain.library.dto.LibraryCompareResponseDTO;
import DiffLens.back_end.domain.library.dto.LibraryResponseDTO;
import DiffLens.back_end.domain.members.entity.Member;

public interface LibraryAnalysisService {
    LibraryCompareResponseDTO.CompareResult compareLibraries(LibraryCompareRequestDTO.Compare request);

    LibraryResponseDTO.LibraryDashboard getLibraryDashboard(Long libraryId);
}
