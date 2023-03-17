package webtoon.payment.services;

import webtoon.payment.dtos.PaymentStatusDto;
import webtoon.payment.models.PaymentStatusModel;

public interface IPaymentStatusService {
    PaymentStatusDto add(PaymentStatusModel paymentStatusModel);
    PaymentStatusDto update(PaymentStatusModel paymentStatusModel);
}
