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
- **모든 연관 관계는 지연로딩으로 설정**  
  즉시로당(EAGER)은 예측이 어렵고 어떤 SQL이 실행될지 추적하기 어려움(특히 JPQL을 실행할 때 N + 1문제 발생)  
  **실무에서 모든 연관관계는 지연로딩(LAZY)으로 설정.**  
  JPQL -> ```SELECT o FROM ORDER o;```   
  SQL  -> ```SELECT * FROM ORDER```로 변환되어 날아가기 때문에 그안에 `EAGER`가 있다면 단권커리가 날아가기 때문에 N + 1 개가 추가 되어 날아간다.  
  `@manyToOne`은 default가 `EAGER`이고 `@oneToMany`의 default값은 `LAZY`이다. 
- 컬랙샨은 필드에서 초기화  
  - null 문제에서 안전
  - 하이버네이트는 엔티티를 영속화 할 떄, 컬렉션을 감싸서 하이버네이트가 제공하는 내장 컬렉션으로 변경 
  ```java
  Member member = new Member();
  System.out.println(member.getOrders().getClass());
  em.persist(team);
  System.out.println(member.getOrders().getClass());
    
  //출력 결과
  class java.util.ArrayList
  class org.hibernate.collection.internal.PersistentBag
  ```
- `CascadeType`가 없는 경우 데이터를 저장할 때 Delivery와 Order가 각각 persist 해야 하는데 `CascadeType.ALL` 옵션이 있다면 Delivery를 가지고있는 Order 객체만 persist하면 Delivery도 같이 저장됨.    
  ```java
  // class Order
  @OneToOne(fetch = LAZY)
  @JoinColumn(name = "delivery_id")
  private Delivery delivery; //배송정보

  // --------------------------
  
  @OneToOne(cascade = CascadeType.ALL, fetch = LAZY)
  @JoinColumn(name = "delivery_id")
  private Delivery delivery; //배송정보
  
  // Hibernate 결과.
  // persist(Order);
  
  // ---------------------------
  
  // persist(Delivery);
  // persist(Order);
  ```
