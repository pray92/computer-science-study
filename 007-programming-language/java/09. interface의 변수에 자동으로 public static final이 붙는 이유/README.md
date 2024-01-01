# interface의 변수에 자동으로 public static final이 붙는 이유

## 요약


---

## public인 이유
- 인터페이스의 추상 메서드처럼 모든 클래스에 접근해야 함

## static인 이유
- `interface`는 스스로 인스턴스화 불가능
- 따라서 변수의 값은 인스턴스가 없어도 존재할 수 있는 `static`으로 선언

## final인 이유
- 변수 값이 프로그램 코드로 인해 재할당받으면 위험 => 공유의 위험성
- 2개 이상의 클래스가 구현`implements`되었을 때 하나라도 상태를 변경하게 되면 신뢰할 수 없어, 디버깅하기 어려운 코드가 됨

# Reference
[Why are interface variables static and final by default?](https://stackoverflow.com/questions/2430756/why-are-interface-variables-static-and-final-by-default)