package DiffLens.back_end.domain.search.entity;

import DiffLens.back_end.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Filter extends BaseEntity {

    @Id
    @Column(name = "filter_id")
    private Long id; // 자동생성 X

    @Column(nullable = false, length = 50)
    private String type;

    @Column(nullable = false, length = 100)
    private String displayValue;

    @Column(nullable = true, length = 200)
    private String rawDataValue;

}
