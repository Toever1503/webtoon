package webtoon.payment.services;

import webtoon.payment.dtos.PaymentDto;
import webtoon.payment.entities.PaymentEntity;
import webtoon.payment.models.PaymentModel;

import java.util.List;

public interface IPaymentService {
    PaymentEntity add(PaymentEntity paymentModel);
    PaymentDto update(PaymentEntity PaymentEntity);
    List<PaymentEntity> getAll();
    Long getIdByOrderId(Long id);
}
