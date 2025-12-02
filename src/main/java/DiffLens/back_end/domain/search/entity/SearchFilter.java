package DiffLens.back_end.domain.search.entity;

import DiffLens.back_end.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchFilter extends BaseEntity {

    @Id // history의 id로 지정. 자동 X
    private Long id;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(nullable = false, columnDefinition = "bigint[] DEFAULT '{}'::bigint[]")
    private List<Long> filters = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "search_id")
    private SearchHistory searchHistory;

}
