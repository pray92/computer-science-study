# 발표 대본

1. JpaMain
   - 방향, 다중성 간략 설명
   - 객체 & 테이블 간 연관관계 설명

2. single.MemberSingle
   - 단방향 연관관계 설명
   - 에너테이션 설명
3. JpaMain
   - 객체 그래프 탐색 방식(query), 실행으로 SELECT 쿼리 확인
   - 객체지향 쿼리 사용(jpql), 실행으로 쿼리 확인
   - update, delete 로직 메서드만 설명, 필요 시 빌드
4. interactive.MemberInter
   - @OneToMany 설명
5. JpaMain
   - biDirection 메서드 보여주기
     - 여기서 setTeam으로 초기화가 안되는 것을 보여주기
     - 이렇게 되면 연관관계 주인만 초기화해도 된다는 전제가 모두 무너짐을 알림
     - 일단 책에 있는 내용을 설명
   - 연관관계 주인 & 양방향 매핑 규칙 설명
6. MemberBi, TeamBi
   - 연관관계 주인 지정 방식 설정 방법 설명(mappedBy)
7. JpaMain
   - 로직상 설명(실제로 동작 안하는 것)
   - 순수 객체 기반 양방향 참조 설명
   - 최상단에 정리 설명