# Serializable

## 요약

- 직렬화는 데이터를 한 데 모아 일련의 바이트 형태로 만들어 놓은 것을 말하며, 컴퓨터 간 데이터 통신을 위해 존재하는 것이다
- Java에서는 Serializable 인터페이스를 선언 및 구현(serialVersionUID)해서 객체를 직렬화 및 역직렬화한다
- 여기서 serialVersionUID는 컴파일러에 따라 계산하기 때문에 올바른 직렬화 및 역직렬화를 위해 클래스에 선언해야 한다

---

## Serialization(직렬화)

- 프로그램에서 사용되는 데이터들은 연속적으로 위치해 있지 않고 내부적으로 포인터에 의해 참조되고 있음
- 이는 프로그램이 실행 중인 컴퓨터에서만 인식할 수 있는 형태
- 다른 컴퓨터와 통신하며 데이터를 알맞게 전달할기 위해선 흩뿌려져 있는 데이터를 한 데 모아 포인터가 존재하지 않는 일련의 바이트 형태로 만들어 보내는 것

### Java에서 직렬화
- 데이터를 파일에 쓰거나 네트워크를 타고 다른 곳에 전송 할 때, 데이터를 바이트 단위로 분해해서 순차적으로 보내는 것
- 기본 자료형은 정해진 바이트 변수라, 바이트 단위로 분해하고 전송하고 조립하는데 문제 없음
- 그러나 객체 크기는 가변적이라 객체를 구성하는 자료형들의 종류와 수에 따라 객체 크기는 다양하게 바뀜
- `Serializable` 인터페이스는 이를 위해 존재
  - java.io: `ObjectInputStream`, `ObjectOutputStream`을 통해 직렬화 및 역직렬화 가능
  - java.nio: 따로 제공하진 않으나, byte 배열을 할당해 커널 버퍼에 해당 byte 배열을 넣어서 직렬화하고, 이후 꺼내서 역직렬화

### serialVersionUID

- Java에서 직렬화를 구현한다는 것은 `serialVersionUID`를 선언하는 것 -> `Serializable`은 인터페이스이며 따로 구현 메서드가 없음
  ```java
  import java.io.Serializable;
  
  public class Person implements Serializable {
  
      private static final long serialVersionUID = -5129628467395047900L;
      
      String name;
      int age;
      double height;
      String hobby;
  }
  ```
- `serialVersionUID`은 **직렬화 버전의 고유 값**
- 직렬화 및 역직렬화 시 이 값으로 해당 클래스의 특정 버전에 맞는지 판단하는 것
  - `serialVersionUID`을 지정하지 않으면 컴파일러가 계산한 값을 부여 -> 컴파일러에 따라 할당되는 값이 다를 수 있음
  - 컴파일러는 `Serializable` 또는 Outer Class를 참고해서 만들기 때문에 만약 클래스에 변경이 있으면 `serialVersionUID`도 변경이 있을 수 있음
  - 이 때문에 역직렬화 시 예상 못한 문제가 발생하기 때문에 선언이 필요함

### 직렬화 가능 객체 조건
1. 기본형 타입
2. `Serializable` 인터페이스를 구현한 객체
    >💡Serializable 인터페이스가 구현되어 있지 않더라도, 객체를 byte 배열에 담아 직렬화 및 역직렬화가 가능함
    > 
    > ```java
    > import java.nio.ByteBuffer;
    >
    > public class SerializationExample {
    >     public static void main(String[] args) {
    >         // 객체를 직렬화
    >         MyObject obj = new MyObject(42, "Hello, World!");
    >         ByteBuffer buffer = ByteBuffer.allocate(1024);
    >         buffer.putInt(obj.getIntField());
    >         buffer.put(obj.getStringField().getBytes());
    >         buffer.flip();
    >     
    >         // 객체를 역직렬화
    >         int intValue = buffer.getInt();
    >         byte[] stringBytes = new byte[buffer.remaining()];
    >         buffer.get(stringBytes);
    >         String stringValue = new String(stringBytes);
    >     
    >         MyObject deserializedObj = new MyObject(intValue, stringValue);
    >         System.out.println(deserializedObj);
    >     }
    > }
    > 
    > class MyObject {
    >     private int intField;
    >     private String stringField;
    > 
    >     public MyObject(int intField, String stringField) {
    >         this.intField = intField;
    >         this.stringField = stringField;
    >     }
    > 
    >     public int getIntField() {
    >         return intField;
    >     }
    > 
    >     public String getStringField() {
    >         return stringField;
    >     }
    > 
    >     @Override
    >     public String toString() {
    >         return "MyObject{intField=" + intField + ", stringField='" + stringField + "'}";
    >     }
    > }
    > ```
3. has-a 관계에 있는 멤버 객체 중 `Serializable` 인터페이스가 구현된 객체
4. `transient`가 선언되지 않은 멤버 객체
   - `Serializable` 구현 안된 객체에 사용
   - `transient`가 선언된 객체를 역직렬화해서 구해왔을 땐 참조형 변수는 null 반환

## transient

- 멤버 변수 앞에 선언
- 객체의 데이터 중 일부는 여러 이유(보안, 비밀번호)로 전송하고 싶지 않을 수 있음
- 이 변수는 직렬화에서 제외되어야 하며, 이를 위해 `transient` 선언
  > 💡마찬가지로 byte 배열 기반 직렬화에선 통하지 않음, `Serializable` 관련 모듈에 통함

# Reference

[직렬화(Serialization) 와 transient 키워드](https://www.nakjunizm.com/2017/12/10/Serialization_And_Transient/)

[[java] serialVersionUID의 용도](https://m.blog.naver.com/writer0713/220922099055)

[자바 직렬화(serialize)란? serialVersionUID 란?](https://velog.io/@hellonewtry/%EC%9E%90%EB%B0%94-%EC%A7%81%EB%A0%AC%ED%99%94%EB%9E%80-serialVersionUID-%EB%9E%80)

[[java] serialVersionUID의 용도](https://m.blog.naver.com/writer0713/220922099055)
