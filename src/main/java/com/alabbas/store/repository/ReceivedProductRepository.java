package com.alabbas.store.repository;

import com.alabbas.store.dto.ReceivedProductsSummaryResponse;
import com.alabbas.store.entity.ReceivedProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReceivedProductRepository extends JpaRepository<ReceivedProduct, Long> {

    @Query(
            value = """
                    SELECT new com.alabbas.store.dto.ReceivedProductsSummaryResponse(
                        r.transactionId,
                        SUM(r.amount),
                        SUM(r.quantity),
                        COUNT(r.id),
                        MAX(r.createdAt)
                    )
                    FROM ReceivedProduct r
                    GROUP BY r.transactionId
                    ORDER BY MAX(r.createdAt) DESC
                    """,
            countQuery = """
                    SELECT COUNT(DISTINCT r.transactionId)
                    FROM ReceivedProduct r
                    """
    )
    Page<ReceivedProductsSummaryResponse> findReceivedProductTransactions(Pageable pageable);

    List<ReceivedProduct> findByTransactionIdOrderByCreatedAtDesc(String transactionId);
}
