# 다대일 단방향

![img.png](image/img.png)

# 다대일 양방향

![img_1.png](image/img_1.png)

# 일대다 단방향

![img_2.png](image/img_2.png)

- 팀 엔티티의 Team.members로 회원 테이블의 TEAM_ID 외래 키 관리
- 보통 자신이 매핑한 테이블의 외래 키를 관리, 이 매핑은 반대쪽 테이블에 있는 외래 키 관리

# 일대다 양방향

![img_3.png](image/img_3.png)

# 주 테이블에 외래 키가 있는 일대일 관계

## 단방향

![img_4.png](image/img_4.png)

## 양방향

![img_5.png](image/img_5.png)

# 대상 테이블에 외래 키가 있는 일대일 관계

## 단방향

![img_6.png](image/img_6.png)

## 양방향

![img_7.png](image/img_7.png)

# 다대다

![img_8.png](image/img_8.png)

# 다대다 매핑 한계

![img_9.png](image/img_9.png)

- 연결 테이블에 다른 컬럼 추가하면 @ManyToMany 사용 불가
- 왜냐하면 다른 엔티티에는 추가한 컬럼들 매핑이 불가하기 때명
- 이를 위한 전략으로, 연결 테이블을 엔티티로 승격시키는 방법이 있음
  - 복합 기본 키
  - 새로운 기본 키 사용