package webtoon.payment.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webtoon.payment.dtos.PaymentDto;
import webtoon.payment.entities.PaymentEntity;
import webtoon.payment.models.PaymentModel;
import webtoon.payment.repositories.IPaymentRepository;
import webtoon.payment.services.IPaymentService;

import java.util.List;

@Service
public class PaymentServiceImpl implements IPaymentService {
    @Autowired
    private IPaymentRepository paymentRepository;

    @Override
    public PaymentEntity add(PaymentEntity paymentModel) {
        PaymentEntity paymentEntity = PaymentEntity.builder()
                .orderId(paymentModel.getOrderId())
                .transId(paymentModel.getTransId())
                .transTrackNumber(paymentModel.getTransTrackNumber())
                .payMoney(paymentModel.getPayMoney())
                .bank(paymentModel.getBank())
                .status(paymentModel.getStatus())
                .paymentContent(paymentModel.getPaymentContent())
                .payUrl(paymentModel.getPayUrl())
                .expired_date(paymentModel.getExpired_date())
                .build();
        return this.paymentRepository.saveAndFlush(paymentEntity);
    }

    @Override
    public PaymentDto update(PaymentEntity PaymentEntity) {
        PaymentEntity paymentEntity = this.getById(PaymentEntity.getId());
        paymentEntity.setOrderId(PaymentEntity.getOrderId());
        paymentEntity.setTransId(PaymentEntity.getTransId());
        paymentEntity.setTransTrackNumber(PaymentEntity.getTransTrackNumber());
        paymentEntity.setPayMoney(PaymentEntity.getPayMoney());
        paymentEntity.setBank(PaymentEntity.getBank());
        paymentEntity.setStatus(PaymentEntity.getStatus());
        paymentEntity.setPaymentContent(PaymentEntity.getPaymentContent());
        paymentEntity.setPayUrl(PaymentEntity.getPayUrl());
        paymentEntity.setExpired_date(PaymentEntity.getExpired_date());
        this.paymentRepository.saveAndFlush(paymentEntity);

        return PaymentDto.builder()
                .orderId(paymentEntity.getOrderId())
                .transTrackNumber(paymentEntity.getTransTrackNumber())
                .payMoney(paymentEntity.getPayMoney())
                .bank(paymentEntity.getBank())
                .status(paymentEntity.getStatus())
                .paymentContent(paymentEntity.getPaymentContent())
                .payUrl(paymentEntity.getPayUrl())
                .expired_date(paymentEntity.getExpired_date())
                .build();
    }

    @Override
    public List<PaymentEntity> getAll() {
        return this.paymentRepository.findAll();
    }

    @Override
    public Long getIdByOrderId(Long id) {
        return this.paymentRepository.getIdByOrderId(id);
    }


    public PaymentEntity getById(Long id){
        return this.paymentRepository.findById(id).get();
    }
}
