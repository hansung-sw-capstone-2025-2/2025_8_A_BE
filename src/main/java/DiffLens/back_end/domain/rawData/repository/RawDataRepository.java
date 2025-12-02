package DiffLens.back_end.domain.rawData.repository;

import DiffLens.back_end.domain.rawData.entity.RawData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RawDataRepository extends JpaRepository<RawData, String> {


    List<RawData> findAllByIdIn(List<String> panelIds);

    Page<RawData> findAllByIdIn(List<String> panelIds, Pageable pageable);

}
