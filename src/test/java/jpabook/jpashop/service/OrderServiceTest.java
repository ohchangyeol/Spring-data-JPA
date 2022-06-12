package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotenoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired EntityManager em ;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;


    @Test
    public void 상품주문() throws Exception{
        // given 주어진 것
        Member member = createMember();
        Book book = createBook("김영한 JPA", 10000, 10);

        // when 실행
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);


        // then 결과
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals("상품 주문 상태는 ORDER : ", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정획해야 한다.",1,getOrder.getOrderItems().size());
        assertEquals("상품 가격은 가격 * 수량이다.",10000 * orderCount, getOrder.getTotalPrice());
        assertEquals("주문한 수량만큼 재고가 줄어야 한다.", 8, book.getStockQuantity() );

    }

    @Test
    public void 주문취소() throws Exception{
        // given 주어진 것
        Member member = createMember();
        Book item = createBook("이팩티브 JAVA", 32000, 5);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        // when 실행
        orderService.cancelOrder(orderId);

        // then 결과
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("주문 취소시 상태는 CAMCEL 이다.", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("주문 취소된 상뭄은 그만큼 재고가 증가해야 한다", 5, item.getStockQuantity());


    }
    
    @Test
    public void 상품주문_재고수량초과() throws Exception{
        // given 주어진 것
        Member member = createMember();
        Item item = createBook("김영한 JPA", 10000, 10);
        
        int orderCount = 11;

        // when 실행
        assertThrows(NotenoughStockException.class, ()->{
            orderService.order(member.getId(), item.getId(), orderCount);
        });

        
        // then 결과
    
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "1234-1"));
        em.persist(member);
        return member;
    }

}