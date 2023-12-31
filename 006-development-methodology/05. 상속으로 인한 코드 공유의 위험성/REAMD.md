# 상속으로 인한 코드 공유의 위험성

## 요약
- `취약한 기반 클래스 문제`: 상속은 결합도를 높이고, 상속이 초래하는 상위 클래스의 하위 클래스 사이의 강한 결합이 결과적으로 코드 수정을 어렵게 한다 -> DIP 위배
- `불필요한 인터페이스 상속 문제`: 상속 받은 상위 클래스의 메서드가 하위 클래스의 내부 구조 규칙을 꺨 수 있다 -> ISP 위배
- `메서드 오버라이딩 오작용`: 오버라이딩 시 상위 클래스가 자신이 사용하는 방법에 하위 클래스가 결합될 수 있다 -> LIP 위배
- `상위 클래스와 하위 클래스의 동시 수정 문제`: 결합도로 인해 상위/하위 클래스를 영원히 변경하지 않거나 둘 다 수정해야할 수 있다
- 이를 해결하기 위해 `추상화에 의존`, `차이를 메서드로 추출`, `중복 코드를 부모 클래스로 올리기`, 즉 **추상화**를 통해 해결한다

---

- 코드를 재사용하기 위해 새로운 클래스를 추가하는 가장 대표적인 방법은 상속
- 클래스 안에 정의된 인스턴스 변수와 메섣르르 자동으로 새로운 클래스에 추가하는 구현 기법

## 상속과 중복 코드
- 코드 재사용 동기는 중복된 코드를 제거
  - 중복 코드의 기준은 요구사항이 변경될 때 두 코드를 함께 수정해야하는 것을 기준으로 함
- 이를 위해 DRY`Don't Repeat Yourself` 원칙을 따라야 함
  > #### DRY
  > - 모든 지식은 시스템 내에서 단일하고, 애매하지 않고, 정말로 믿을 만한 표현 양식을 가져야 한다
- 상속을 하기 위해선 자식 클래스의 작성자가 부모 클래스 구현 방법에 대한 정확한 지식을 가져야 함
- 또한 결합도는 '하나의 모듈이 다른 모듈에 대해 얼마나 많은 지식을 가지고  있는지 나타내는 정도'로 정의함
- 즉, 상속은 **결합도를 높이게 되고 상속이 초래하는 부모 클래스의 자식 클래스 사이의 강한 결합이 결과적으로 코드 수정을 어렵게 만듦**
  - 추가 요구사항으로 인해 자식과 부모 클래스의 메서드를 바꿔야하는 경우 발생
  - 이렇게 되면 중복 코드 제거를 위해 상속을 사용했음에도 부모와 자식에 또 다른 중복 코드를 만들어야 하는 경우 발생
  - 이는 특히나 자식 클래스의 메서드 안에 `super` 참조를 통해 부모 클래스의 메서드를 직접 호출할 경우 매우 강하게 결합
    - 따라서 `super` 호출을 제거할 수 있는 방법을 찾아 결합도 제거 필요

## 취약한 기반 클래스 문제
- 상속은 부모와 자식 간에 겷바도를 높임, 이로 인해 자식은 부모의 불필요한 세부사항에 엮이게 됨
  - 부모의 작은 변경에도 자식에서 큰 문제 발생
  - 이처럼 부모의 변경에 의해 자식이 영향 받는 현상을 **취약한 기반 클래스 문제**라 함
- 상속은 확장 기능에 용이하나, 높은 결합도로 인해 부모를 점진적으로 개선하는 것을 어렵게 만듦
- 또한 자식이 부모의 구현 세부사항에 의존하도록 만들기 때문에 캡슐화 약화

## 불필요한 인터페이스 상속 문제
- 대표적인 예가 `Vector`를 상속한 `Stack`
- `Vector`의 불필요한 퍼블릭 인터페이스를 상속받아 `Stack`의 LIFO 원칙에 어긋나게 사용될 수 있음
- 결국 **상속 받은 부모 클래스의 메서드가 자식 클래스의 내부 구조에 대한 규칙을 깰 수 있음*

## 메서드 오버라이딩 오작용
- 하위 클래스가 상위 클래스의 메서드를 오버라이딩할 경우 상위 클래스가 자신의 메서드를 사용하는 방법에 하위 클래스가 결합될 수 있음 -> 코드 중복

## 부모 클래스와 자식 클래스의 동시 수정 문제
- 결합도로 인해 자식 클래스와 부모 클래스의 구현을 영원히 변경하지 않거나, 자식 클래스와 부모 클래스를 동시에 변경하거나 둘 중 하나를 선택할 수 밖에 없음

## 해결 방안
### 추상화에 의존
- 부모와 자식 모두 추상화에서 의존하도록 수정

### 차이를 메서드로 추출
- 중복 코드 안에서 차이점을 별도의 메서드로 추출

### 중복 코드를 부모 클래스로 올려라
- 목표는 모든 클래스들을 추상화에 의존하도록 만드는 것이기 때문에 해당 클래스는 추상 클래스로 만드는 것이 적절
