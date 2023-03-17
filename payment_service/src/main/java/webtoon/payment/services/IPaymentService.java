package webtoon.payment.services;

import webtoon.payment.dtos.PaymentDto;
import webtoon.payment.entities.PaymentEntity;
import webtoon.payment.models.PaymentModel;

public interface IPaymentService {
    PaymentDto add(PaymentModel paymentModel);
    PaymentDto update(PaymentModel paymentModel);
}
