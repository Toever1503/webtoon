package webtoon.payment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import webtoon.payment.entities.PaymentEntity;

public interface IPaymentRepository extends JpaRepository<PaymentEntity, Long>, JpaSpecificationExecutor<PaymentEntity> {


    @Query("SELECT p.id from tbl_payment p where p.orderId.id = ?1")
    Long getIdByOrderId(Long id);

    @Query("DELETE FROM tbl_payment p where p.orderId.id = ?1")
    void DeleteByOrderId(Long id);
}
