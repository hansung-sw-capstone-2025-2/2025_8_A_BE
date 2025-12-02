package DiffLens.back_end.domain.members.entity;

import DiffLens.back_end.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

//@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Agreement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agreement_id")
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // 연관관계

    // 양방향
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

}
