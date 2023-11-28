# redis.conf

## 네트워크 설정

- `bind`: redis가 bind할 인터페이스(Default: `127.0.0.1`)
- `protected-mode`: bind 설정된 ip만 접속 가능하게 설정하는 모드(Default: `yes`)
- `port`: Client, Slave 서버가 통신할 포트 설정
  - Default: `6379`
  - Cluster 모드로 운영 시, 서버들간의 통신은 기본 포트에 10,000을 더해야 함(ex. port: `6666`, port for cluster: `16666`)
- `tcp-backlog`: redis 서버의 초당 클라이언트 연결 개수(Default: `511`)
- `maxclients`: 최대 접속 가능한 클라이언트 수(Default: `10000`)
- `tcp-keepalive`: 클라이언트가 죽었을 때 써버가 확인하여 클라이언트와의 접속을 제거하는 시간(Default: `300`)

## 일반 설정

- `requirepass`: redis 서버 password(Default: `""`)
- daemonize: redis 서버를 데몬으로 실행할지 여부 설정
  - Default: `no`
  - `no`인 경우, 서버 실행 시 서버 메시지(로그)가 화면에 표시되고 리눅스 프롬프트가 떨어지지 않음 => foreground 실행
  - `yes`인 경우, `background`로 실행되고 리눅스 프롬프트가 바로 떨어짐 => `logfile` 지정 필수, 지정하지 않으면 로그가 남지 않음
- `supervised`: `Systemd` 또는 `upstart`로 redis 서버를 시작했을 때 사용하는 파라미터(Default: `no`)
- `pidfile`: pid 경로 설정
  - Default: `/var/run/redis_6379.pid`
  - `daemonize yes` 설정한 경우 백그라운드로 실행되는 서버의 pid를 해당 파일에서 확인 가능
- `loglevel`: 로그 레벨 설정
  - Default: `notice`
  - 로그 레벨 단계: `debug > verbose > notice > warning`
- `logifile`: 로그 경로 설정(Default: `""`)
- database: DB 개수 설정
  - Default: `16`
  - redis는 여러 개의 DB를 가질 수 있으며, 여기서 DB는 서로 다른 키 공간
  - **cluster 모드에서는 DB가 하나만 존재**
- `always-show-logo`: redis 시작 시 ASCII 로고 출력(Default: `yes`)

## 스냅샷 설정

- `save`: 메모리에 있는 전체 데이터를 디스크에 저장하는 옵션
  - Default: `""`
  - `save <seconds> <changes>`
  - 지정된 시간 동안 지정된 개수 이상의 키가 변경되면 disk에 저장
- `stop-writes-on-bgsave-error`: RDB 저장 실패 시 Redis 데이터 쓰기를 할 수 없도록 설정하는 옵션(Default: `yes`)
- `rdbcompression`: RDB 파일로 덤프 시 텍스트를 압축하여 파일 사이즈를 줄잉(Default: `yes`)
- `rbdchecksum`: RDB 파일이 손상되었는지 확인(Default: `yes`)
- `dbfilename`: RDB 파일 이름 설정(Default: `dump.rdb`)
- `dir`: `dbfilename`으로 설정한 RDB 파일 저장 경로(Default: `./`)
- `rdb-del-sync-files`: redis 서버를 재기동하거나 전체 동기화 후 동기화에 사용된 RDB 파일 삭제 여부(Default: `no`)

## 복제 설정
- `replica-serve-stale-data`: 복제 서버가 마스터와의 연결이 끊어졌을 때 클라이언트 요청 응답 유무 설정(Default: `yes`)
- `replica-read-only`: 복제 서버에 조회 전용 여부 설정
- `repl-diskless-ysnc`: 전체 데이터 동기화 방식 선택
  - Default: `no`
  - `yes` 선택 시 소켓, `no` 선택 시 disk
- `repl-diskless-sync-delay`: 소켓 복제의 경우 여러 복제 요청에 한번에 처리할 수 있도록 전송 시작 시간 설정(Default: `5`)
- `repl-diskless-load`: RDB 파일을 만들지 않고 복제 방법 선택
  - default: `disabled`
  - `on-empty-db`: 복제 서버에 키가 없을 경우 diskless 기능을 사용하지 않음, 데이터가 있으면 rdb 파일 생성
  - `swapdb`: 복제 서버에 키 여부와 상관없이 diskless로 동작
- `repl-disable-tcp-nodelay`: 마스터와 슬레이브 간 전체 동기화 시 데이터를 모아 큰 패킷을 전송할지 여부(Default: `no`)
- `replica-priority`: 마스터 다운 시 여러 슬레이브 중 어느 서버가 마스터가 될지 우선순위를 정함
  - Default: `100`
  - 숫자가 작을 수록 우선순위 높음
  - `0`으로 선택 시 마스터로 선택하지 않음

## LAZY FREEING 설정
> 💡Redis에서 Key(Value)를 삭제하는 방식
> - `DEL`: 명령이 실행되면 바로 데이터를 삭제
> - `UNLINK`: 명령이 실행되면 일단 키는 지우고 남은 멤버는 별도 thread인 LAZY_FREE에서 삭제

- `lazyfree-lazy-eviction`: 키 삭제 시 UNLINK 사용 여부(Default: `no`)
- `lazyfree-lazy-expire`: expire 키 삭제 시 UNLINK 사용 여부
  - Default: `no`
  - `RENAME`: 기존 키와 값을 삭제하고 키 이름을 변경
- `replica-lazy-flush`: 전체 동기화 시 복제 서버가 자신이 가지고 있는 모든 데이터를 Flushall async로 지울지 여부(Default: `no`)

## APPEND ONLY MODE 설정

- `appendonly`: aof 파일 사용 여부(Default: `no`)
- `appendfilename`: aof 파일 이름 설정(Default: `appendonly.aof`)
- no-appendfsync-on-rewrite: AOF/RDB 파일 저장 중에 fsync() 실행 여부
  - Default: `no`
  - AOF 재작성이나 RDB 파일 저장 시 `fsync()`가 실행되면 지연 발생 가능
  - `yes` 설정 시 `fsync()` 실행하지 않음
- `auto-aof-rewrite-percentage`: aof 파일 재작성하는 최소 크기 퍼센트 설정
  - Default: `100`
  - 작은 파일이 자주 재작성되는 것을 방지
- `auto-aof-rewrite-min-size`: aof 파일을 재작성하는 최소 크기 설정(Default: `64mb`)
- `aof-load-truncated`: redis 시작 시 aof 파일을 메모리 로드 중 파일 손상 발생 시 행동 설정
  - Default: `yes`
  - `yes`: 가능한 많은 데이터를 로드하고 관리자에게 알리기 위해 내용을 로그로 올리고 정상적으로 시작
  - `no`: 오류를 남기고 중단
- `aof-use-rdb-peamble`: aof 재작성 시 aof 파일을 rdb 포맷으로 쓰기
  - Default: `yes`
  - 쓰는 시간, 로드 시간, aof 파일 크기 축소 가능

## SLOW 로그 설정
- `slowlog-log-slower-than`: 지정한 실행 시간을 초과하는 쿼리들을 기록에 남김(Default: `10000`(10ms))
- `slowlog-max-len`: slow log에 사용할 메모리 지정(Default: `128`)

## 이벤트 알림 설정
- `notify-keyspace-events`: 키 변경 이벤트를 클라이언트에게 알릴지 설정(Default: `""`)

## ADVANCED 설정
### 메모리 절약을 위한 데이터 구조 설정
- `hash-max-ziplist-entries`: hash에 포함된 field의 수가 설정 값보다 크면 Hash Table 혹은 Zip Map으로 인코딩(Default: `512`)
- `list-max-ziplist-size`: list에 포함된 값이 설정한 값보다 크면 Linked List 또는 Zip List로 인코딩
  - Default: `-2`
  - `-5`: max size 64KB
  - `-4`: max size 32KB
  - `-3`: max size 16KB
  - `-2`: max size 8KB
  - `-1`: max size 4KB
- `list-compress-depth`: list 데이터 압축 설정, 앞뒤 양쪽 노드 n개씩은 압축하지 않고 가운데 노드들을 압축(Default: `0`(모든 노드를 압축하지 않음))
- `set-max-intset-entries`: Set에 포함된 요소의 개수가 설정한 값보다 크거나 정수가 아닌 요소가 하나라도 포함되어 있다면 Hash Table 또는 Int Set으로 인코딩(Default: `512`)
- `zset-max-ziplist-entries`: Sorted Set에 포함된 field의 종류가 설정한 값보다 크면 Skip List 혹은 Zip List로 인코딩(Default: `128`)
- `zset-max-ziplist-value`: Sorted Set에 포함된 field 값 중 가장 긴 값의 길이가 설정한 값보다 크면 Skip List 또는 Zip List로 인코딩(Default: `64`)

### 클라이언트 출력 버퍼 제한
> 💡어떤 이유든 서버에서 데이터를 빨리 읽지 않는 클라이언트의 연결을 강제 해제

- `client-output-buffer-limit`
  - 일반 클라이언트: `normal 0 0 0`
  - Replica 클라이언트: `replica 256mb 64mb 60`
  - Pub/Sub 클라이언트: `pubsub 32mb 8mb 60`
- `hz`: 백그라운드 작업(timeout된 클라이언트 연결 해제, 만료 키 삭제 등) 수행 주기 설정(Default: `10`)
- `dynamic-hz`: 10개 이상의 클라이언트가 연결될 때 필요에 따라 hz의 배수를 사용(Default: `yes`)
- `aof-rewrite-incremental-fsync`: 옵션 활성화 시 ㅁof 파일을 재작성할 때 32mb마다 fsync 수행(Default: `yes`)

# Reference
- [[Redis] redis.conf 설정 파일 주요 옵션 정리](https://velog.io/@inhwa1025/Redis-redis.conf-설정-파일-주요-옵션-정리)