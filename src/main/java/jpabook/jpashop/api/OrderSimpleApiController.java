package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
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
     * V3. 엔티티를 조회해서 DTO로 변환(fetch join 사용O)
     * - 엔티티를 페치 조인(fetch join)을 사용해서 쿼리 1번에 조회
     */
    private final OrderRepository orderRepository;
    @GetMapping("/api/v3/simple-orders")
    public Result ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return new Result(result);
    }

    /**
     * V4. JPA에서 DTO로 바로 조회
     * - 일반적인 SQL을 사용할 때처럼 원하는 값을 선택해서 조회
     * - new 명령어를 사용해서 JPQL의 결과를 DTO로 즉시 변환
     * - API 스펙에 맞춘 코드가 리포지토리에 들어가게 된다는 단점
     * - 그렇게 성능에 큰 영향을 주지 않음..
     */
    private final OrderSimpleQueryRepository orderSimpleQueryRepository; // 의존관계 주입
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderSimpleQueryRepository.findOrderDtos();
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
