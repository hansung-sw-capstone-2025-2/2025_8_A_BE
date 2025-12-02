package DiffLens.back_end.domain.rawData.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LifestyleDTO {
    @JsonProperty("겨울방학")
    private String winterVacation;

    @JsonProperty("반려동물")
    private String petOwnership;

    @JsonProperty("이사스트레스")
    private String movingStress;

    @JsonProperty("스트레스원인")
    private String stressCause;

    @JsonProperty("해외여행")
    private String overseasTravel;

    @JsonProperty("여름걱정")
    private String summerConcern;

    @JsonProperty("알람설정")
    private String alarmSetting;

    @JsonProperty("혼밥빈도")
    private String soloMealFrequency;

    @JsonProperty("노년행복")
    private String seniorHappiness;

    @JsonProperty("라이프스타일")
    private String lifestyleType;

    @JsonProperty("여행스타일")
    private String travelStyle;

    @JsonProperty("여름패션")
    private String summerFashion;

    @JsonProperty("비대처")
    private String rainResponse;

    @JsonProperty("갤러리사진")
    private String galleryPhoto;

    @JsonProperty("물놀이장소")
    private String waterPlayLocation;
}