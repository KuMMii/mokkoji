package online.mokkoji.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SessionRequestDto {
    private Boolean allowTranscoding;
    private RecordingPropertyRequestDto defaultRecordingProperties;
}
