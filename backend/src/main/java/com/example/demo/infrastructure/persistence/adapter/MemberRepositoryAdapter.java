package com.example.demo.infrastructure.persistence.adapter;

import com.example.demo.domain.model.Member;
import com.example.demo.domain.port.MemberRepository;
import com.example.demo.infrastructure.persistence.mapper.MemberMapper;
import com.example.demo.infrastructure.persistence.repository.MemberJpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * MemberRepository 포트의 구현체.
 * 회원 정보의 영속성을 담당한다.
 */
@Repository
public class MemberRepositoryAdapter implements MemberRepository {
    
    private final MemberJpaRepository memberJpaRepository;
    private final MemberMapper memberMapper;
    
    public MemberRepositoryAdapter(MemberJpaRepository memberJpaRepository, MemberMapper memberMapper) {
        this.memberJpaRepository = memberJpaRepository;
        this.memberMapper = memberMapper;
    }
    
    @Override
    public Optional<Member> findById(Long id) {
        return memberJpaRepository.findById(id)
                .map(memberMapper::toDomain);
    }
} 