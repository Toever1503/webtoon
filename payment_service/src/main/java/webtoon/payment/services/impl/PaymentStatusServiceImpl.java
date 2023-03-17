package webtoon.payment.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webtoon.payment.dtos.PaymentStatusDto;
import webtoon.payment.entities.PaymentStatusEntity;
import webtoon.payment.models.PaymentStatusModel;
import webtoon.payment.repositories.IPaymentStatusRepository;
import webtoon.payment.services.IPaymentStatusService;
@Service
public class PaymentStatusServiceImpl implements IPaymentStatusService {
    @Autowired
    private IPaymentStatusRepository paymentStatusRepository;
    @Override
    public PaymentStatusDto add(PaymentStatusModel paymentStatusModel) {
        PaymentStatusEntity paymentStatusEntity = PaymentStatusEntity.builder()
                .orderId(paymentStatusModel.getOrderId())
                .status(paymentStatusModel.getStatus())
                .created_at(paymentStatusModel.getCreated_at())
                .modified_at(paymentStatusModel.getModified_at())
                .build();
        this.paymentStatusRepository.saveAndFlush(paymentStatusEntity);
        return PaymentStatusDto.builder()
                .orderId(paymentStatusEntity.getOrderId())
                .status(paymentStatusEntity.getStatus())
                .created_at(paymentStatusEntity.getCreated_at())
                .modified_at(paymentStatusEntity.getModified_at())
                .build();
    }

    @Override
    public PaymentStatusDto update(PaymentStatusModel paymentStatusModel) {
        PaymentStatusEntity paymentStatusEntity = this.getById(paymentStatusModel.getId());
        paymentStatusEntity.setOrderId(paymentStatusModel.getOrderId());
        paymentStatusEntity.setStatus(paymentStatusModel.getStatus());
        paymentStatusEntity.setCreated_at(paymentStatusModel.getCreated_at());
        paymentStatusEntity.setModified_at(paymentStatusModel.getModified_at());
        this.paymentStatusRepository.saveAndFlush(paymentStatusEntity);
        return PaymentStatusDto.builder()
                .orderId(paymentStatusEntity.getOrderId())
                .status(paymentStatusEntity.getStatus())
                .created_at(paymentStatusEntity.getCreated_at())
                .modified_at(paymentStatusEntity.getModified_at())
                .build();
    }

    public PaymentStatusEntity getById(Long id) {
        return this.paymentStatusRepository.findById(id).get();
    }
}
