package com.jpabook.jpashop.repository;

import com.jpabook.jpashop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;

public interface MemberRepository extends JpaRepository<Member, Long> {
    public List<Member> findByName(String name);
}
