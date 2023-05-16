package webtoon.main.payment.services;

import webtoon.main.payment.dtos.PaymentDto;
import webtoon.main.payment.entities.PaymentEntity;

import java.util.List;

public interface IPaymentService {
    PaymentEntity add(PaymentEntity paymentModel);
    PaymentDto update(PaymentEntity PaymentEntity);
    List<PaymentEntity> getAll();
    Long getIdByOrderId(Long id);
}
