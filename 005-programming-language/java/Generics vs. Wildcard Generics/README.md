# Generics vs. Wildcard Generics

## 요약

---

## Generics

```java
public class Box<T> {
    
    T item;
    
    T getItem() { 
        return this.item;
    }
    
    void setItem(T item) {
        this.item = item;
    }
 }
```
- 다양한 타입의 객체들을 다루는 메서드 또는 클래스의 컴파일 시점에 타입 체크해주는 기능
- 객체의 안정성을 높이고 형변환의 번거로움이 줄어듦

### 용어

- `Box<T>`: 지네릭 클래스, **T의 Box** 또는 **T Box**라고 읽음
- `T`: 타입 변수 또는 타입 매개변수
- `Box`: 원시 타입`Raw Type`

### Generics 제한

```java
public class Box<T> {
    // static 사용 불가
    static item;                            // 에러
    static int compare(T t1, T t2) { ... }  // 에러
    
    // Generics 배열 생성 불가
    T[] itemArr;                            // OK, T 타입의 배열을 위한 참조 변수
    T[] toArray() {
        T[] tmpArr = new T[itemArr.length]; // 에러, 지네릭 배열 생성 불가
		...
    }
}
```
- 모든 객체에 대해 동일하게 동작해야 하는 `static` 멤버에 타입 변수를 사용할 수 없음
  - `T`는 인스턴수 변수로 간주되기 때문
  - 가령, `Box<Apple>.item`과 `Box<Grape>.item`이 다른 것이어서는 안됨
- 또한 Generics 타입 배열 생성도 허용되지 않음
  - 생성 불가의 원인은 `new` 연산자
  - `new` 연산자는 컴파일 시점에 타입 `T`가 뭔지 정확히 알아야 함 -> 위 코드의 컴파일 시점에선 `T`가 뭔지 알 수 없음
  - 같은 이유로 `instanceof` 연산자도 `T`를 피연산자로 사용 불가

### 타입 생략
```java
public class Fruit { ... }
public class Apple extends Fruit { ... }

Box<Apple> appleBox = new Box<>();
```
- JDK 7 버전 이후로 추정 가능 시 타입 생략이 가능

### 제한된 Generics 클래스
```java
public class FruitBox<T extends Fruit> { 
	ArrayList<T> list = new ArrayList<T>();
}

public interface Eatable { ... }
public class FruitBox<T extends Eatable> { 
	ArrayList<T> list = new ArrayList<T>();
}

public class FruitBox<T extends Eatable & Eatable> { 
	ArrayList<T> list = new ArrayList<T>();
}
```
- `extends`를 사용하면 특정 타입의 자손만 대입 가능
- 클래스가 아니라 인터페이스를 구현해야 한다는 제약이 필요해도 `extends` 사용
- 두개 이상의 구현체가 필요함을 제한시키고 싶으면 `&`로 연결

## Wildcard
```java
class Juicer {
	static Juice makeJuice(FruitBox<Fruit> box) {
		...
	}
}

FruitBox<Fruit> fruitBox = new FruitBox<Fruit>();
FruitBox<Apple> appleBox = new FruitBox<Apple>();
...

Juicer.makeJuice(fruitBox);  // OK, 특정 타입 지정
Juicer.makeJuice(appleBox);  // 에러, 특정 타입 지정
```
- static 메서드에는 타입 매개변수 T 사용 불가 ⇒ 아예 generics를 적용하지 않던가, 아니면 특정 타입을 지정해줘야 함
- 위처럼 타입이 강제되기 때문에, 여러 타입의 매개변수를 갖는 makeJuice()를 만들 수 밖에 없음

```java
// 컴파일 에러!
static Juice makeJuice(FruitBox<Fruit> box) {
	...
}
static Juice makeJuice(FruitBox<Apple> box) {
	...
}
```
- 문제는 Generics 타입이 다른 것만으론 오버로딩 성립 불가
- Generics 타입은 컴파일러가 컴파일할 땜나 사용하고 제거되기 때문에, 위 예시는 **메서드 중복 처리**

```java
static Juice makeJuice(FruitBox<? extends Fruit> box) {
	...
}
```
- 이러기 위해 고안된 것이 `Wildcard`
- 기호 `?`로 표현하며 어떠한 타입도 될 수 있음
  - `<? extends T>`: 와일드 카드 상한 제한, T와 하위 타입만 가능
  - `<? super T>`: 와일드 카드 하한 제한, T와 상위 타입만 가능
  - `<?>`: 제한 없음, 모든 타입 가능(=<? extends Object>)
  - `?`만으론 Object와 다를게 없으므로, `extends`, `super` 키워드로 하한/상한 제한

## Generic Method
```java
static <T> void sort(List<T> list, Comparator<? super T> c)
```
- 메서드 선언부에 Generics 타입이 선언된 메서드
- 지네릭 클래스에 정의된 타입 매개변수와 지네릭 메서드에 정의된 타입 매개변수는 완전 별개의 것

### 사용 예시
```java
static Juice makeJuice(FruitBox<? extends Fruit> box) { ... }

// 이렇게 바꾸면 됨
static <T extends Fruit> Juice makeJuice(FruitBox<T> box) { ... }

// 호출 방법
FruitBox<Fruit> fruitBox = new FruitBox<Fruit>();
FruitBox<Apple> appleBox = new FruitBox<Apple>();
...
Juicer.<Fruit>makeJuice(fruitBox);
Juicer.<Apple>makeJuice(appleBox);
```
### 주의할 점 
```java
<Fruit>makeJuice(fruitBox);         // 에러, 클래스 이름 생략 불가
this.<Fruit>makeJuice(fruitBox);
Juicer.<Fruit>makeJuice(fruitBox);
```
- 지네릭 메서드 호출 시 대입된 타입 생략이 불가한 경우에는 참조 변수나 클래스 이름 생략 불가
- `this.`나 `클래스 이름.`을 생략하고 메서드 이름만으로 호출이 가능하나, 대입된 타입 있을 시 반드시 써줘야 함

# Reference

자바의 정석 2권 - 지네릭스, 열거형, 에너테이션

[Java Generic, WildCard에 대해서](https://velog.io/@eversong/Java-Generic-WildCard에-대해서)