package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    // 양방향 연관관계가 있으면 한쪽은 JsonIgnore해야 함 -> 그렇지 않으면 무한루프 걸림
    @JsonIgnore
    @OneToMany(mappedBy = "member") // 연관관계의 주인X 거울O.
    private List<Order> orders = new ArrayList<>();
}

