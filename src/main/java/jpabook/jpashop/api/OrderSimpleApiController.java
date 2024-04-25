package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
     * 간단한 주문 조회 V1. 엔티티 직접 노출
     * 문제 1. 무한루프
     * 해결: JsonIgnore 추가해서 해결
     *
     * 문제 2. Internal Server Error
     * - fetch = LAZY(지연로딩)로 되어있기 때문에 DB에서 바로 데이터를 가져오는 게 아니라
     * - ProxyMember 객체를 생성해서 넣어둠
     * - 이 객체가 바로 에러 메세지에 나오는 bytebuddy
     * - Json 라이브러리가 루프를 돌릴 때,
     * - 순수한 자바 객체인 멤버가 아니라 bytebuddy를 만났기 때문에 오류가 난 것
     * 해결: build.gradle에 hibernate5JakartaModule 라이브러리 추가
     *
     *  결론:
     *  - 그냥 엔티티를 외부에 노출하지 않는 게 좋음
     *  - hibernate5JakartaModule을 사용해서 해결했지만, DTO로 변환해서 반환하는 게 더 좋은 방법
     *
     *  주의!
     *  지연 로딩(LAZY)을 피하기 위해 즉시 로딩(EAGER)으로 설정하면 안 됨
     *  즉시 로딩 때문에 연관관계가 필요 없는 경우에도 데이터를 항상 조회해서 성능 문제가 발생할 수 있음
     *  또한 튜닝이 매우 어려워짐
     *  항상 지연 로딩을 기본으로하고, 성능 최저화가 필요한 경우에는 fetch join을 사용할 것
     */
    private final OrderRepository orderRepository;
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        // 양방향 연관관계가 있으면 한쪽은 JsonIgnore해야 함 -> 그렇지 않으면 무한루프 걸림
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); //LAZY 강체 초기화
            order.getDelivery().getAddress(); //LAZY 강체 초기화
        }
        return all;
    }
}
