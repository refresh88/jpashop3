package jpabook.jpashop3;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    @PersistenceContext // 팩토리 머시꼥이 귀찮은거 얘가 다해줌.
    private EntityManager em;

//         "커맨드랑 쿼리를 분리해라" : 저장을 하고나면 사이드 이펙트(내부에서 변경)를 일으키는 커맨드성이기 떄문에
//         리턴값을 안만듬. 그러나 id 정도 리턴하면 조회할때 쓸수 있으니까 요래 설계함.
//         사이드 이펙트를 유발하는 커맨드는 리턴값을 안만드는게 기본 개념.
//         대부분의 문제는 데이터를 변경하는 곳에서 발생함.
//         반대로 아무런 변화가 없는 메서드는 리턴값이 있음. 조회가 그렇듯이.
    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    public Member findMember(Long id) {
        return em.find(Member.class, id);
    }

}
