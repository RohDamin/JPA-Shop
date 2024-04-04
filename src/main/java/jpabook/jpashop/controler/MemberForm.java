package jpabook.jpashop.controler;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberForm {

    @NotEmpty(message = "회원 이름은 필수입니다") // 이름으로 null받으면안됨
    private String name;

    private String city;
    private String street;
    private String zipcode;

}