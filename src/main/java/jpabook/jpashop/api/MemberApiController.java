package jpabook.jpashop.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id,
                                               @RequestBody @Valid UpdateMemberRequest request){
        memberService.update(id, request.getName()); // 업데이트 후 트랜젝션 끝남
        Member findMember = memberService.findOnde(id); // 커맨드와 쿼리를 분리하는 게 좋음 -> 변경 후 리턴 받아오기보다 트랜젝션 끝난 후 다시 findOne으로 찾아줌
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @Data
    static class UpdateMemberRequest { // DTO
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }


    /**
     * 등록 V2: 요청 값으로 Member 엔티티 대신에 별도의 DTO를 받음
     * 장점:
     *  - 엔티티와 프로젠테이션 계층을 위한 로직을 분리할 수 있음
     *  - 엔티티와 API 스펙을 명확하게 분리할 수 있음
     *  - 엔티티가 변해도 API 스펙이 변하지 않음
     *  - 실무에서는 엔티티를 절대 외부에 노춣하면 안 됨!
     */
    @Data
    static class CreateMemberRequest { // DTO
        @NotEmpty
        private String name;

    }

    @Data
    static class CreateMemberResponse {
        private Long id;
        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}

