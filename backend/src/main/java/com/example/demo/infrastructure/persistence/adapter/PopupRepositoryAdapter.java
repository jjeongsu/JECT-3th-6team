package com.example.demo.infrastructure.persistence.adapter;

import com.example.demo.domain.model.PopupDetail;
import com.example.demo.domain.port.PopupRepository;
import com.example.demo.infrastructure.persistence.mapper.PopupMapper;
import com.example.demo.infrastructure.persistence.repository.PopupJpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * PopupRepository 포트의 구현체.
 * 팝업 정보의 영속성을 담당한다.
 */
@Repository
public class PopupRepositoryAdapter implements PopupRepository {
    
    private final PopupJpaRepository popupJpaRepository;
    private final PopupMapper popupMapper;
    
    public PopupRepositoryAdapter(PopupJpaRepository popupJpaRepository, PopupMapper popupMapper) {
        this.popupJpaRepository = popupJpaRepository;
        this.popupMapper = popupMapper;
    }
    
    @Override
    public Optional<PopupDetail> findById(Long id) {
        return popupJpaRepository.findById(id)
                .map(popupMapper::toDomain);
    }
} 