package com.example.demo.infrastructure.persistence.adapter;

import com.example.demo.domain.model.Member;
import com.example.demo.domain.port.MemberRepository;
import com.example.demo.infrastructure.persistence.mapper.MemberEntityMapper;
import com.example.demo.infrastructure.persistence.repository.MemberJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * MemberRepository 포트의 구현체.
 * 회원 정보의 영속성을 담당한다.
 */
@Repository
public class MemberRepositoryAdapter implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;
    private final MemberEntityMapper memberEntityMapper;

    public MemberRepositoryAdapter(MemberJpaRepository memberJpaRepository, MemberEntityMapper memberEntityMapper) {
        this.memberJpaRepository = memberJpaRepository;
        this.memberEntityMapper = memberEntityMapper;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return memberJpaRepository.findById(id)
                .map(memberEntityMapper::toDomain);
    }
} 