# Garbage Collector

## 요약
- Garbage Collector(GC)는 JVM에서 자동으로 heap 메모리를 관리하는 기능이다
- GC는 기본적으로 Mark and Sweep Compact 알고리즘을 거친다
  - Marking: GC Root로부터 참조되는 객체와 그 reachable 객체가 참조한느 객체를 찾는다
  - Sweep: Marking 이후 marking되지 않은 unreachable 객체를 메모리에서 해제한다
  - Compact: 살아 있는 영역을 조각 모음한다
- GC Root 대상은 heap 영역 내 다른 객체 참조가 있는 경우 stack 영역의 지역 변수, method 영역에 static 변수에 대한 참조가 있는 경우 그리고 JNI에 의해 생성된 객체에 의한 참조가 있는 경우다
- Heap 영역은 `Young`(Eden, Service0/1), `Old` Generation 영역으로 구성된다
  - MinorGC는 `Young` Generation에서 발생한다
  - MajorGC는 `Old` Generation에서 발생한다
- Stop-the-world(STW)는 GC 수행을 위해 모든 애플리케이션 스레드가 일시정지하는 현상이다
- GC 튜닝의 목적은 MajorGC의 STW 시간 단축이다
- GC 종류
  1. Serial GC: 싱글 스레드로 동작하는 GC
  2. Parallel GC: MinorGC에서만 멀티 스레드로 동작하는 GC
  3. Parallel Old GC: MajorGC에서도 멀티 스레드로 동작하는 GC
  4. Concurrent Mark and Sweep(CMS) GC: MajorGC에서 동시적으로 mark/sweep/compact를 거치며, 시스템 자원 소모가 커 JDK 1.14부터 사장되었다
  5. G1 GC: Heap 영역 전체를 1~32MB의 '동일한' 크기의 region으로 나눠 처리하는 GC다
  6. ZGC: Heap 영역을 small(2MB), medium(32MB), larg(2 * n MB)로 동적으로 region(ZPage)를 나눠 처리하는 GC다

---

- GC는 JVM에서 자동으로 heap 메모리를 관리하는 기능

## Mark and Sweep Compact

![img.png](img.png)

### 1. Marking
- GC Root로부터 참조되는 객체와 그 reachable 객체가 참조하는 객체를 찾음

### 2. Sweep
- Marking 과정이 끝나면 marking되지 않은 unreachable 객체를 메모리에서 해제

### 3. Compact
- 살아 남은 영역을 조각 모음

## GC Root 대상

1. Heap 영역 내 다른 객체 참조가 있는 경우
2. Stack 영역의 지역 변수, 파라미터에 대한 참조가 있을 경우
3. Method 영역의 static 변수에 의한 참조가 있을 경우
4. JNI(Java Native Interface)에 의해 생성된 객체에 의한 참조가 있을 경우

## GC 실행 과정

![img_1.png](img_1.png)

- Heap 여역은 Young Generation(Eden, Service 0, Service 1) 그리고 Old Generation으로 구성
  - MinorGC: Young Generation에서 발생하는 GC
  - MajorGC: Old Generation에서 발생하는 GC
---
1. 객체가 인스턴스화되면 `Eden` 영역에 생성
2. `Eden` 영역이 한계치 초과
   1. MinorGC 발생
   2. `Eden`에서 살아남은 객체들은 `Service0` 영역으로 이동
   3. 만약 `Eden` 영역의 살아있는 객체가 `Service`보다 클 경우 바로 `Old` 영역으로 이동
3. `Service 0` 영역의 한계치 초과
   1. MinorGC 발생
   2. 살아남은 객체들은 Service 1 영역 이동
   3. `Service` 영역 간 살아 남은 객체들의 이동이 반복되며, `Service` 영역 중 한 영역은 항상 비어 있게 됨
   4. 이처럼 MinorGC 발생 시 살아남은 객체들은 영역을 이동하면서 age-bit가 1씩 증가
4. `Promotion`: Age-bit가 MaxTenuringThreshold 설정 값 초과 시 Old Generation 이동

## Stop-the-world
- GC 발생 시 GC를 실행하는 스레드를 제외한 모든 애플리케이션의 스레드가 GC 종료 전까지 일시정지하는 것을 말함
- 모든 스레드가 GC 종료 전까지 일시정지되기 때문에 성능에 많은 영향을 끼침
  - MinorGC보다 실행 속도가 느린 MajorGC에서 문제가 발생
- MajorGC의 STW 시간을 줄이기 위한 알고리즘이 나오고 있음
- 보통의 경우, GC 튜닝은 **MajorGC의 STW 시간을 단축하는 작업**

## GC 종류

### Serial GC

- 싱글 코어에서 사용된 GC
- 싱글 스레드로 동작하기 때문에 STW 시간이 길고 느림
- 대신 시스템 자원을 덜 사용
- Mark and Sweep Compact 알고리즘 사용

### Parallel GC(JDK 1.7 ~)

- Minor GC에서만 멀티 스레드로 동작 => 속도 개선
- Mark and Sweep Compact 알고리즘 사용

### Parallel Old GC

- Major GC에서도 멀티 스레드로 동작
- Minor GC는 Mark and Sweep Compact 알고리즘 사용
- Major GC는 Mark and Summary Compact 알고리즘 사용
  > #### Sweep vs. Summary
  > - `Sweep`: 싱글 스레드가 old 영역 전체를 훑어 살아있는 객체만 찾아냄
  > - `Summary`: 멀티 스레드가 old 영역을 분리하여 훑음

### Concurrent Mark and Sweep(CMS) GC

- STW 방식이 매우 짧다는 장점이 있으나 그만큼 시스템 자원(메모리, CPU)를 많이 사용
- 더이상 메모리 할당이 어려울 때까지 Compaction을 미루기 때문에 더 긴 STW 발생
- JDK 1.14부터 사용 중지
---
1. Young Generation에선 Parllel GC와 동일
2. Old Generation에선 다음 과정을 거침
   1. Intial Mark`STW`
      - GC Rootdㅔ서 가장 가까운 객체 중 살아있는 객체를 찾아 marking -> `STW` 짧음
   2. Concurrent Mark
      - Initial Mark 단계에서 살아남은 객체의 참조를 따라가며 살아있는 객체를 찾음
      - 이때 여러 개의 스레드가 동작
   3. Remark`STW`
      - Concurrent Mark를 수행하는 동안 객체의 참조가 끊기거나 새롭게 생긴 객체가 없는지 다시 한번 확인
   4. Concurrent Sweep
      - 참조되지 않는 객체를 메모리에서 해제
      - 애플리케이션 스레드와 동시에 진행
   5. Compact`STW`
      - 최대한 미루다 더이상 메모리 할당이 어려울 경우 실행

### G1 GC(JDK 1.9 ~)

![img_2.png](img_2.png)

- Heap 영역 전체를 1~32MB의 동일한 크기의 region으로 나눔
- 각 region이 Eden, Survivor, Old, Available/Unused 역할 수행
  1. Eden: 다른 GC의 Eden과 동일, 새로 생긴 객체들 할당
  2. Survivor: 살아 있는 객체들이 할당
  3. Old: 오래 살아있는 객체들이 할당
  4. Humongous: region 크기의 50%가 넘는 큰 객체들 할당
  5. Available/Unused: 아직 사용되지 않는 영역
- Garbage가 꽉 찬 지역을 우선적으로 GC가 동작하게 됨
- Multi-core CPU, 대용량 메모리 시스템을 위한 GC
- 성능 튜닝
  - 튜닝의 목적은 GC에 걸리는 시간을 최소화하는 것
  - `-XX:InitiatingHeapOccupancyPercent`: IHOP(Marking에 해당하는 최저 임계치) 조절
  - `-XX:G1HeapRegionSize`: 지역당 하나의 사이즈(Default: 최대 heap / 2048)
  - `-XX:G1ReservePercent=10`: 공간 overflow 위험을 줄이기 위해 항상 여유 공간을 유지할 예비 메모리(백분율)
  - `-XX:G1HeapWastePercent=10`: 낭비할 heap 공간에 대한 백분율
---
1. G1 Young GC
   - 이저 GC들과 마찬가지로 객체는 제일 처음 Eden 지역에 생성
   - Minor GC를 거쳐 살아남은 객체들은 Survivor 지역으로 이동된 후 Eden 지역은 Available/Unused로 변경됨
2. G1 Old GC

   ![img_3.png](img_3.png)
   1. Initial Mark`STW`
      - Old region의 객체들이 참조하는 객체를 Survivor region에서 찾음
   2. Root Region Scan
      - 1번 과정에서 살아남거나 old generation의 객체로부터 참조되거나 참조된 객체로 mark된 객체가 있는 survivor region을 스캔
      - 애플리케이션 스레드와 동시 실행
   3. Concurrent Mark
      - 전체 heap에서 GC 대상이 될 region을 mark
   4. Remark`STW`
      - 3번 과정에서 GC 대상이라 판단된 region 메모리 해제 -> Available/Unused로 변경
   5. Clean up`STW`
      - 살아남은 객체가 가장 적은 region의 미사용 객체를 제거
   6. Copy`STW`
      - 5번 과정에서 완전히 비어지지 않은 region의 살아남은 객체들을 Available/Unused 지역으로 복사하여 Compaction 수행
      > #### Shenadonah GC
      > G1 GC에서 Copy 과정을 배제해 최적화 수행하는 GC로 보임. JDK 1.14부터 나왔으며 아직까진 Experimental 단계
   

### ZGC(JDK 1.11 ~)


# Reference

[Java GC Gargabe Collection 알짜만 빼먹기 / 알고리즘 / 종류 / 모니터링 VisualVM](https://aljjabaegi.tistory.com/636)

[JAVA의 Garbage Collection을 소개합니다](https://preamtree.tistory.com/118)

[ZGC에 대해서](https://www.blog-dreamus.com/post/zgc에-대해서)

[JVM과 Garbage Collection - G1GC vs ZGC](https://huisam.tistory.com/entry/jvmgc)
