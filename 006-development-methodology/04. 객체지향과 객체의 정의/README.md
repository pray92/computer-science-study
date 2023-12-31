# 객체지향과 객체의 정의

## 서적
- 사물은 눈으로 보여지는 것, 손으로 만져지는 것, 머릿속으로 상상되는 모든 것
- 객체지향의 출발은 '우리가 주변에서 사물을 인지하는 방식대로 프로그래밍할 수 있지 않겠는가'였음
- 컴퓨터에 맞춘 사고 방식을 버리고 현실세계를 인지하는 방식으로 프로그램을 만들기 때문에 객체지향은 직관적임
- 객체지향의 큰그림
  - 세상에 존재하는 모든 것은 '사물'
  - 각각의 사물은 고유함
  - 사물은 '속성'을 갖음
  - 사물은 '행동'을 함
- 그리고 사물을 하나하나 이해하기 보단 사물을 분류`class`해서 이해하는 것이 인간의 인지법
  - 직립보행을 하며 말하는 존재 => 사람으로 분류
  - 연미복, 짧은 다리, 날지 못하는 새 => 펭귄으로 분류
  - 밤하늘에 반짝이는 사물들 => 별로 분류
- 최지수, 이일민, Nolan 존재하는 사람이라는 분류`class`에 속함
- 그리고 사람이라는 분류 안에 객체`object`들은 나이, 몸무게, 키 등의 속성`property`와 먹다, 뛰다, 자다, 울다 등의 행동`method`를 가짐
- 객체지향 이전에는 속성과 메서드를 객체를 단위로 묶지 않고 속성 따로 메서드 따로 분리된 형태로 프로그래밍함
- 객체지향에서는 주변에서 실제 사물을 인지 및 사고하는 방식대로 객체 단위의 프로그래밍이 가능
  - 인간의 인지 및 사고 방식까지 프로그래밍에 접목하는 인간(개발자) 지향을 실천 => 따라서 객체지향은 직관적

## [Slack](https://f-lab-community.slack.com/archives/C02JPTLK8BG/p1684816475345029)
- 객체는 상태와 행동을 갖는 대상
- 여기서 나온 의문점
  1. 특정 형태를 가지고 아무런 행동도 없는 것(여기서는 예를 장식품으로 둚)은 객체라 할 수 있는가? => 행동이 없는 분류`class`를 객체라고 할 수 있는가?
  2. 이와 같은 대상은 무엇이라 불러야 하는가? => 행동이 없는 것은 무엇이라 불러야 하는가?
- 고찰 끝에 나온 결론
  1. '개체'라 불릴 수 있지 않을까?
     - 전체나 집단에 상대하여 하나하나의 낱개를 이르는 말
     - <생물> 하나의 독립된 생물체, 살아가는데 필요한 독립적인 기능을 가지고 있음
     - <철학> 단일하고 독립적인 통일적 존재, 철학 사상의 발전 과정에서 이 통일성은 물질적, 양적 측면 또는 정신적, 질적 측면 따위의 여러 관점에서 고찰되었다
  2. 프로그래밍 관점에서는 설명할 수 없다? -> 프로그램에 존재하는 모든 대상은 행동을 가지기 때문
- 김세훈 님의 의견
  - VO, Entity로 볼 수 있지 않을까?
  - 객체지향을 이해한 바로는, 프로그램은 현실을 그대로 반영하지 않음
  - 현실에서 장식품은 아무런 행동도 할 수 없지만, 프로그램에서 무언가 행동하게 할 수 있음 -> 빛을 낸다, 흔들린다
  - 예시로 든 장식품이 객체의 정의에 해당하는지 아닌지는 프로그램에서 장식품이 어떤 역할을 하냐에 따라 달라짐
    - 프로그램에서 장심품이 스스로 행동을 할 줄 안다면 장식품을 객체라할 수 있음
    - 장식품이 스스로할 행동이 없다면 VO, Entity로 볼 수 있음
- 홍브로 님의 의견
  - 인터페이스라 보여질 것 같음
  - 행동도 다른 행동에 의해 연결된 행동들이 있듯이, 집을 꾸민다는 행위에 의해 일어나는 선택적인 행동으로 장식품은 어딘가에 걸쳐진다는 제약이 생김
  - 사람이 객체라 할 때, 장식품이 주어지면(implements) 그걸 어디에 놓던가 걸던가 하는 행동(구현)이 일어남

## 생각 정리
- 객체지향은 '우리가 주변에서 사물을 인지하는 방식대로 프로그래밍'하는 '사상'에서 출발 by 엘런케이
- 그리고 그 사상을 바탕으로 C++, python 등 객체지향을 지원하는 프로그래밍 언어들이 나오면서 각자의 방향으로, 특정을 가지고 발전하는 형태의 패러다임이 나옴
- 여기서 사물은,
  1. 눈으로 보여지는 것, 손으로 만져지는 것, 머릿속으로 상상되는 모든 것
  2. 속성, 상태 그리고 행동을 가짐
  3. 고유함
- 객체지향에서 사물은 객체`Object`
- 속성은 객체를 구성하는 단순한 값`primitive type`을 의미
- 상태는 객체의 과거에 어떤 행동들을 했는지 설명 및 객체의 특징을 포현
  - 속성의 변화라고 생각함, 행동으로 인해 속성이 변하기 때문
- 행동은 객체가 할 수 있는 행위
- 이를 통해,
  - 상태와 행동을 가지는 대상은 객체
  - 상태를 가지나 행동을 가지지 않는 대상은 타입
  - ~~상태와 행동을 둘 다 가지지 않고 속성만 존재하는 대상은 VO~~
    - 노경태 님의 의견
      - VO는 값 객체, DTO와 달리 내부에서만 알고 있는 외부에 노출시키지 않는 객체 
        - ex. Password라는 VO를 정의, 그 안에 encode와 match 구현으로 속성에 대한 행동을 정의
    - 멘토 님의 의견
      - 상태를 가져오는 것도 행동 중 하나(Getter, 외부와의 협력 부분 담당), 따라서 VO도 객체
        - VO, DTO 등의 비즈니스 로직이 없다고 행동이 없다고 하는 것은 잘못된 생각 => 행동 != 로직
      - 다만 VO, DTO, Entity 같이 어떤 역할을 가지냐에 따라 서로 다른 역할과 목적을 가짐
      - 불변은 최초에 부여된 상태를 변화하지 않는 것, 상태가 없는 것은 아님
      - 행동이 없는 '것'은 표현이 불가능
        ```java
        // 행동이 없는 클래스, 인스턴스화할 수 있지만 무쓸모
        class BoardVO {
           private String title;
        }
    
        // 이걸 생각했으나, 상태를 가지게 되므로 이건 타입
        class BoardVO {
           public String title;
        }
        ```
      - 따라서 행동이 없는 객체는 표현이 불가능 -> 외부와의 협력, 책임 그리고 역할도 부여받지 못하기 때문
    - 이승근 님의 의견
      - VO는 여러가지 의미로 해석 가능, Context에 따라 다름
        1. DDD에서 VO는 불변 객체를 의미, 행동을 가질 수 있다고 보여짐
        2. VO를 DTO와 동일하게 본다면 행동을 가지면 안됨
      - 객체지향에 대한 결론
        - 객체의 분류는 중요한 것은 아님, 패러다임의 일환으로서 바라보고 큰 가치를 만들기 위한 '부품'으로만 봐야 함
        - 따라서 객체가 타입이냐, 객체냐, 추상이냐는 분류를 위한 구분으로서 용어가 사용되는게 아님, **자신의 의도를 설명하기 위한 방안으로 사용해야 함**

## 2차 정리
- 객체의 분류(타입/객체/추상)는 패러다임의 일환으로 바라보고 큰 가치를 만들기 위한 부품으로 봐야 한다
- 구분으로 용어가 사용된다기 보단, 개인의 의도를 설명하기 위한 방안으로 사용되어야 한다
- 왜냐하면 개인마다 생각하는 객체의 방향이 다르기 때문이다
- **따라서 내가 '객체, 타입, VO 등을 바라보는 개념의 정립'을 먼저하고, 이를 기준으로 잡아서 이야기하는 것이 좋다**

### 객체지향
- 객체지향 프로그래밍은 '우리가 주변에서 사물을 인지하는 방식대로 프로그래밍'하는 것이다
- 전통적인 절차지향 프로그래밍에서 사용되던 방식이 주는 높은 결합도와 낮은 응집도 문제 개선을 위해 도입한 것이다
- 사물을 분류`class`해서 분류된 객체`object`를 실제 사물로 인지 및 사고하는 인간적인 방식대로 객체 단위의 프로그래밍을 수행한다
- 객체지향은 인간(개발자) 지향을 실천한다, 따라서 객체지향은 직관적이다
---
- 객체지향 프로그래밍은 '우리가 주변에서 사물을 인지하는 방식대로 프로그래밍'하는 것이다
  - 여기서 사물은,
    1. 눈으로 보이는 것, 손으로 만져지는 것, 머릿속에서 상상되는 모든 것
    2. 속성, 상태 그리고 행동을 가짐
    3. 고유함
  - 라는 특징을 가진다
- 이는 객체지향 프로그래밍 패러다임을 정립한 Alan Kay에 의해 개발되었다
- 전통적인 절차지향적 프로그래밍에서 상요되는 방식이 주는 높은 결합도와 낮은 응집도 문제를 개선하기 위해 객체를 지향해서 프로그래밍하라는 개념을 도입한 것이다
- 일반적인 인간적 사고 방식은 사물을 하나하나 이해하기 보단, 사물을 분류`class`해서 이해한다
  - 직립보행하며 말하는 존재 = 사람
  - 연미복, 짧은 다리, 날지 못하는 새 = 펭귄
  - 밤하늘을 반짝이는 사물 = 별
- 최지수, 이일민, Nolan 존재는 사람이라는 분류`class`에 속한다
- 그리고 사람으로 분류된 객체`object`들은 나이, 키, 성별 같은 고유 속성과 걷다, 뛰다, 먹다 같은 행동`method`를 가진다
  - 또한 행동`method`으로 인해 역할, 책임 그리고 협력을 가진다
- 객체지향은 실제 사물을 인지 및 사고하는 방식대로 객체 단위의 프로그래밍이 가능하다
  - 인간의 인지 및 사고 방식까지 프로그래밍에 접목하는 인간(개발자) 지향을 실천하고 있다, 따라서 객체지향은 직관적이다

### 관련 메타포 및 객체 관련 고찰
- 객체는 상태, 행동을 가지는 대상이다
- 행동은 객체의 상태를 가져오는 것도 포함한다, 이를 통해 역할과 책임을 가지고 객체간 협력을 하기 때문이다
- 객체는 VO, DTO, Entity 처럼 서로 다른 역할과 목적을 가진다
- 객체는 타입의 인스턴스이다
---
- 사물: 객체`Object`
- 속성: 객체를 구성하는 단순한 값`primitive type`
- 상태: 속성의 변화 -> 행동으로 인해 속성이 변하기 때문
- 행동: 객체가 할 수 있는 행위`method`
- 타입: 객체의 분류`class`
---
- 객체는 상태, 행동을 가지는 대상이다 => 즉, 객체는 해당 타입의 인스턴스
- 행동을 가지지 않는 객체는 무의미하다
  - VO 같은 비즈니스 로직이 없는 객체도 상태를 가져오는 '행동'이 존재하기 때문이다
  - 이 행동을 통해 객체의 역할과 책임이 부여되고, 객체간 협력이 이뤄진다
  ```java
  // 행동이 없는 클래스, 인스턴스화할 수 있지만 무쓸모
  class BoardVO {
     private String title;
  }

  // 이걸 생각했으나, 상태를 가지게 되므로 이건 타입
  class BoardVO {
    public String title;
  }
  ```
  - 다만 객체는 VO, DTO, Entity처럼 객체지만 서로 다른 역할과 목적을 가질 수 있다
  - 비즈니스 로직이 없다고 하여 VO, DTO 등이 객체가 아닌 것은 아니다
    - 행동은 로직이 아니다, 협력도 포함한다(Getter, Setter)
    - 어떤 행동은 상태를 변화시키지 않는 경우도 있다
  - 불변은 최초에 부여된 상태를 변화시키지 않는 것이지, 상태가 없는 것은 아니다

# Reference

[9가지 프로그래밍 언어로 배우는 개념: 1편 - 타입 이론](https://tech.devsisters.com/posts/programming-languages-1-type-theory/)

[bliki: EvansClassification](https://martinfowler.com/bliki/EvansClassification.html)