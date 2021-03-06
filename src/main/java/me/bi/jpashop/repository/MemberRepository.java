package me.bi.jpashop.repository;

import lombok.RequiredArgsConstructor;
import me.bi.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByMemberName(String memberName) {
        return em.createQuery("select m from Member m where m.memberName = :name", Member.class)
                .setParameter("name", memberName)
                .getResultList();
    }
}
