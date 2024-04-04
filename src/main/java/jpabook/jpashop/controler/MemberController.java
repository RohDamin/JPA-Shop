package jpabook.jpashop.controler;

import jakarta.validation.Valid;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // model: 컨트롤러로 데이터를 넘길 때 model에 실어서 넘김
    @GetMapping("/members/new")
    public String createForm (Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {

        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
        Member member = new Member();

        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }

    // 엔티티는 절대 api로 뢰부에 노출하면 안됨 -> 정보 노출, api 스펙이 변하는 문제
    // 여기에서는 서버 안에서만 기능이 돌기 때문에 괜찮음
    // 엔티티는 핵심 비즈니스 로직만 가지고 있어야 함, 화면을 위한 로직은 가지고 있으면 안됨
    @GetMapping("/members")
    public String list (Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members); // 실무에는 이렇게 엔티티 노출X
        return "members/memberList";
    }
}
