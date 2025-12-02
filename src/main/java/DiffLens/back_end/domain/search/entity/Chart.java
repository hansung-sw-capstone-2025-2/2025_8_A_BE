package DiffLens.back_end.domain.search.entity;

import DiffLens.back_end.domain.search.enums.chart.ChartType;
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
@ToString
public class Chart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chart_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChartType chartType;

    @Column(nullable = false, length = 50)
    private String panelColumn;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 50)
    private String xAxis;

    @Column(nullable = false, length = 50)
    private String yAxis;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(nullable = false, columnDefinition = "text[]")
    private List<String> labels = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(nullable = false, columnDefinition = "text[]")
    private List<Integer> values =  new ArrayList<>();


    // 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "history_id")
    private SearchHistory searchHistory;

    // 연관관계 편의 메서드
    public void setSearchHistory(SearchHistory searchHistory) {
        this.searchHistory = searchHistory;

        if (searchHistory != null && !searchHistory.getCharts().contains(this)) {
            searchHistory.getCharts().add(this);
        }
    }


}
