package DiffLens.back_end.domain.rawData.converter;

import DiffLens.back_end.domain.rawData.dto.PanelDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Component
public class JsonToDTO implements RawDataConverter<PanelDTO, Object> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public PanelDTO convert(Object rawJson) {
        try {
            String jsonString = rawJson instanceof String ?
                    (String) rawJson :
                    objectMapper.writeValueAsString(rawJson);
            return objectMapper.readValue(jsonString, PanelDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert RawData JSON to PanelDTO", e);
        }

    }
}
