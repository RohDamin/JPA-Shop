package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    /**
     * 등록 V1: 요청 값으로 Member 엔티티를 직접 받음
     * 문제점:
     * - 엔티티에 프로젠테이션 계층을 위한 로직이 추가됨
     *   - 엔티티에 API 검증을 위한 로직이 들어감 (@NotEmpty 등)
     *   - 어떤 API에서는 NotEmpty가 필요하지만, 어떤 API에서는 그렇지 않을 수도 있음 -> 모든 요구사항을 엔티티에 담기 힘듦
     *   - 엔티티가 변경되면 API 스펙이 변함
     * 해결방법:
     * - API 요청 스펙에 맞춰 별도의 DTO를 파라미터로 받아야 함
     */
    @NotEmpty // 이름 필수값 -> 비어있으면 안됨
    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member") // 연관관계의 주인X 거울O.
    private List<Order> orders = new ArrayList<>();
}

