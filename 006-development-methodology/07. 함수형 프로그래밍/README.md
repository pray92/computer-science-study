# 함수형 프로그래밍
## 요약

- 자료 처리를 수학적 함수의 계산으로 취급해 상태와 가변 데이터를 멀리하는 프로그래밍 패러다임이다
    - 객체 지향과 사고 방식이 다르니 이러한 사고 방식을 학습하는 것에 중점을 두어야 한다
- 함수형 프로그래밍의 특징은 순수 함수, 무상태 및 불변성, 선언형 함수 그리고 함수의 1급 객체화하는 것이다
- 높은 수준의 추상화를 제공하고, 함수의 재사용성을 늘리고, 불변성으로 인한 예측이 용이하다, 그러나 무한 루프 및 함수 객체화로 인한 가독성이 저하할 수 있다

---

- 자료 처리를 수학적 함수의 계산으로 취급
- 상태와 가변 데이터를 멀리하는 프로그래밍 패러다임

>💡 함수형 프로그래밍을 배워야하는 이유<br>
>*잠시 당신이 나무꾼이라고 가정해보자. 당신은 숲에서 가장 좋은 도끼를 가지고 있고, 그래서 가장 일 잘하는 나무꾼이다. 그런데 어느 날 누가 나타나서 나무를 자르는 새로운 패러다임인 전기톱을 알리고 다닌다. 이 사람이 무척 설득력이 있어서 당신은 사용하는 방법도 모르면서 **전기톱**을 사게 된다. 당신은 여태껏 했던 방식대로 시동을 걸지도 않고 전기톱으로 나무를 마구 두들겨댄다. 곧 당신은 사용하는 방법도 모르면서 전기톱을 사게 된다. 곧 당신은 이 새로운 전기톱은 일시적인 유행일 뿐이라고 단정하고 다시 도끼를 쓰기 시작한다. 그때 누군가 나타나서 **전기톱의 시동 거는 법**을 가르쳐 준다.*
-닐포드, ‘함수형 사고’

- 이처럼 함수형 프로그래밍은 절차지향, 객체지향과 다른 새로운 ‘방식’
- 앞으로 쓸 일이 없을 것 같다 해도 **다른 방식으로 사고하는 법을 배우는 것**

## 객체지향과 비교

- 객체지향에선 비즈니스 관점에서 애플리케이션이 작동 → 메서드 위주로 관리
- 함수형은 데이터 기준으로 애플리케이션이 작동 → 바라보는 관점이 다름
- 예시(회원가입)
  - 객체지향(프로세스) : 유효성 검사 → 테이블에 추가 및 저장
  - 함수형(데이터) : 데이터를 받음 → 유효성 검사 및 변경 → 해시화한 변경 값
    ```java
    // 객체지향 프로그래밍
    Request request;
    request.validation();
    joinData = request.createMember();
    return makeResponse(joinData);
    
    // 함수형 프로그래밍, 데이터 중심으로 움직임
    // 데이터에 대해서 유효성 검사 후 이를 통해 나온 '데이터'를 통해 가입 처리
    // 그 다음에 나온 데이터를 가지고 반환 값을 만듦, 여기 보면 객체가 끼지 않음
    // 연께 과정이 모두 데이터 기반으로 동작함
    return validation(request)
                        .(x-> createMember)
                        .makeResponse(joinData);
    
    return validation(request)
                        .fliter(x-> x.Type == "a")
                        .(x-> createMember).otherWise(updateMember())
                        .makeResponse(joinData);
    ```
- 도메인에 따라 추구하는 패러다임이 있음
- 객체지향 → 이커머스처럼 고객 중심의 도메인에 적합
- 함수형 → IT플랫폼처럼 데이터 관점에 강한 도메인에 적합

## 특징

### 순수 함수

- 동일한 입력에는 항상 같은 값을 반환해야 하는 함수
- 함수의 실행이 프로그램의 실행에 영향을 미치면 안됨
- 함수 내부에서 인자 값을 변경하거나 프로그램 상태를 변경하는 side effect가 없어야 함

```java
int num = 1;

// 순수 함수 X - add라는 함수 안에서 전역 변수, num을 참조
int add(int i) {
	return i + num;
}

// 순수 함수 O - 프로그램 실행에 영향을 미치지 않고 입력 값에 대해서만 값의 변환이 있음
int add(int a, int b) {
	return a + b;
}
```

- 순수 함수는 프로그램의 변환 없이 입력 값에 대한 결과를 예상할 수 있어 **테스트 용이**

### 무상태, 불변성(Stateless, Immutability)
- 데이터는 변하지 않는 불변성을 유지해야 함
- 변경이 필요한 경우, 원본 데이터 구조를 변경하지 않고 그 데이터의 복사본(방어적 복사)을 만들어서 일부를 변경
- 변경한 복사본을 사용해 작업을 진행

```java
class Person {
	public String name = "jisu"
	public int age = 32;
}

// 객체 값을 바꾸기 위해서는 데이터의 복사본을 만들어, 그 복사본을 사용해 작업을 진행 및 반환
Person increaseAge(Person person) {
	Person copy = new Person();
	copy.age++;
	return copy;
}
```

### 선언형 함수(Expression)

> 명령형 프로그래밍은 무엇을 **어떻게(how)** 할 것인가에 주목, 선언형 프로그래밍은 **무엇(what)** 을 할 것인가에 주목
>

```java
// for문을 사용해 각 배열 요소에 값을 곱해주는 명령형 프로그래밍
void multiply(int[] nums, int multipleValue) {
	for(int i = 0; i < nums.length; ++i) {
		nums[i] *= multipleValue;
	}
}

// if, switch, for 등 명령문을 사용하지 않고 함수형 코드로 사용
int[] multiply(int[] nums, int multipleValue) {
	return Arrays.stream(nums).mapToInt(num -> num * multipleValue).toArray();
}
```

### 1급 객체와 고차 함수(First-class, Higher-order functions)
- 함수형 프로그래밍에서는 함수가 1급 객체가 됨

```java
// 1급 객체
final Predicate<String> isJisu = (str) -> str.equalsIgnoreCase("jisu");

System.out.println(isJisu("JISu");
```

- 변수나 데이터 구조 안에 담을 수 있음
- 파라미터로 전달 가능
- 반환 값으로 사용 가능
- 할당에 사용된 이름과 관계 없이 고유한 구별 가능
- 동적으로 속성 할당 가능
- 또한 고차 함수의 속성을 가져야 함
    ```jsx
    // 고차 함수, Java 지원 안해서 Javascript로 대체
    const addInform = (name) => (age) => age + name;
    const jongmin = addInform("지수");
    
    console.log(jongmin("32")); // 32지수
    ```
  - 함수를 인자로써 전달할 수 있어야 함
  - 함수의 반환 값으로 다른 함수를 사용할 수 있음

## 장단점

- 높은 수준의 추상화 제공
- 함수 단위의 코드 재사용 수월
- 불변성 지향 ⇒ 프로그래밍 동작 예측 쉬움

---

- 순수 함수 구현을 위해 가독성을 포기할 수 있음
- 반복이 for문이 아닌 재귀를 통해 이뤄짐 → 무한루프 가능성
- 사용은 쉬우나 조합은 쉽지 않음

# Reference

[함수형 프로그래밍이란?](https://jongminfire.dev/함수형-프로그래밍이란)