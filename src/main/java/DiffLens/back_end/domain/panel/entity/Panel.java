package DiffLens.back_end.domain.panel.entity;

import DiffLens.back_end.domain.rawData.entity.RawData;
import DiffLens.back_end.domain.search.enums.filters.Gender;
import DiffLens.back_end.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
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
public class Panel extends BaseEntity {

    @Id
    @Column(name = "panel_id", length = 50)
    private String id;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column
    @Min(0)
    private Integer age;

    @Column(length = 20)
    private String ageGroup;

    @Column(length = 4)
    private Integer birthYear;

    @Column(length = 100)
    private String region;

    @Column(length = 100)
    private String residence;

    @Column(length = 50)
    private String maritalStatus;

    @Column
    @Min(0)
    private Integer childrenCount;

    @Column(length = 10)
    private String familySize;

    @Column(length = 50)
    private String education;

    @Column(length = 200)
    private String occupation;

    @Column(length = 100)
    private String job;

    @Column(length = 50)
    private String personalIncome;

    @Column(length = 50)
    private String householdIncome;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[] DEFAULT '{}'::text[]")
    private List<String> electronicDevices = new ArrayList<>();

    @Column(length = 100)
    private String phoneBrand;

    @Column(length = 100)
    private String phoneModel;

    @Column(length = 20)
    private String carOwnership;

    @Column(length = 50)
    private String carBrand;

    @Column(length = 100)
    private String carModel;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[] DEFAULT '{}'::text[]")
    private List<String> smokingExperience = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[] DEFAULT '{}'::text[]")
    private List<String> cigaretteBrands = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[] DEFAULT '{}'::text[]")
    private List<String> eCigarette = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[] DEFAULT '{}'::text[]")
    private List<String> drinkingExperience = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String profileSummary;

    @JdbcTypeCode(SqlTypes.VECTOR)
    @Column(columnDefinition = "vector(4096)")
    @Basic(fetch = FetchType.LAZY)
    @Transient
    private float[] embedding = new float[4096]; // float[] 써야한다고 함...

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "hashtags", columnDefinition = "text[]")
    private List<String> hashtags = new ArrayList<>();

    // 연관관계
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id") // FK 컬럼 이름을 Panel PK와 동일하게
    @MapsId
    private RawData rawData;

}
