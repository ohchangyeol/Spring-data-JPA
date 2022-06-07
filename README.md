# Spring Data JPA

김영한 개발자님의 [인프런 강의](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-JPA-%ED%99%9C%EC%9A%A9-1)를 보고 학습한 내용입니다.

#### 현재 Repository의 내용은
- JPA 활용1 (JPA_01)
- JPA 활용2 (JPA_02)
- 스프링데이터 JPA (SD_JPA)
- QueryDSL (DSL)

순으로 강의를 보며 따라할 계획이며 branch로 버전 관리를 할 예정입니다.

## JPA 활용1 
*Branch Name :: JPA_01*

- JPA의 동작하는 모든 Entity는 transaction안에서 이루어져야만 한다.
- Test code에서 이루어지는 데이터는 rollback
- rollback(value = false) 함으로서 rollback을 취소한다.
- 쿼리 파라미터 로그 남기는 설정  
  application.yml -> ```org.hibernate.type: trace```  
  build.gradle -> ```com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.5.6```  
  **다만 배포시엔 성능 이슈가 될수 있기 때문에 개발단계에서만 쓴는 것을 권장**