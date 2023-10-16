# 발표 대본

1. JpaMain
   - 대표 에너테이션 설명
2. Member
   - @Entiy, @Table 설명
   - @Enumerated, @Temporal, @Lob 설명
3. persistence.xml
   - hbm2ddl.auto, show_sql 속성 설명
   - class 속성 짧게 설명 후 빌드를 통해 DDL 결과 확인
   - JDK 17부터 reflection 통한 함수 접근 불가 이슈 있 11로 변경,
   - hgm2ddl.auto
   - 이름 매핑 전략 -> 간략하게 설명, 상세 내용은 이후에
4. Member
   - username에 DDL 제약조건 추가된거 보여주기
   - 빌드해서 uniqueConstraints 설정시 Alter 확인
   - `앞서 언급된 제약 조건은 DDL 생성만 사용되고, JPA 실행 로직엔 영향 주지 않음`
   - `이 기능 사용 시 애플리케이션 개발자가 엔티티만 보고도 다양한 제약 조건 파악 가능`
5. peristence.xml
   - 키 생성 전략 설명
6. Member
   - @Id 적용 가능 Java 타입
   - 식별자 선택 전략 설명
   - 자연 키, 대리 키 설명
   - 직접 할당 방법 및 관련 예외처리 설명
7. Member
   - IDENTITY 상세 설명
8. Board
   - SEQUENCE 상세 설명
   - persistence.xml에 관련 설정 설명
9. BoardTable
   - TABLE 상세 설명
10. BoardAuto
   - AUTO 상세 설명
11. FieldAndColumnMapping
   - 각 에너테이션 설명
   - @Access는 마지막에 설명

            - 