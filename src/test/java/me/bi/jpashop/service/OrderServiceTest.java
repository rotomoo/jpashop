package me.bi.jpashop.service;

import me.bi.jpashop.domain.Address;
import me.bi.jpashop.domain.Member;
import me.bi.jpashop.domain.Order;
import me.bi.jpashop.domain.OrderStatus;
import me.bi.jpashop.domain.item.Book;
import me.bi.jpashop.exception.NotEnoughStockException;
import me.bi.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;
    
    @Test
    public void 주문() throws Exception {
        //given
        Member member = createMember("회원1");
        Book book = createBook("시골 JPA", 10000, 10);
        int count = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), count);

        //then
        Order findOrder = orderRepository.findOne(orderId);
        
        // 주문 상태
        assertEquals(findOrder.getStatus(), OrderStatus.ORDER);

        // 상품 종류수
        assertEquals(findOrder.getOrderItems().size(), 1);
        
        // 주문 가격
        assertEquals(findOrder.getTotalPrice(), 10000 * 2);
        
        // 주문 수량
        assertEquals(8, book.getStockQuantity());
    }

    @Test
    public void 주문_재고수량초과() throws Exception {
        //given
        Member member = createMember("회원1");
        Book book = createBook("시골 JPA", 10000, 10);

        int count = 11;

        //when
        try {
            Long orderId = orderService.order(member.getId(), book.getId(), count);
        } catch (NotEnoughStockException e) {
            return;
        }

        //then
        fail("재고 수량 부족 예외가 발생해야합니다.");

    }

    @Test
    public void 주문_취소() throws Exception {
        //given
        Member member = createMember("회원2");
        Book book = createBook("시골JPA", 10000, 10);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order findOrder = orderRepository.findOne(orderId);
        assertEquals(findOrder.getStatus(), OrderStatus.CANCEL);
        assertEquals(book.getStockQuantity(), 10);

    }

    private Member createMember(String memberName) {
        Member member = new Member();
        member.setMemberName(memberName);
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }

    private Book createBook(String itemName, int price, int stockQuantity) {
        Book book = new Book();
        book.setItemName(itemName);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }
}