# final 사용 이점

## 요약

- 불변 객체는 객체 생성 이후 내부 상태가 변하지 않는 객체이며, read-only 메서드만 제공하거나 방어적 복사를 통해 메서드를 제공한다
- Thread-safe하여 병렬 프로그래밍에 유용하고 동기화를 고려하지 않아도 된다
- 실패 원자적인(Failure Atomic) 메서드를 만들 수 있다
- Cache나 Map 또는 Set 등의 요소로 활용하기에 적합하다
- 부작용(Side Effect)를 피해 오류 가능성을 최소화한다
- 다른 사람이 작성한 함수를 예측 가능하여 안전하게 사용할 수 있다 -> 무분별한 사용을 막아 찾기 힘든 버그를 사전에 방지
- GC 성능을 높일 수 있다

---

## 불변 객체

- 객체 생성 이후 내부의 상태가 변하지 않는 객체
- 불변 객체는 read-only 메서드만을 제공
- 객체의 내부 상태를 제공하는 메서드를 제공하지 않거나 방어적 복사`defensive-copy`를 통해 제공
- 대표적인 불변 객체로 `String`이 있음
  ```java
  // 해당 메서드는 String 요소 배열을 반환한다
  // 참조를 통한 변경을 막기 위해 내부를 복사하여 전달, 방어적 복사를 수행한다
  public char[] toCharArray() {
    // Cannot use Arrays.copyOf because of class initialization order issues
    char result[] = new char[value.length];
    System.arraycopy(value, 0, result, 0, value.length);
    return result;
  }
  ```

## 1. Thread-safe하여 병렬 프로그래밍에 유용, 동기화 고려할 필요가 없다
- Multi-thread 환경에서 동기화 문제가 발생하는 이유는 공유 자원에 동시 쓰기`write` 때문
- 만약 공유 자원이 불변이면 동기화 고려가 필요함
- 안정성 보장 뿐만 아니라 동기화 처리를 하지 않아 성능상 이점도 존재

## 2. 실패 원자적인(Failure Atomic) 메서드를 만들 수 있다
- 가변 객체를 통해 작업하는 도중 예외 발생 시 해당 객체가 불안정 상태에 빠질 수 있음
- 불안정 상태의 객체는 또 다른 에러를 유발
- 하지만 불변 객체라면 어떤 예외가 발생하더라도 메서드 호출 전 상태를 유지
- 예외가 발생하더라도 오류가 발생하지 않은 것처럼 다음 로직 처리가 가능

## 3. Cache나 Map 또는 Set 등의 요소로 활용하기 적합하다
- `Cache`, `Map`, `Set` 등의 원소인 가변 객체가 변경되면 이를 갱신하는 등의 부가 작업이 필요
- 불변 객체라면 한번 데이터가 저장된 이후에 다른 작업들을 고려하지 않아도 되므로 사용하는데 용이하게 작용

## 4. 부작용(Side Effect)를 피해 오류 가능성을 최소화한다
- 부작용이란 변수의 값이나 상태 등 변환가 발생하는 효과를 의미
- Setter를 통해 여러 객체들에서 값을 변경하면 객체 상태를 예측하기 어려워짐
- 바뀐 상태 파악을 위해선 메서드를 살펴봐야하고 이는 유지보수성을 떨어뜨림
- 불변 객체는 기본적으로 값의 수정이 불가능하므로, 변경 가능성이 적어 객체의 생성과 사용이 상당히 제한됨
- 그렇기 대문에 메서드가 호출되어도 객체의 상태가 유지되기 때문에 안전하게 객체를 다시 사용할 수 있음
- 불변 객체는 오류를 줄여 유지보수성이 높은 코드를 작성하도록 도와줌

## 5. 다른 사람이 작성한 함수를 예측 가능하여 안전하게 사용할 수 있다
- 개발은 협업으로 이뤄짐 -> 불변성은 협업 과정에서 많은 도움 줌
- 불변성이 보장되면 다른 사람이 개발한 함수를 위험없이 사용할 수 있음
- 마찬가지로 다른 사람도 내가 작성한 메서드를 호출하여도, 값이 변하지 않음을 보장받을 수 있음
- 그러므로 서로 변경에 대한 불안 없이 다른 사람의 코드를 이용할 수 있음

## 6. 가비지 컬렉션의 성능을 높일 수 잇다
- 불변의 객체는 한번 생성된 이후 수정이 불가능한 객체
- Java에서는 `final` 키워드를 사용해서 불변 객체 생성이 가능
- 이렇게 객체를 생성하기 위해선 객체를 가지는 컨테이너 객체도 존재한다는 것을 의미
- 당연히 불변 객체가 먼저 생성되어야 컨테이너 객체가 이를 참조할 수 있음
  - 불변 객체를 가지고 있는 불변 컨테이너 객체는 인스턴스 생성 시 반드시 해당 객체를 초기화해야 함
    ```java
    public class MutableHolder {
        private Object value;   
        public Object getValue() { return value; }
        public void setValue(Object o) { value = o; }
    }
    
    public class ImmutableHolder {
        private final Object value;
        public ImmutableHolder(Object o) { value = o; }
        public Object getValue() { return value; }
    }
    
    @Test
    public void createHolder() {
        // 1. Object 타입의 value 객체 생성
        final String value = "redgem92";
    
        // 2. Immutable 생성 및 값 참조
        final ImmutableHolder holder = new ImmutableHolder(value);
    }
    ```
  - 이러한 점은 GC 수행 시 가비지 컬렉터가 컨테이너 하위 불변 객체들을 skip할 수 있게 도와줌
  - 해당 컨테이너 객체`ImmutableHolder`가 살아있다는 것은 하위 불변 객체`value` 역시 처음 할당된 상태로 참조되고 있음을 의미하기 때문
  - 결국 불변 객체를 사용하면 가비지 컬렉터가 스캔해야 하는 메모리 영역과 빈도 수 역시 줄어듦
  - GC가 수행되어도 지연 시간을 줄일 수 있음 => 필드 값을 수정할 수 없도록 사용하는 것이 좋음
  - 물론 위 예제처럼 `MutableHolder`처럼 값이 바뀌는 경우라면 이걸 사용하는 것이 낫지 않냐는 이야기가 있음
  - 하지만 GC는 새롭게 생성된 객체는 대부분 금방 죽는다는 `Weak Generational Hypothesis` 가설에 맞추어 설계되었음
  - GC 입장에선 생명 주기가 짧은 객체를 처리하는 것은 문제가 없으며, 오히려 `MutableHolder`의 값이 지속되어 `old-to-young` 참조가 일어나는 것이 더 큰 성능 저하를 야기함

# Reference

[[Java] 불변 객체(Immutable Object) 및 final을 사용해야 하는 이유](https://mangkyu.tistory.com/131)