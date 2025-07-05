package com.example.demo.infrastructure.persistence.adapter;

import com.example.demo.domain.model.Member;
import com.example.demo.domain.model.PopupDetail;
import com.example.demo.domain.model.Waiting;
import com.example.demo.domain.model.WaitingQuery;
import com.example.demo.domain.port.WaitingRepository;
import com.example.demo.infrastructure.persistence.entity.WaitingEntity;
import com.example.demo.infrastructure.persistence.mapper.WaitingMapper;
import com.example.demo.infrastructure.persistence.repository.WaitingJpaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * WaitingRepository 포트의 구현체.
 * 대기 정보의 영속성을 담당한다.
 */
@Repository
public class WaitingRepositoryAdapter implements WaitingRepository {
    
    private final WaitingJpaRepository waitingJpaRepository;
    private final WaitingMapper waitingMapper;
    
    public WaitingRepositoryAdapter(
            WaitingJpaRepository waitingJpaRepository,
            WaitingMapper waitingMapper) {
        this.waitingJpaRepository = waitingJpaRepository;
        this.waitingMapper = waitingMapper;
    }
    
    @Override
    public Waiting save(Waiting waiting) {
        WaitingEntity entity = waitingMapper.toEntity(waiting);
        WaitingEntity savedEntity = waitingJpaRepository.save(entity);
        
        // 저장된 엔티티를 도메인 모델로 변환
        // 필요한 정보들은 이미 waiting 객체에 있으므로 그대로 사용
        return waitingMapper.toDomain(savedEntity, waiting.popup(), waiting.member());
    }
    
    @Override
    public List<Waiting> findByQuery(WaitingQuery query) {
        // WaitingQuery의 memberId를 사용하여 해당 회원의 대기 목록을 조회
        // 현재는 간단하게 구현하고, 나중에 필요에 따라 확장
        List<WaitingEntity> entities = waitingJpaRepository.findByMemberIdOrderByCreatedAtDesc(
                query.memberId(), PageRequest.of(0, query.size()));
        
        return entities.stream()
                .map(entity -> {
                    // 엔티티에서 기본 정보만 추출하여 임시 도메인 모델 생성
                    // 실제 사용 시에는 필요한 정보를 별도로 조회해야 함
                    return waitingMapper.toDomain(entity, null, null);
                })
                .toList();
    }
    
    @Override
    public Integer getNextWaitingNumber(Long popupId) {
        return waitingJpaRepository.findMaxWaitingNumberByPopupId(popupId).orElse(0) + 1;
    }
} 