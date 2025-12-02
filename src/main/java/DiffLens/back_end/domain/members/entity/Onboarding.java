package DiffLens.back_end.domain.members.entity;

import DiffLens.back_end.domain.members.enums.Industry;
import DiffLens.back_end.domain.members.enums.Job;
import DiffLens.back_end.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Onboarding extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "onboarding_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Job job;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Industry industry;

    // 연관관계

    // 양방향
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public void setMember(Member member) {
        this.member = member;
        if (member.getOnboarding() != this) {
            member.setOnboarding(this);
        }
    }

}
