package jpabook.jpashop.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.Result;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /**
     * 조회 V2: 응답 값으로 엔티티가 아닌 별도의 DTO를 반환
     * 장점:
     * - 엔티티가 변해도 API 스펙이 변경되지 않음
     * - 추가로 Result 클래스로 컬렉션을 감싸서 향후 필요한 필드(카운트 등)을 추가할 수 있음
     */
    @GetMapping("/api/v2/members")
    public Result memberV2() {
        List<MemberDto> collect = memberService.findMembers().stream() // 엔티티 -> DTO 변환
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());
        return new Result(collect.size(), collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
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

