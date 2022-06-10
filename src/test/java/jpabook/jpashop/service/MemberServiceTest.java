package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class MemberServiceTest {

   @Autowired MemberService memberService;
   @Autowired MemberRepository memberRepository;
   @Autowired EntityManager em;

   @Test
   //@Rollback(false)
   public void 회원가입() throws Exception{
      //given
      Member member = new Member();
      member.setUserName("kim");

      //when
      Long saveId = memberService.join(member);

      //then
      //em.flush();
      assertEquals(member, memberRepository.findOne(saveId));
   }


   @Test
   public void 중복_회원_예외() throws Exception{
       //given
      Member member1 = new Member();
      member1.setUserName("kim");

      Member member2 = new Member();
      member2.setUserName("kim");
       //when
      memberService.join(member1);
      assertThrows(IllegalStateException.class,()->{
         memberService.join(member2);
      });
      //try {
      //   // Exception 발생 시 catch.
      //   memberService.join(member2);
      //}catch (IllegalStateException e){
      //   return;
      //}
       //then

      //fail("예외가 발생해야 함.");
   }
}