package DiffLens.back_end.domain.rawData.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsumptionDTO {
    @JsonProperty("OTT개수")
    private String ottCount;

    @JsonProperty("전통시장")
    private String traditionalMarket;

    @JsonProperty("설선물")
    private String holidayGift;

    @JsonProperty("소비만족")
    private String consumptionSatisfaction;

    @JsonProperty("배송서비스")
    private String deliveryService;

    @JsonProperty("주요지출")
    private String mainExpense;

    @JsonProperty("포인트관심")
    private String pointInterest;
}
