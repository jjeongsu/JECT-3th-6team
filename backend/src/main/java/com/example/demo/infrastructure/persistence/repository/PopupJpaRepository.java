package com.example.demo.infrastructure.persistence.repository;

import com.example.demo.domain.model.popup.PopupMapQuery;
import com.example.demo.infrastructure.persistence.entity.popup.PopupEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * PopupEntity를 위한 Spring Data JPA Repository.
 */
@Repository
public interface PopupJpaRepository extends JpaRepository<PopupEntity, Long> {

    @Query("""
            SELECT DISTINCT p FROM PopupEntity p \
            JOIN PopupLocationEntity pl ON p.popupLocationId = pl.id \
            LEFT JOIN PopupCategoryEntity pc ON p.id = pc.popupId \
            WHERE pl.latitude BETWEEN :#{#query.minLatitude} AND :#{#query.maxLatitude} \
            AND pl.longitude BETWEEN :#{#query.minLongitude} AND :#{#query.maxLongitude} \
            AND (:#{#query.type} IS NULL OR CAST(p.type AS string) = CAST(:#{#query.type} AS string)) \
            AND (
                ((:#{#query.dateRange.startDate()} IS NULL) OR (:#{#query.dateRange.endDate()} IS NULL))
                OR
                (p.startDate <= :#{#query.dateRange.endDate} AND p.endDate >= :#{#query.dateRange.startDate})
                ) \
            AND (:#{#query.categories} IS NULL OR pc.name IN (:#{#query.categories}))
            """
    )
    List<PopupEntity> findByMapQuery(@Param("query") PopupMapQuery query);
  
    /**
     * ID로 팝업을 조회한다.
     *
     * @param id 팝업 ID
     * @return 팝업 엔티티
     */
    Optional<PopupEntity> findById(Long id);

    @Query("""
        SELECT p FROM PopupEntity p
        WHERE (:popupId IS NULL OR p.id = :popupId)
        AND ((:startDate IS NULL OR :endDate IS NULL) OR (p.endDate >= :startDate AND p.startDate <= :endDate))
        AND (:types IS NULL OR p.type IN :types)
        AND EXISTS (
            SELECT 1 FROM PopupCategoryEntity c
            WHERE c.popupId = p.id AND (:categories IS NULL OR c.name IN :categories)
        )
        AND EXISTS (
            SELECT 1 FROM PopupLocationEntity l
            WHERE l.id = p.popupLocationId AND (:region1DepthName IS NULL OR l.region1DepthName = :region1DepthName)
        )
        AND (:lastPopupId IS NULL OR p.id > :lastPopupId)
        ORDER BY p.startDate ASC, p.id ASC
    """)
    List<PopupEntity> findFilteredPopups(
        @Param("popupId") Long popupId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("types") List<String> types,
        @Param("categories") List<String> categories,
        @Param("region1DepthName") String region1DepthName,
        @Param("lastPopupId") Long lastPopupId,
        Pageable pageable
    );

    /**
     * 키워드 검색을 위한 팝업 조회 메서드.
     * 키워드가 팝업 제목에 포함되는 팝업을 검색한다.
     */
    @Query("""
        SELECT p FROM PopupEntity p
        WHERE (:popupId IS NULL OR p.id = :popupId)
        AND (:keyword IS NULL OR p.title LIKE CONCAT('%', :keyword, '%'))
        AND (:lastPopupId IS NULL OR p.id > :lastPopupId)
        ORDER BY p.startDate ASC, p.id ASC
    """)
    List<PopupEntity> findByKeyword(
        @Param("popupId") Long popupId,
        @Param("keyword") String keyword,
        @Param("lastPopupId") Long lastPopupId,
        Pageable pageable
    );
} 