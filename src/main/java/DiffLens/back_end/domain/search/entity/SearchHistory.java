package DiffLens.back_end.domain.search.entity;

import DiffLens.back_end.domain.members.entity.Member;
import DiffLens.back_end.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[] NOT NULL DEFAULT '{}'")
    private List<String> panelIds;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "float[] NOT NULL DEFAULT '{}'")
    private List<Float> concordanceRate; // 일치율

    // 연관관계

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "searchHistory")
    private SearchFilter searchFilter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @Setter
    private Member member;

    @OneToMany(mappedBy = "searchHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chart> charts = new ArrayList<>();

    public void setFilter(SearchFilter searchFilter) {
        this.searchFilter = searchFilter;
    }

    // 연관관계 편의 메서드
    public void addChart(Chart chart) {
        charts.add(chart);
        chart.setSearchHistory(this);
    }

    public void removeChart(Chart chart) {
        charts.remove(chart);
        chart.setSearchHistory(null);
    }

    public void setCreatedAt(LocalDate date) {
        this.date = date;
    }

}
