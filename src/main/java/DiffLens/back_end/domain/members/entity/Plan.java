package DiffLens.back_end.domain.members.entity;

import DiffLens.back_end.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Plan extends BaseEntity {

    @Id
    @Column(name = "plan_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Min(0)
    @Column(nullable = false)
    private Integer price;

}
