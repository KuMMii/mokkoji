package online.mokkoji.event.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import online.mokkoji.result.domain.Result;
import org.springframework.data.redis.core.RedisHash;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@RedisHash
public class PhotoResDto {
//    private Long resultId;
    private Result result;
    private String photoPath;
}
