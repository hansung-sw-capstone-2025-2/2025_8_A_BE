package DiffLens.back_end.domain.panel.repository;

import DiffLens.back_end.domain.panel.entity.Panel;
import DiffLens.back_end.domain.panel.repository.projection.PanelWithRawDataDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PanelRepository extends JpaRepository<Panel, String> {

    /**
     * embedding 제외하여 조회 -> DTO 반환
     */
    @Query("""
        SELECT new DiffLens.back_end.domain.panel.repository.projection.PanelWithRawDataDTO(
            p.id,
            p.gender,
            p.age,
            p.ageGroup,
            p.birthYear,
            p.region,
            p.residence,
            p.maritalStatus,
            p.childrenCount,
            p.familySize,
            p.education,
            p.occupation,
            p.job,
            p.personalIncome,
            p.householdIncome,
            p.electronicDevices,
            p.phoneBrand,
            p.phoneModel,
            p.carOwnership,
            p.carBrand,
            p.carModel,
            p.smokingExperience,
            p.cigaretteBrands,
            p.eCigarette,
            p.drinkingExperience,
            p.profileSummary,
            p.hashtags,
            p.rawData
        )
        FROM Panel p
        WHERE p.id IN :ids
        """)
    List<PanelWithRawDataDTO> findPanelsWithRawDataByIds(@Param("ids") List<String> ids);

    @Query("""
    SELECT new DiffLens.back_end.domain.panel.repository.projection.PanelWithRawDataDTO(
        p.id,
        p.gender,
        p.age,
        p.ageGroup,
        p.birthYear,
        p.region,
        p.residence,
        p.maritalStatus,
        p.childrenCount,
        p.familySize,
        p.education,
        p.occupation,
        p.job,
        p.personalIncome,
        p.householdIncome,
        p.electronicDevices,
        p.phoneBrand,
        p.phoneModel,
        p.carOwnership,
        p.carBrand,
        p.carModel,
        p.smokingExperience,
        p.cigaretteBrands,
        p.eCigarette,
        p.drinkingExperience,
        p.profileSummary,
        p.hashtags,
        p.rawData
    )
    FROM Panel p
    WHERE p.id IN :ids
    """)
    Page<PanelWithRawDataDTO> findPanelsWithRawDataByIdsInPage(@Param("ids") List<String> ids, Pageable pageable);

    @Query(
            """
            SELECT p FROM Panel p
            WHERE p.id IN :ids
            """
    )
    List<Panel> findByIdList(@Param("ids") List<String> ids);

    Page<Panel> findByIdIn(List<String> ids, Pageable pageable);

}
