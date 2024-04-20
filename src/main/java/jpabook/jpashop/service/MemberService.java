package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 트랜젝션: readOnly=true는 데이터의 변경이 없는 읽기 전용 메서드에 사용 - 성능 향상
@RequiredArgsConstructor // final이 있는 필드만 가지고 생성자를 만들어 줌 -> 이 방법이 좋음
public class MemberService {
    private final MemberRepository memberRepository;

    // 회원 가입
    @Transactional // 쓰기 메서드인 경우 readOnly=true 적용이 되지 않도록 트랜젝션만 써줌
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 전체 회원 조회

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOnde(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}
