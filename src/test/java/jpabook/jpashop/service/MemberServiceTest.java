package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional // 데이터를 변경해야 함 -> 롤백 필요
class MemberServiceTest {

    // 테스트 요구사항
    // 1. 회원가입을 성공해야 한다
    // 2. 회원가입 할 때 같은 이름이 있으면 예외가 발생해야 한다

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em; // 인서트 쿼리 보고싶을 때

    // @Rollback(false) // DB에 테스트 내용이 저장됨 - 직접 확인하고 싶을 때 사용
    @Test
    void 회원가입() throws Exception {
        // given
        Member member = new Member();
        member.setName("kim");

        // when
        Long savedId = memberService.join(member);

        // then
        em.flush(); // 인서트 쿼리 보고싶을 때
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test
    void 중복_회원_예외() throws Exception {
        // given
        Member member1 = new Member();
        member1.setName("kim1");
        Member member2 = new Member();
        member2.setName("kim1");

        // when
        memberService.join(member1);
        assertThrows(IllegalStateException.class, () -> { // validateDuplicateMember가 실행되어 IllegalStateException이 발생하면 성공
            memberService.join(member2); // 똑같은 이름이므로 예외가 발생해야 함
        });

        // then
        // fail문이 실행되면 실패한 것
        // fail("예외가 발생해야 함");
    }
}