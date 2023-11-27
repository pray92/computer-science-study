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

(TODO)

# Reference
- [[Redis] redis.conf 설정 파일 주요 옵션 정리](https://velog.io/@inhwa1025/Redis-redis.conf-설정-파일-주요-옵션-정리)