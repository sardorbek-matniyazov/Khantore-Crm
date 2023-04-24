package khantorecrm.repository;

import khantorecrm.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Modifying
    @Query(
            value = "update payment set payment_order_type = 'CONFIRMED' where created_by_id = ?1 AND payment_order_type = ?4 AND created_at BETWEEN ?2 AND ?3",
            nativeQuery = true
    )
    Integer updatePaymentsByPeriod(Long createdById, Timestamp startDate, Timestamp endDate, String orderType);

    @Query(
            value = "select sum(payment_amount) from payment  where created_by_id = ?1 AND payment_order_type = ?4 AND created_at BETWEEN ?2 AND ?3",
            nativeQuery = true
    )
    Double selectPaymentsByPeriod(Long createdById, Timestamp time, Timestamp time1, String name);
}
