package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * xToOne 관계:
 * Order
 * Order -> Member // ManyToOne 관계
 * Order -> Delivery // OneToOne 관계
 *
 * 참고:
 * Order -> OrderItems 와는 OneToMany 관계라 나중에 다룸
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    /**
     * V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용X)
     * 단점: 지연로딩으로 쿼리 N번 호출
     * - 쿼리가 총 1+N+N번 실행됨
     */
    private final OrderRepository orderRepository;
    @GetMapping("/api/v2/simple-orders")
    public Result ordersV2() {
        //List<SimpleOrderDto> collect; //= orderRepository.findAllByString()
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return new Result(result);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); // LAZY 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getMember().getAddress(); // LAZY 초기화
        }
    }


}
