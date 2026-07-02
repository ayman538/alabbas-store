package com.alabbas.store.repository;

import com.alabbas.store.dto.ReceivedProductsSummaryResponse;
import com.alabbas.store.entity.ReceivedProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReceivedProductRepository extends JpaRepository<ReceivedProduct, Long> {

    @Query(
            value = """
                SELECT new com.alabbas.store.dto.ReceivedProductsSummaryResponse (
                    r.transactionId,
                    SUM(r.amount),
                    SUM(r.quantity),
                    COUNT(r.id),
                    MAX(r.createdAt)
                )
                FROM ReceivedProduct r
                WHERE
                    (
                        CAST(:createdFrom AS timestamp) IS NULL
                        OR r.createdAt >= :createdFrom
                    )
                    AND
                    (
                        CAST(:createdTo AS timestamp) IS NULL
                        OR r.createdAt <= :createdTo
                    )
                GROUP BY r.transactionId
                ORDER BY MAX(r.createdAt) DESC
                """,
            countQuery = """
                SELECT COUNT(DISTINCT r.transactionId)
                FROM ReceivedProduct r
                WHERE
                    (
                        CAST(:createdFrom AS timestamp) IS NULL
                        OR r.createdAt >= :createdFrom
                    )
                    AND
                    (
                        CAST(:createdTo AS timestamp) IS NULL
                        OR r.createdAt <= :createdTo
                    )
                """
    )
    Page<ReceivedProductsSummaryResponse> findReceivedProductTransactions(
            @Param("createdFrom") LocalDateTime createdFrom,
            @Param("createdTo") LocalDateTime createdTo,
            Pageable pageable
    );

    List<ReceivedProduct> findByTransactionIdOrderByCreatedAtDesc(String transactionId);
}
