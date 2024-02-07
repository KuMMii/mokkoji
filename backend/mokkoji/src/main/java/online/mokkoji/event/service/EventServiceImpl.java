package online.mokkoji.event.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.mokkoji.common.exception.RestApiException;
import online.mokkoji.common.exception.errorCode.EventErrorCode;
import online.mokkoji.common.exception.errorCode.ResultErrorCode;
import online.mokkoji.common.exception.errorCode.UserErrorCode;
import online.mokkoji.event.domain.Event;
import online.mokkoji.event.dto.request.MessageReqDto;
import online.mokkoji.event.repository.EventRepository;
import online.mokkoji.openvidu.dto.request.SessionReqDto;
import online.mokkoji.result.domain.Result;
import online.mokkoji.result.domain.RollingPaper.BackgroundTemplate;
import online.mokkoji.result.domain.RollingPaper.PostitTemplate;
import online.mokkoji.result.domain.RollingPaper.RollingPaper;
import online.mokkoji.result.repository.BackgroundTemplateRepository;
import online.mokkoji.result.repository.PostitTemplateRepository;
import online.mokkoji.result.repository.ResultRepository;
import online.mokkoji.result.repository.RollingPaperRepository;
import online.mokkoji.user.domain.User;
import online.mokkoji.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService {


    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ResultRepository resultRepository;
    private final RollingPaperRepository rollingPaperRepository;
    private final BackgroundTemplateRepository backgroundTemplateRepository;
    private final PostitTemplateRepository postitTemplateRepository;


    // 호스트 Session 생성
    @Override
    public String createSession(SessionReqDto sessionDto) {

        //User 객체 가져오기
        // userId 없을 경우
        User user = userRepository.findById(sessionDto.getUserId())
                .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));

        // Event 객체 생성
        Event event = Event.createSession()
                .user(user)
                .sessionId(sessionDto.getSessionId())
                .startTime(sessionDto.getStartTime())
                .build();

        // repository에 저장
        Event savedEvent = eventRepository.save(event);

        // 빈 Result도 생성
        Result result = new Result(savedEvent);
        Result savedResult = resultRepository.save(result);
        // 빈 rollingpaper 생성
        PostitTemplate postitTemplate = postitTemplateRepository.findById(1).orElseThrow(() -> new RestApiException(ResultErrorCode.POSTIT_NOT_FOUND));
        BackgroundTemplate backgroundTemplate = backgroundTemplateRepository.findById(1).orElseThrow(() -> new RestApiException(ResultErrorCode.BACKGROUND_NOT_FOUND));
        RollingPaper rollingPaper = RollingPaper.buildWithResult()
                .result(savedResult)
                .backgroundTemplate(backgroundTemplate)
                .postitTemplate(postitTemplate)
                .build();
        rollingPaperRepository.save(rollingPaper);

        return savedEvent.getSessionId();
    }


    // 호스트의 세션 status closed로 변경
    @Override
    public void deleteSession(String sessionId, SessionReqDto sessionReqDto) {

        // 세션의 호스트Id와 지금 전달받은 userId가 맞는지 확인
        Event event = eventRepository.findBySessionId(sessionId);
        if (!event.getUser().getId().equals(sessionReqDto.getUserId())) {
            log.error("호스트Id가 아님"); //임시로 하는 거.
            throw new RestApiException(EventErrorCode.HOST_NOT_FOUND);
        }

        //session의 status를 CLOSED로 변경
        event.closeSession(sessionReqDto);

        //session 저장
        eventRepository.save(event);


    }

    // 롤링페이퍼 파일 받아서 유효성 검사
    @Override
    public Map<String, MultipartFile> createRollingpaperFileMap(MessageReqDto messageReqDto) {

        Map<String, MultipartFile> fileMap = new HashMap<>();
        // 음성이 있는 경우 map에 저장
        MultipartFile voice = messageReqDto.getVoice();
        if (voice != null && !voice.isEmpty()) {
            fileMap.put("voice", voice);
        }
        // 영상이 있는 경우 map에 저장
        MultipartFile video = messageReqDto.getVideo();
        if (video != null && !video.isEmpty()) {
            fileMap.put("video", video);
        }

        return fileMap;
    }
}