package DiffLens.back_end.domain.members.repository;

import DiffLens.back_end.domain.members.entity.Member;
import DiffLens.back_end.domain.members.entity.Onboarding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OnboardingRepository extends JpaRepository<Onboarding, Long> {

    @Query(
            """
                SELECT ob FROM Onboarding ob
                WHERE ob.member = :member
            """
    )
    Optional<Onboarding> findByMember(@Param("member") Member member);

}
