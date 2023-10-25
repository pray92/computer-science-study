# 발표 대본

1. JpaMain
   - 연관관계 고려사항 나열(다중성, 단/양방향, 연관관계 주인)
   - 이후 세부사항 설명
2. MemberMtO
   - 다대일 단방향, 양방향 그림 보여줌
   - Member에 다대일 양방향 예제 보여줌(OneToMany 없애서 단방향 예제 설명)
   - 다대일 관련 설명
3. single.TeamOtM
   - 일대다 연관관계 설명
   - 일대다 단방향 그림 보여주고 설명
   - 일대다 단방향 예제 보여줌
4. JpaMain.otMSingleSave
   - 예제 실행
   - 단점 및 예시 설명
5. bidirection.MemberOtMBi
   - 일대다 양방향 불가능 설명
   - @JoinColumn을 통해 코드 상에선 설계 가능함을 표시
   - 일대다 양방향 그림 표시
6. manytoone.MemberMtO
   - 일대일 단방향, 양방향 그림 표시
   - 일대일 연관관계 설명
7. manytoone.Locker
   - 예제 표시
8. onetoone.Locker
   - 대상 테이블에 외래 키가 있는 일대일 단방향 설명
   - 양방향 및 정리 설용
9. manytomany.single.MemberMtM
   - 다대다 연관관계 설명 및 속성 설명
10. JpaMain.mtMSingleSave
    - 실행하여 MEMBER_PRODUCT 생성 표시
11. manytomany.bidirection.ProductBi
    - 양방향 설정 방법 설명
    - 실행하여 역방향 검색 가능문
12. README
    - 다대다 매핑 한계 설명
13. MemberProductId
    - 복합키 전략 예시 보여주면서 복합 기본 키 설명 -> 식별 관계
14. JpaMain
    - 복합키 예시 실행함
15. Order
    - 새로운 기본 키 사용 설명 -> 비식별 관계
16. JpaMain
    - 새로운 기본 키 사용 예시 실음