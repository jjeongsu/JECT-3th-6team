package com.example.demo.infrastructure.persistence.repository;

import com.example.demo.infrastructure.persistence.entity.popup.PopupLocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopupLocationRepository extends JpaRepository<PopupLocationEntity, Long> {

}
