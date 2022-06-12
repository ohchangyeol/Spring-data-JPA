# Spring Data JPA

김영한 개발자님의 [인프런 강의](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-JPA-%ED%99%9C%EC%9A%A9-1)를 보고 학습한 내용입니다.

## 📃실전! 스프링 부트와 JPA 활용1 - 웹 애플리케이션 개발
*Branch Name :: JPA_01*

### 알아두면 좋아요👍
- `main`, `test` 디렉토리에서는 각자의 `resources`를 읽음


### 👀Transaction?
- `@Transactional (readOnly = true)`  
  - 성능 최적화  
  - 리소스 X 읽기만
- JPA의 동작하는 모든 Entity는 transaction안에서 이루어져야만 한다.
- Test code에서 이루어지는 데이터는 rollback
- rollback(value = false) 함으로서 rollback을 취소한다.

### 👀쿼리 파라미터 로그 남기는 설정  
  application.yml -> ```org.hibernate.type: trace```  
  build.gradle -> ```com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.5.6```  
  **다만 배포시엔 성능 이슈가 될수 있기 때문에 개발단계에서만 쓴는 것을 권장**

### 👀즉시로딩과 지연로딩의 차이
  >**모든 연관 관계는 지연로딩으로 설정**  

  즉시로당(EAGER)은 예측이 어렵고 어떤 SQL이 실행될지 추적하기 어려움(특히 JPQL을 실행할 때 N + 1문제 발생)  
  **실무에서 모든 연관관계는 지연로딩(LAZY)으로 설정.**  
  JPQL -> ```SELECT o FROM ORDER o;```   
  SQL  -> ```SELECT * FROM ORDER```로 변환되어 날아가기 때문에 그안에 `EAGER`가 있다면 단권커리가 날아가기 때문에 N + 1 개가 추가 되어 날아간다.  
  `@manyToOne`은 default가 `EAGER`이고 `@oneToMany`의 default값은 `LAZY`이다. 
### 👀컬랙션은 필드에서 초기화  
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
  persist(Order);
  
  ---------------------------
  
  persist(Delivery);
  persist(Order);
  ```
### 👀Dependency Injection
- `@Autowired`
  - 필드 주입 -> 주입하기가 까다로움
  - setter 주입 -> 테스트 시 mock으로도 주입 가능, 누가 변경 가능
  - 생성자 주입 -> 스프링이 컴포넌트를 읽을때 생성자에 컴포넌트가 있다면 주입. 필드는 변경할 수 없게 final 지정. 
- lombok
  - `@AllArgsConstructor`라는 어노테이샨이 지정된 클래스의 field를 읽어 접근 키워드와 상관없이 constructor를 생성
  - `@RequiredArgsConstructor`같은 경우 field에 final 이라는 키워드가 붙은 것만 constructor를 만들어 주입한다. 

### abstract class Item ❔
#### 부모객체인데 왜 인퍼테이스가 아니고 추상클래스일까?
인터페이스에는 정의만 해주는것으로 구현된 메서드가 들어가지않는다.   
보통 개발을 할때 서비스에서 Entity를 가져와 수량을 바꾸고 다시 set 하는 경우가 대다수다.근데 객체지향으로 생각을 한다면 데이터가 가지고있는 쪽에 비지니스 로직을 가지고있는 것이 좋다.(응집력)  
> 그래서 서비스로 분리하는 것보다 핵심 비지니스 로직을 Entity 안에 넣어 응집력을 높히고 관리가 수월하게 된다.  
> 이처럼 엔티티가 비지니스 로직을 가지고 객체지향의 특성을 적극 활용하는 것을 [도메인 모델 패턴](http://martinfowler.com/eaaCatalog/domainModel.html)이라고 하고,  
> 반대로 서비스 계층에 로직이 있는 경우 [트랜잭션 스크립트 패턴](http://martinfowler.com/eaaCatalog/transactionScript.html)이라고 한다.

**entity에는 변화가 자주 일어나느 값이라면 setter보다 핵심 비지니스로직을 사용하는 것을 권장하고 좋다.**

### Lambda와 가변변수


- Lambda
  ```java
  public int getTotalPrice () {
      int totalPrice = 0;
      for (OrderItem orderItem : orderItems) {
          totalPrice += orderItem.getTotalPrice();
      }
      return totalPrice;
  }
  ```
  ```java
  public int getTotalPrice () {
      return orderItems.stream()
          .mapToInt(OrderItem::getTotalPrice)
          .sum();
  }
  ```
  
### 준영속 엔티티
영속성 컨텍스트가 더는 관리하지 않는 엔티티를 말한다.  
`itemService.saveItem(book)` 에서 수정을 시도하는 `Book` 객체다. `Book` 객체는 이미 DB
에 한번 저장되어서 식별자가 존재한다. 이렇게 임의로 만들어낸 엔티티도 기존 식별자를 가지고 있으면 준
영속 엔티티로 볼 수 있다.
```java
@PostMapping(value = "/items/{itemId}/edit")
public String updateItem(@ModelAttribute("form") BookForm form) {
    Book book = new Book();
    book.setId(form.getId());
    book.setName(form.getName());
    book.setPrice(form.getPrice());
    book.setStockQuantity(form.getStockQuantity());
    book.setAuthor(form.getAuthor());
    book.setIsbn(form.getIsbn());
    itemService.saveItem(book);
    return "redirect:/items";
}
```
#### 준영속 엔티티를 수정하는 2가지 방법
- 변경 감지 기능 사용
  ```java
  @Transactional
  void update(Item itemParam) { //itemParam: 파리미터로 넘어온 준영속 상태의 엔티티
    Item findItem = em.find(Item.class, itemParam.getId()); //같은 엔티티를 조회한
    다.
    findItem.setPrice(itemParam.getPrice()); //데이터를 수정한다.
    findItem.setStockQuantity(itemParam.getStockQuantity());
  } 
  ```
  > 영속성 컨텍스트에서 엔티티를 다시 조회한 후에 데이터를 수정하는 방법
  트랜잭션 안에서 엔티티를 다시 조회, 변경할 값 선택 트랜잭션 커밋 시점에 변경 감지(Dirty Checking)
  이 동작해서 데이터베이스에 UPDATE SQL 실행

- 병합 사용
  ```java
  @Transactional
  void update(Item itemParam) { //itemParam: 파리미터로 넘어온 준영속 상태의 엔티티
   Item mergeItem = em.merge(item);
  }
  ```
  > 조회한 영속 엔티티( mergeMember )에 member 엔티티의 값을 채워 넣는다. (member 엔티티의 모든 값
  을 mergeMember에 밀어 넣는다. 이때 mergeMember의 “회원1”이라는 이름이 “회원명변경”으로 바
  뀐다.)

  **주의: 변경 감지 기능을 사용하면 원하는 속성만 선택해서 변경할 수 있지만, 병합을 사용하면 모든 속성이
  변경된다. 병합시 값이 없으면 null 로 업데이트 할 위험도 있다. (병합은 모든 필드를 교체한다.)**


- 권장 코드
  ```java
  public class ItemService {
    @Transactional
    public void updateItem(Long id, String name, int price) {
        Item item = itemRepository.findOne(id);
        item.setName(name);
        item.setPrice(price);
    }
  }
  ```
  ```java
  public class ItemController {
    @PostMapping(value = "/items/{itemId}/edit")
    public String updateItem(@ModelAttribute("form") BookForm form) {
        itemService.updateItem(form.getId(), form.getName(), form.getPrice());
        return "redirect:/items";
    }
  }
  ```