package DiffLens.back_end.domain.rawData.service;

import java.io.IOException;

public interface RawDataUploadService {

    void uploadRawData(String json) throws IOException;

}
