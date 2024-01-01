# equals()와 hashCode()

## 요약
- equals는 기본적으로 객체의 동일성을 판단한다. 프로그래밍상 속성을 비교해 동등성을 표현하고 싶다면 수정해줘야 한다
- hashCode는 런타임의 객체에 유일한 고윳값을 반환한다. Set이나 HashMap 같은 자료구조를 사용할 때 데이터를 저장되는 위치를 결정하기 때문에 필요 시 수정해줘야 한다
- equals 값이 참이면 hashCode 값도 동일해야 하나, hashCode 값이 같다고 equals 값이 참이 보장될 필요는 없다

---

## equals()
```java
public boolean equals(Object obj) {
    return (this == obj);
}
```
- 기본적으로 2개의 객체가 **동일**한지 검사하기 위해 사용
- `equals()`가 구현되 방법은 2개의 객체가 참조하는 것이 동일한지를 확인 => 동일성`Identity` 비교
- 하지만 프로그래밍 상 동일한 객체가 메모리 상에 여러 개 띄워져 있는 경우가 있음
  - 해당 객체는 서로 다른 메모리에 띄워져 있어 동일하다고 인식되지 않음 -> 프로그래밍상 같은 객체라고 인식할 필요가 있음
  - 이렇게 '같다'라고 하는 개념을 동등성`Equality`이라 함
  - 이러한 동등성을 위해 값으로 객체 비교하도록 `equals()`를 오버라이딩해줘야 함
    ```java
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof {this의 클래스 타입} type) {
            return this.variable == type.variable;
        }
        return false;   
    }
    ```

### 예시(String)
```java
String s1 = new String("Test");
String s2 = new String("Test");

System.out.println(s1 == s2);			// false
System.out.println(s1.equals(s2));		// true;

// equals, overridden in String Class 
public boolean equals(Object anObject) {
    if (this == anObject) {
        return true;
    }
    if (anObject instanceof String) {
        String anotherString = (String)anObject;
        int n = value.length;
        if (n == anotherString.value.length) {
            char v1[] = value;
            char v2[] = anotherString.value;
            int i = 0;
            while (n-- != 0) {
                if (v1[i] != v2[i])
                    return false;
                i++;
            }
            return true;
        }
    }
    
    return false;
}
```
- 동일한 값을 갖는 문자열 2개 생성 시 각각 서로 다른 메모리에 할당되므로 동일하지 않음
- 대신 같은 값을 지니므로 동등함 -> `equals()` 비교 시 true
- `equals()`를 오버라이딩해서 동등성 비교 처리했기 때문

## hashCode()
```java
public native int hashCode();
```
- 실행 중`Runtime`에 객체의 유일한 Integer 값을 반환
- 여기서 native 키워드는 메서드가 Java Native Interface`JNI` native code를 이용해 구현되었음을 의미
  - `native`는 메서드에만 적용 가능한 제어자, C/C++ 등 Java가 아닌 언어로 구현된 부분을 `JNI`을 통해 Java에서 이용할 때 사용
- `hashCode()`는 `HashMap` 같은 자료구조를 사용할 때 데이터가 저장되는 위치를 결정하기 위해 사용

### 예시
```java
public class HashSet<T>
        extends AbstractSet<E>
        implements Set<E>, Cloneable, java.io.Serializable 
{
    ...
    public HashSet() {
        map = new HashMap<>();
    }

    public boolean add(E e) {
        return map.put(e, PRESENT)==null;
    }
}
 
public class HashMap<K, V> {
    ...
    public V put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
    }

    final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict) {
        ...
            Node<K,V> e; K k;
            // 주목, hash 값을 먼저 비교
            if (p.hash == hash && ((k = p.key) == key || (key != null && key.equals(k))))
        ...
    }
}

```
```java
import java.util.HashSet;
import java.util.Set;
 
public class Main {
    static class Employee{
        private Integer id;
        private String firstname;
        private String lastName;
        private String department;

        //Setters and Getters
        
        @Override
        public boolean equals(Object obj) {
            if(o == null) {
                return false;
            }
            if (o == this) {
                return true;
            }
            if (getClass() != o.getClass()) {
                return false;
            }

            Employee e = (Employee) o;
            return (this.getId() == e.getId());
        }
    }
    
    public static void main(String[] args) {
        Employee e1 = new Employee();
        Employee e2 = new Employee();
 
        e1.setId(100);
        e2.setId(100);
 
        //Prints 'true' now!
        System.out.println(e1.equals(e2));
 
        Set<Employee> employees = new HashSet<Employee>();
        employees.add(e1);
        employees.add(e2);
         
        System.out.println(employees);  //Prints two objects
    }
}
```
- 모든 클래스가 상속하는 Object 클래스의 hashCode()는 해당 메모리 주소 값을 반환
- 따라서 `e1`과 `e2`는 다른 해시 값을 반환
- `Set` 자료구조는 서로 다른 객체를 저장하며, Java에선 `HashMap`으로 이를 구현
  - `HashMap`에서 값을 추가할 때 hashCode 결과 값을 먼저 비교
  - `e1`와 `e2`의 해시 값은 다르므로 서로 다르다고 판단하여 다른 위치에 저장됨
- 이러한 문제를 해결하기 위해 hashCode 메서드도 해당 클래스에 오버라이딩하여 수정해야 함
  ```java
  @Override
  public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + getId();
        return result;
  }
  ```

## equals()와 hashCode() 관계
- 동일한 객체는 동일한 메모리 주소를 가짐 => 동일한 객체는 동일한 해시 코드를 가져야 함
- 따라서 `equals()` 메서드를 오버라이딩한다면 `hashCode()`도 함께 오버라이딩해야 함
- 관계를 정의하면 아래와 같음
  - Java 프로그램을 실행하는 동안 equals()에 사용된 정보가 수정되지 않았다면 hashCode()는 항상 동일한 정수 값을 반환해야 함
  - 두 객체가 equals()에 의해 동등하다면, 두 객체의 hashCode() 값도 일치해야 함
  - 두 객체가 equals()에 의해 동등하지 않다면, 두 객체의 hashCode() 값은 일치하지 않아도 됨
  > **관계 요약**
  >- `obj1.equals(obj2) == true` 이면 `obj1.hashCode() == obj2.hashCode()`여야 한다
  >- `obj1.hashCode() == obj2.hashCode()`라고 `obj1.equals(obj2) == true`일 필요는 없다

## 라이브러리를 활용한 Override
```java
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Employee {
    private Integer id;
    private String firstname;
    private String lastName;
    private String department;
     
    //Setters and Getters
 
    @Override
    public int hashCode() {
        final int PRIME = 31;
        return new HashCodeBuilder(getId()%2==0?getId()+1:getId(), PRIME).toHashCode();
    }
 
    @Override
    public boolean equals(Object o) {
    if (o == null)
       return false;
        
    if (o == this)
       return true;
        
    if (o.getClass() != getClass())
       return false;
     
    Employee e = (Employee) o;
     
    return new EqualsBuilder().
              append(getId(), e.getId()).
              isEquals();
    }
}
```
- Apache Commons의 라이브러리를 사요하여 `HashCodeBuilder`와 `EqualsBuilder`를 사용해 수정 가능
- 외부 라이브러리를 이용해 해당 메서드들을 오버라이딩할 수 있음

# Reference

[[Java] equals와 hashCode 함수](https://mangkyu.tistory.com/101)