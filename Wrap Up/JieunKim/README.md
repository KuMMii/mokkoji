# Daily Wrap Up

## 20240213

### 오늘 한 것

- OpenVidu 소그룹 세션Id 백에서 생성하는 것으로 수정
  - redis에서 counter 사용
- 디버깅 후 자잘한 오류 수정

### 어려웠던 점

- redis에서 increment할 때 값이 없으면 set을 해야하는 줄 알았는데, set은 String밖에 되지 않아 자료형이 달라 increment가 되지 않는 오류에 빠졌음

### 새로 알게 된 점

- `redisConfig.java` 에서 `redisTemplate.setValueSerializer(**new** GenericJackson2JsonRedisSerializer());` 때문에

```java
// 카운터가 있으면 그대로 ++1, 아니면 0으로 시작
        if (redisTemplate.hasKey(counterKey)) {
            valueOperations.increment(counterKey, 1);
        } else {
            valueOperations.set(counterKey,"0");
        }
```

- 이렇게 하면 String으로 저장된 0에서 increment를 하지 못함
- 그래서 따로 분기처리를 하지 않고 있으면 increment를 함
  ```java
  // 키가 있든 없든 increment 메서드 호출
      valueOperations.increment(counterKey, 1);
  ```

### 내일 할 것

- 디버깅 후 오류 수정
- 발표 PPT 제작
- UCC 촬영

## 20240212

### 오늘 한 것

- OpenVidu 소그룹 세션 생성, 정보 get api 생성
  - 소그룹 생성 후 RDB가 아닌 Redis에 저장

### 어려웠던 점

- 소그룹 리스트를 get할 때 redis에서 가져와야 하는데 key값이 있는지 boolean을 return해야하는 `redisTemplate.hasKey(key)` 메서드가 null을 출력함
  - 이유는 3가지이다.
    1. key 가 없음
    2. pipeline 사용 중인 상태
    3. 트랜잭션이 걸려있는 상태

### 새로 알게 된 점

- 파이프라인이나 트랜잭션에서 redis 명령을 실행하면 명령의 결과가 즉시 반환되지 않고 커맨드 버퍼에 저장이 돼서 해당 값이 트랜잭션이 커밋되거나 롤백이 되야 해당 값이 반환되는데 트랜잭션이 진행중인 상태에서는 버퍼에 남아있어 아무 결과값이 없는 null이 나오는 것이라고 한다.
- 나는 트랜잭션이 걸려있는 상태였다

```java
@Bean
    public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return redisTemplate;
    }
```

- 관련 링크
  [https://velog.io/@greentea/spring-RedisTemplate.hasKey값이-null이-나올-경우](https://velog.io/@greentea/spring-RedisTemplate.hasKey%EA%B0%92%EC%9D%B4-null%EC%9D%B4-%EB%82%98%EC%98%AC-%EA%B2%BD%EC%9A%B0)
  https://stackoverflow.com/questions/70032606/redis-haskey-method-return-null

### 내일 할 것

- 프론트와 연결 후 오류 수정

## 20240210-11

### 주말동안 한 것

- S3에서 파일 삭제 오류 수정
  - key값을 파라미터로 넣어야 했는데 냅다 url을 넣으니 안됐던 것이었다
- redis cache로 photoPath 추가, 조회, 삭제 성공

### 어려웠던 점

- redis cache에서 조회, 삭제는 잘 되는데 추가가 계속 안됐다.

### 새로 알게된 점

- `@Cache` 어노테이션들은 외부에서 불려지는 메서드에서만 작동한다. - 이유는 프록시 모드가 기본이기 때문에 내부 호출 메서드에서는 작동하지 않는다. - 그래서 추가가 안됐던 것이었음... - 내부에서 불려지는 메서드에 캐싱을 하면 적용되지 않는다 - https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/cache.html#cache-annotation-enable - 여길 보면

  > In proxy mode (which is the default), only external method calls coming in through the proxy are intercepted. This means that self-invocation, in effect, a method within the target object calling another method of the target object, will not lead to an actual caching at runtime even if the invoked method is marked with @Cacheable - considering using the aspectj mode in this case.

  프록시모드에서는 외부에서 호출되는 메서드에서만 사용가능하다고 한다

### 내일 할 것

- 롤링페이퍼 프론트와 연결
- 시간이 되면 jwt에 대한 설명 듣기

## 20240209

### 오늘 한 것

- s3 삭제 메서드 오류 해결
- queryDsl 사용하는 repository 생성

### 어려웠던 점

- QueryDsl의 q class가 자꾸 `cannot resolve symbol QPhoto` 오류를 뜨며 클래스 import를 할 수가 없었다
  - build-generated가 source 폴더로 해야함
  - test에서는 위의 방식으로도 됨. 근데 main에서 안돼서 밑의 링크들을 다해보고도 안됐다...근데 갑자기 또 돼서 잘 사용함
  - https://ottl-seo.tistory.com/entry/IntelliJ-Cannot-resolve-symbol-%EC%97%90%EB%9F%AC-%ED%95%B4%EA%B2%B0
  - https://jong-bae.tistory.com/43
  - https://pamyferret.tistory.com/11

### 새로 알게 된 점

- QueryDsl
  - JPAQueryFactory Config 생성
    - https://batory.tistory.com/496
  - queryDsl select한 것 dto에 저장
    - https://icarus8050.tistory.com/5

### 내일 할 것

- 캐시에 저장하는 값이 삭제 후 새로고침이 되지 않는 오류 수정

## 20240208

### 오늘 한 것

- 웹소켓 오류 해결
- backend-deploy 브랜치 내의 파일 정리해서 오류나는 것 해결
- s3 삭제 메서드 생성

### 새로 알게 된 점

- 웹소켓 오류

  - `websocket connection to 'wss failed` 이런게 계속 났음
  - 근데 http_port를 80, https_port를 443으로 바꾸니 됨
  - 관련 링크

    - https://stackoverflow.com/questions/32693376/websocket-connection-on-wss-failed

    - https://stackoverflow.com/questions/34132419/websocket-connection-to-wss-failed-error-in-connection-establishment-n

- Docker 로그 보는 법
  1. pem키가 있는 곳에서 git bash를 켠다
  2. ssh -i [키 파일 이름] ubuntu@[도메인 이름] 를 입력
  3. docker ps 입력
  4. 원하는 컨테이너 아이디 복사
  5. docker logs <ID값> 입력

### 내일 할 것

- s3 삭제 오류 해결

## 20240207

### 오늘 한 것

- openvidu url 제대로 찾아서 설정
- 롤링페이퍼 테이블 auto-increment 문제 해결

### 어려웠던 점

#### auto_increment가 사라짐

- 롤링페이퍼 저장할 때마다 `field 'id' doesn't have a default value` 이 오류 났음
  - 이유는 auto-increment가 설정되지 않아서→`@GeneratedValue(strategy = GenerationType.IDENTITY)` 가 mysql에 적용이 되지 않을 때가 있음
    - 그러면 `@GeneratedValue(strategy = GenerationType.AUTO)` 로 변경해야 한다고 함
    - 오히려 auto_increment가 들어가지 않음
    - 별 짓 다 해보다가 table drop하고 다시 생성하니까 됨ㅎ

### 새로 알게 된 점

- 오픈비두 url은 http://우리도메인:https포트로 해야함
- 우리는 http://mokkoji.online:5443으로 하니까 됐다
- 포트가 너무 많아서 어려움...

### 내일 할 것

- 오픈비두 배포

## 20240206

### 오늘 한 것

- 결과물 프론트와 연결
- 오픈비두 배포 오류 해결 고민

### 어려웠던 점

#### Result 테이블의 image의 기본값을 `@ColumnDefault`로 하면 에러가 남

```java
You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near 'url,
```

가 떴음

- 어떻게 해도 떠서
  `@Column(length = 255, columnDefinition = "varchar(255) default 'url'")` 로 변경
- event와 result가 연관관계로 얽혀있어서 [`object references an unsaved transient instance - save the transient instance before flushing`](https://bcp0109.tistory.com/344) 에러가 나며 테이블 생성이 되지 않음
  - 그래서 `cascade = CascadeType.PERSIST` 으로 해결

### 내일 할 것

- 오픈비두 배포

## 20240205

### 오늘 한 것

- 백 코드 배포 때 오픈비두 오류 해결
- 사진첩 사진 추가 api 생성
- 대표이미지 설정 api 생성

### 어려웠던 점

#### 사진첩 추가를 할 때 List<MultipartFile>을 어떤 어노테이션으로 받아야 할 지 몰라 공부함

- 컨테이너를 올릴 때 `Circular placeholder reference in property definitions` 오류가 났다.
  - 이건 `key=${key}` 와 같은 경우일 때 일어나는 오류다
  - 스프링 자체에서 키-밸류의 이름을 같은 것으로 하면 안된다고 함

### 새로 알게 된 점

- `@RequestPart`
  - 만약 파라미터가 String이나 MultipartFile/Part가 아니면 HttpMessageConverters에 의존해 request의 헤더의 Content/Type으로 변환
  - 여러 종류의 파라미터가 있는 경우 사용됨
- `@RequestParam`
  - 만약 파라미터가 String이나 MultipartFile/Part가 아니면 등록된 Converter나 PropertyEditor로 변환함
  - name-value form 환경에서 사용됨
  - **List<MultipartFile>을 request로 받는 것을 이걸로 채택함**
- `@RequestBody`
  - HttpMessageConverter를 이용해 Java 객체로 변환
  - 주로 Json 형태를 변환함
  - `@Valid` 를 이용해서 자동 유효성 검사 적용
- `@ModelAttribute`
  - default 설정
  - 생성자나 Setter로 request를 객체로 변환(HttpMessageConverter 사용X)

### 내일 할 것

- 프론트와 연결

## 20240203-04

### 주말동안 한 것

- Openvidu 프론트-백 연결 80퍼센트 성공
  - 백은 더 할 것은 없어보인다
- redis write 전략을 write-behind에서 write-around로 변경.
  - 때문에 동시성이슈가 사라져 redisson을 버리고 lettuce를 사용하기로 함

### 어려웠던 점

- 프론트와 연결할 때 끝없는 오류가 정말 갑갑했다. 그치만 다 해결함 얏호
- redis의 write-back 방식을 사용해야 한다는 생각에 사로잡혔을 때 이 방식을 cache와 연결해서 사용하는 과정을 완벽히 이해하지 못해 코드가 중구난방으로 적혔었다. 이제는 데이터 보존을 위해 write-around를 사용해 걱정할 일이 없음

### 내일 할 것

- 롤링페이퍼 프론트와 연결
- 시간이 되면 jwt에 대한 설명 듣기

## 20240202

### 오늘 한 것

- 백-프 오픈비두 합침
- 수정 중
- 롤링페이퍼 get 생성

### 어려웠던 점

- 자꾸 CORS 에러가 나고 포트가 너무 많아서 뭐가 뭔지 헷갈림

### 새로 알게 된 점

- 8080→ 이건 백의 서버포트임
  - 포스트맨에서는 이 포트로 연결이 되어야함
- 4443→ 이건 프론트의 연결하는 포트
  - localhost:4443을 통해 들어와야 한다.

```java
Access to XMLHttpRequest at 'http://localhost:8080/meetings/api/sessions' from origin 'http://localhost:5173' has been blocked by CORS policy: Response to preflight request doesn't pass access control check: No 'Access-Control-Allow-Origin' header is present on the requested resource.
```

- 이건 CORS에러여서 프론트에서 프록시 설정을 하니까 해결됨

### 주말동안 할 것

- 롤링페이퍼 CRUD 완성
- 백-프 합쳐보기

## 20240201

### 오늘 한 것

- backend 브랜치에 머지 후 컨플릭, 컨벤션에서 벗어나는 부분 수정
- 결과물 편집 메서드 생성
- redis 객체 안의 객체 넣어서 올릴려고 했는데 stackoverflow났음...

### 어려웠던 점

- redis photo 안의 연관관계로 result를 넣었는데 result와 event의 연관관계로 stackoverflow가 남
- 그래서 다시 원래대로 돌아갔다. 그냥 레포지토리 쓰기로 함

### 새로 알게 된 점

- Builder 사용법 제대로 알게 됨
  - https://stackoverflow.com/questions/67945674/how-can-we-add-additional-constructor-when-builder-pattern-is-being-used

### 내일 할 것

- result 편집 가져오기 끝내기
- master 브랜치에 머지
- 평가 듣기

## 20240131

### 오늘 한 것

- redisson write-behind 성공
- redis 남은 정보 가져와서 저장 성공
- 지금 write-behind하고 데이터 삭제 성공

### 어려웠던 점

- redisson으로 write-behind하는 게 정말 생소해서 어려웠다...
- 남은 정보를 가져오는 점이 정말 어려웠음
- 지금은 write-behind할 때 지우는 것도 어려웠다

### 새로 알게 된 점

- redisson 이용 방법. 특히 RMapCache를 이용해 Write-Behind하기

  - config가 정말 중요했다.

    ```java
    @Configuration
    @AllArgsConstructor
    public class CacheConfig {

        private final RedissonClient redissonClient;
        private final PhotoRepository photoRepository;

        @Bean
        public RMapCache<String, Photo> photoRMapCache() {
            final RMapCache<String, Photo> photoRMapCache
                    = redissonClient.getMapCache("photos", MapCacheOptions.<String, Photo>defaults()
                    .writer(getPhotoMapWriter())
                    .writeMode(MapOptions.WriteMode.WRITE_BEHIND)
                    .writeBehindBatchSize(100)
                    .writeBehindDelay(1000));

            return photoRMapCache;
        }

        private MapWriter<String, Photo> getPhotoMapWriter() {
            return new MapWriter<String, Photo>() {
                @Override
                public void write(Map<String, Photo> map) {
                    map.forEach((k, v) -> {
                        photoRepository.save(v);
                    });
                }

                @Override
                public void delete(Collection<String> keys) {
                    // TODO : 2024.01.31 url로 삭제 시 삭제되게 하고싶은데 이상함
                    keys.stream().forEach(key -> {
                        photoRepository.deleteByUrl(key);
                    });

                }
            };
        }

    }
    ```

### 내일 할 것

- 프론트와 연결
- 오류 수정
- 배포

## 20240130

### 오늘 한 것

- redis에 사진, 롤링페이퍼 정보 list로 저장하는 것 성공
- S3 폴더 구조 변경
- `PhotoRedis cannot be cast to class java.lang.String (online.mokkoji.event.domain.PhotoRedis is in unnamed module of loader 'app'; java.lang.String is in module java.base of loader 'bootstrap')` 해결
- 분산락을 위한 redisson 설치 후 셋팅

### 어려웠던 점

- 분산락의 개념이 뭔지 이해가 잘 가지 않았음
- 저 에러가 뭐때문인지 처음에 잘 이해가 가지 않음
- redisson 셋팅이 아직 잘 되지 않음

### 새로 알게 된 점

- 분산락이 무엇인지
  - 경쟁 상황(Race Condition) 이 발생할때, 하나의 공유자원에 접근할때 데이터에 결함이 발생하지 않도록 원자성(atomic) 을 보장하는 기법
- `PhotoRedis cannot be cast to class java.lang.String (online.mokkoji.event.domain.PhotoRedis is in unnamed module of loader 'app'; java.lang.String is in module java.base of loader 'bootstrap')`
  - 이건 내가 object를 objectMapper로 String화 시키지 않아서 그랬던 것이다. 근데 serialization을 아예 dto에서 해도 괜찮을 듯

### 내일 할 것

- redisson으로 write-back 성공
- 프론트와 연결
- 배포

## 20240129

### 오늘 한 것

- 경배님과 백엔드 merge
- merge 후 에러 수정
- 패키지 구조 수정
- 코드 스타일 정함
  - 엔티티는 builder 사용
  - 서비스는 인터페이스, impl사용
  - 컨트롤러에 있는 유효성 검사 전부 서비스에서 하기
  - 유저 정보는 전부 jwt로 받아오기
- 롤링페이퍼 s3올리는 것 성공
- redis에 연결하려 했으나 실패

### 어려웠던 점

- merge 후 컨벤션 정하는 데 시간이 오래걸림
- redis에 객체를 리스트로 넣고 싶은데 되지를 않는다...

### 새로 알게 된 점

- builder에 대해 새로 알게 됨
- jpa entity @AllArgsConstructor 사용 가넝(나는 루프에 빠지는 줄 알았음)

### 내일 할 것

- redis 연결
- 소그룹 session 열기
- 배포

## 20240127-28

### 주말동안 한 것

- S3 IAM 계정 생성
- S3로 업로드하는 Event API 생성

### 어려웠던 점

- Multipart/file과 json을 같이 받는 것
  - `@RequestPart`를 사용하면 된다.
  - 특이점은 userId하나만 json에서 받고 싶더라도 dto로 감싸줘야 인식을 했음
- application-s3.yml에 S3 설정 내용을 담았는데 안됨...
  - `Could not resolve placeholder 'cloud.aws.credentials.access-key' in value "${cloud.aws.credentials.access-key}"`
  - 그래서 그냥 application.properties에 담으니까 됐음

### 내일 할 것

- Event API 끝내기
- redis에 연결
- 소그룹 session 생각하기

## 20240126

### 전날 저녁에 한 것

- OpenVidu on-premise 방식으로 배포 시도. 서버가 열려있지 않아 실패

### 오늘 한 것

- 3주차 발표 경청
- OpenviduController 에러 처리 후 서비스로 에러처리하라는 코드리뷰 받아서 수정

### 어려웠던 점

- 배포할 때 port번호 설정에 어려움

### 새로 알게 된 점

- 예외처리는 controller가 아닌 service단에서 한다
- server port, http_port, https_port는 전부 다르다

### 주말에 할 것

- OpenVidu 배포
- redis와 연결
- 채팅 알아보기

## 20240125

### 오늘 한 것

- openvidu controller 뼈대 완성(exception 처리 추가 필요)

### 내일 할 것

- 서버에 업로드
- exception 컨트롤러에 추가
- 카카오페이 api 추가
- 소그룹 어떻게 하냐,,생각하기
- 채팅 한번 구경하기
- 설문조사 메서드 생성

## 20240124

### 오늘 한 것

- openvidu session 생성 repo, sevice 성공
- Event, Result 테이블 정규화 후 entity 생성
- maven 3.9.0 설치
- openvidu 커스텀 시작

### 어려웠던 점

- 오픈비두 튜토리얼을 커스텀하려고 하니까 되지 않아서 docker에서 pull해와서 커스텀 시작
- maven 설치해야하는 줄 모르고 mvn 왜 안되나 했음

### 새로 알게 된 점

- JPA entity에서 enum column은 기본값을 주려면 하드코딩하는 법 밖에 없다
- - Intellij 글자 오류
    - `Alt+=` 하면 알파벳이 하나하나 떨어져서 나옴

### 내일 할 것

- OpenVidu 컨트롤러 만들기

## 20240123

### 오늘 한 것

- openvidu session post 연결 성공
- event controller, repo, service 생성
- gerrit, 컨플릭과 기나긴 싸움…………………………………………………

### 어려웠던 점

- 엔티티 수정 때 오류가 남
- h2 db는 user라는 테이블명을 가질 수 없다. 이것때문에 2시간 날림
- dto 생성이 어렵다

### 내일 할 것

- OpenVidu 컨트롤러, 서비스, 레포 끝내기

## 20240122

### 오늘 한 것

- Entity 생성

### 어려웠던 점

- 양방향, 단방향 설정을 뭐로 해야할지 헷갈렸음

### 내일 할 것

- OpenViduController 완성

## 20240120-21

### 주말동안 한 것

- redis 드디어 연결
- OpenVidu api 명세서 작성

### 어려웠던 점

- entityManagerFactory를 Bean으로 못 만든 오류
  - h2가 꺼져있어서 그랬다...^^
- redisConfig를 Bean으로 못 만든 오류

```java
@Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;
```

이걸 `@Value("${spring.data.redis.host}")` 이렇게 했어야 했음!!!!!!! yml을 무시하지 말자..
여기서 끝이 아님

- org.hibernate.exception.sqlgrammarexception: could not prepare statement [sequence "hello_seq" not found; sql statement:

```yml
jpa:
  hibernate:
    ddl-auto: create
```

여기서 jpa: 를 안해서 ddl-auto가 적용이 안돼서 sequence를 생성하기 못했던 것임;;;;

### 내일 할 것

- Entity 생성
- OpenVidu 시작

## 20240119

### 오늘 한 것

- 패키지 생성
- docker 설치
- redis 공부
- redis config 실패

### 어려웠던 점

- redis 연결을 할 때 redisConfig가 계속 연동이 되지 않음... 계속 entityManagerFactory가 없다고 뜸...

### 주말동안 할 일

- 초기 세팅 완료
- OpenVidu 작업 시작

## 20240118

### 오늘 한 것

- 피그마 만들기
- api 명세서 아주 조금 수정
- 브랜치 생성
- 백엔드 프로젝트 생성

### 어려웠던 점

- api response body parameter에 대한 이해
- 리모트 브랜치를 로컬 브랜치로 가져오기

### 내일 할 것

- redis, querydsl 초기 셋팅
- webRTC 공부
- webRTC 생성

## 20240117

### 오늘 한 것

- 기술 스택 정하기
- 기능 담당 정하기
- 컨설턴트님 피드백
- 쿼리 DSL 공부

### 어려웠던 점

- 없음

### 내일 할 것

- 피드백 회의(밥 먹기 전 끝내보기)
- 와이어프레임 생성
- 피그마 수정
- 명세서 수정
- api 명세서 수정
  - 간단한 규칙에 맞게 수정
- erd 수정

## 20240116

### 오늘 한 것

- ERD 피드백
- API 명세서 작성
- Jira 공부(너무 어려움 미쳤음)
  - 지라 규칙 생성
  - 스토리 포인트 생성 배움

### 어려웠던 점

- 지라가 아직 헷갈린다...

### 내일 할 것

- 기술 스택 정하기
- 기능 별 파트 분배
- 기획 마무리
- 도커 공부
- S3 공부
- Redis 공부
- 쿼리 DSL

## 20240115

### 주말동안 한 것

- 화상 화면 피그마 완성

### 오늘 한 것

- 피그마 완성
  - 피그마 피드백 받은 것 수정
- 개발 컨벤션 정리
- 지라 깃랩 연동

### 어려웠던 점

- 컨벤션을 정리할 것이 많아서 헷갈림

### 내일 할 것

- api 명세 작성
- 도커 공부
- S3 공부
- Redis 공부
- 쿼리 DSL

## 20240112

### 오늘 한 것

- 피그마 완성
  - 소셜 로그인
  - 화상 모바일 화면
  - 소그룹
  - 송금, 메시지 작성 모달
- 피그마 피드백 받기
- 기획서 피드백하기

### 어려웠던 점

### 주말에 할 것

- 도커 공부
- 지라 공부
- 깃랩 공부
- S3 공부
- Redis 공부
- 쿼리 DSL

## 20240111

### 오늘 한 것

피그마팀, 기획서팀을 나눠서 진행. 나는 피그마팀.

- 완성
  - flow chart
  - 화상 메인
  - 소그룹
  - 송금, 메시지 작성 모달
- 진행 중
  - 소셜 로그인 모달

### 내일 할 것

## 20240110

- 규칙 정함
  - 깃 컨벤션
  - 파트 나누기
  - 기술스택
  - 그라운드룰 추가
  - 기능 리스트업
- 디자인 컨셉
  - 색상
  - 폰트
  - 레이아웃

## 20240109

- 피그마 목업
- 기획서 작성

## 20240108

- 기능 리스트업
- 와이어 프레임 작성
- 디자인 시스템 생성