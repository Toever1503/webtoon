package webtoon.payment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import webtoon.payment.entities.PaymentStatusEntity;

public interface IPaymentStatusRepository extends JpaRepository<PaymentStatusEntity, Long>, JpaSpecificationExecutor<PaymentStatusEntity> {


}
