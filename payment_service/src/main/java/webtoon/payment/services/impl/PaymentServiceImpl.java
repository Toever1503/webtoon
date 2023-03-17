package webtoon.payment.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webtoon.payment.dtos.PaymentDto;
import webtoon.payment.entities.PaymentEntity;
import webtoon.payment.models.PaymentModel;
import webtoon.payment.repositories.IPaymentRepository;
import webtoon.payment.services.IPaymentService;

@Service
public class PaymentServiceImpl implements IPaymentService {
    @Autowired
    private IPaymentRepository paymentRepository;

    @Override
    public PaymentDto add(PaymentModel paymentModel) {
        PaymentEntity paymentEntity = PaymentEntity.builder()
                .orderId(paymentModel.getOrderId())
                .transId(paymentModel.getTransId())
                .transTrackNumber(paymentModel.getTransTrackNumber())
                .payMoney(paymentModel.getPayMoney())
                .bank(paymentModel.getBank())
                .paymentContent(paymentModel.getPaymentContent())
                .payUrl(paymentModel.getPayUrl())
                .expired_date(paymentModel.getExpired_date())
                .build();
        this.paymentRepository.saveAndFlush(paymentEntity);
        return PaymentDto.builder()
                .orderId(paymentEntity.getOrderId())
                .transId(paymentEntity.getTransId())
                .transTrackNumber(paymentEntity.getTransTrackNumber())
                .payMoney(paymentEntity.getPayMoney())
                .bank(paymentEntity.getBank())
                .paymentContent(paymentEntity.getPaymentContent())
                .payUrl(paymentEntity.getPayUrl())
                .expired_date(paymentEntity.getExpired_date())
                .build();
    }

    @Override
    public PaymentDto update(PaymentModel paymentModel) {
        PaymentEntity paymentEntity = this.getById(paymentModel.getId());
        paymentEntity.setOrderId(paymentModel.getOrderId());
        paymentEntity.setTransId(paymentModel.getTransId());
        paymentEntity.setTransTrackNumber(paymentModel.getTransTrackNumber());
        paymentEntity.setPayMoney(paymentModel.getPayMoney());
        paymentEntity.setBank(paymentModel.getBank());
        paymentEntity.setPaymentContent(paymentModel.getPaymentContent());
        paymentEntity.setPayUrl(paymentModel.getPayUrl());
        paymentEntity.setExpired_date(paymentModel.getExpired_date());
        this.paymentRepository.saveAndFlush(paymentEntity);

        return PaymentDto.builder()
                .orderId(paymentEntity.getOrderId())
                .transId(paymentEntity.getTransId())
                .transTrackNumber(paymentEntity.getTransTrackNumber())
                .payMoney(paymentEntity.getPayMoney())
                .bank(paymentEntity.getBank())
                .paymentContent(paymentEntity.getPaymentContent())
                .payUrl(paymentEntity.getPayUrl())
                .expired_date(paymentEntity.getExpired_date())
                .build();
    }

    public PaymentEntity getById(Long id){
        return this.paymentRepository.findById(id).get();
    }
}
