package DiffLens.back_end.domain.rawData.entity;

import DiffLens.back_end.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RawData extends BaseEntity {

    @Id
    private String id; // Panel과 동일한 ID를 PK로 사용

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false)
    @Setter
    private Object json;
}