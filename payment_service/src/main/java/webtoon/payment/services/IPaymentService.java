package webtoon.payment.services;

import webtoon.payment.dtos.OrderDto;
import webtoon.payment.dtos.PaymentDto;
import webtoon.payment.entities.PaymentEntity;
import webtoon.payment.models.PaymentModel;

import java.util.List;

public interface IPaymentService {
    PaymentEntity add(PaymentEntity paymentModel);
    PaymentDto update(PaymentModel paymentModel);
    List<PaymentEntity> getAll();
}
