package jpabook.jpashop3.service;

import jpabook.jpashop3.domain.Member;
import jpabook.jpashop3.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 조회 메서드가 많으면 이걸 넣고 변경 메서드에 트랜잭션 어노테이션 넣어준다.
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();

    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원 전체 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    /**
     * return 값을 Member로 해도 됨. 하지만 우리는 쿼리와 커맨드를 구분하는게 좋음.
     * update는 커맨드임(변경성 메서드).
     * 그러나 Member를 리턴하게 되면 Member를 쿼리(조회 메서드)하게 됨.
     * 하지만 id값 정도는 리턴해도 좋음. 변경된 값을 찾을수도 있으니.
     */
    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);

    }
}
