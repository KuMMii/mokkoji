package online.mokkoji.result.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.mokkoji.S3.S3Service;
import online.mokkoji.common.auth.jwt.util.JwtUtil;
import online.mokkoji.event.dto.response.PhotoResDto;
import online.mokkoji.result.dto.request.RollingPaperReqDto;
import online.mokkoji.result.dto.response.ResultResDto;
import online.mokkoji.result.service.ResultService;
import online.mokkoji.user.domain.User;
import online.mokkoji.user.repository.UserRepository;
import online.mokkoji.user.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.version}/results")
public class ResultController {

    private final ResultService resultService;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final S3Service s3Service;
    private final UserRepository userRepository;

    // 행사 리스트
    @GetMapping("/lists")
    public ResponseEntity<Map<String, Object>> getResultList(HttpServletRequest req) {

//        String provider = "NAVER";
//        String email = "";

        Map<String, Object> result = resultService.getResultList(jwtUtil.getProvider(req), jwtUtil.getEmail(req));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 추억 결과물 보기
    @GetMapping("/recollections/{resultId}")
    public ResponseEntity<ResultResDto> getResult(@PathVariable Long resultId, @PageableDefault(page = 0, size = 9) Pageable pageable) {
        ResultResDto resultResDto = resultService.getResult(resultId, pageable);

        return new ResponseEntity<>(resultResDto, HttpStatus.OK);
    }

    // 추억 생성
    @GetMapping("/{resultId}")
    public ResponseEntity<Map<String, Object>> addRecollection(@PathVariable Long resultId, HttpServletRequest req) {
        resultService.createRecollection(resultId);

        String provider = jwtUtil.getProvider(req);
        String email = jwtUtil.getEmail(req);

        Map<String, Object> result = resultService.getResultList(provider, email);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    // 기억 편집화면
    @GetMapping("/{resultId}/memories")
    public ResponseEntity<Map<String, Object>> getRollingpaperAndPhotoEdit(@PathVariable Long resultId) {

        Map<String, Object> photoAndMessageMap = resultService.getPhotoAndMessageMap(resultId);

        return new ResponseEntity<>(photoAndMessageMap, HttpStatus.OK);
    }

    // 롤링페이퍼 편집 완료
    @PatchMapping("/{resultId}/memories/rollingpaper")
    public ResponseEntity<String> updateRollingpaper(@PathVariable Long resultId,
                                                     @RequestBody RollingPaperReqDto rollingPaperReqDto) {

        resultService.updateRollingpaper(resultId, rollingPaperReqDto);

        return new ResponseEntity<>("편집 완료", HttpStatus.OK);
    }


    // 사진첩 사진 추가
    @PostMapping("/{resultId}/memories/photos")
    public ResponseEntity<String> addPhotos(@PathVariable("resultId") Long resultId,
                                           HttpServletRequest req,
                                           @RequestParam("photos") List<MultipartFile> photoList) throws IOException {

        User user = userService.getByProviderAndEmail(jwtUtil.getProvider(req), jwtUtil.getEmail(req));

        // 사진 업로드
        List<PhotoResDto> photoResDtoList = s3Service.uploadPhotoList(photoList, user.getId(), resultId);
//        List<PhotoResDto> photoResDtoList = s3Service.uploadPhotoList(photoList, 1L, resultId);

        // db에 저장
        resultService.createPhotoList(photoResDtoList);

        return new ResponseEntity<>("사진 업로드 완료", HttpStatus.OK);
    }


    // 대표이미지 설정
    @PatchMapping("/results/{resultId}/memories")
    public ResponseEntity<String> updateThumbnail(@PathVariable("resultId") Long resultId, @RequestBody String url) {
        resultService.updateThumbnail(resultId, url);
        return new ResponseEntity<>("대표이미지 설정 완료", HttpStatus.OK);
    }


}
