package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Delivery {
    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    // 양방향 연관관계가 있으면 한쪽은 JsonIgnore해야 함 -> 그렇지 않으면 무한루프 걸림
    @JsonIgnore
    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)// 1대1 연관관계 거울
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; // READY, COMP
}
