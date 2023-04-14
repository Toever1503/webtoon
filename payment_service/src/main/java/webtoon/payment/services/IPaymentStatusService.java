package webtoon.payment.services;

import webtoon.payment.dtos.PaymentStatusDto;
import webtoon.payment.entities.PaymentStatusEntity;
import webtoon.payment.models.PaymentStatusModel;

import java.util.List;

public interface IPaymentStatusService {
    PaymentStatusDto add(PaymentStatusModel paymentStatusModel);
    PaymentStatusDto update(PaymentStatusModel paymentStatusModel);
    List<PaymentStatusEntity> getAll();
}
