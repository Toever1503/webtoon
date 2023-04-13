package webtoon.payment.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webtoon.payment.dtos.OrderDto;
import webtoon.payment.dtos.PaymentDto;
import webtoon.payment.entities.OrderEntity;
import webtoon.payment.entities.PaymentEntity;
import webtoon.payment.models.PaymentModel;
import webtoon.payment.repositories.IOrderRepository;
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
                .paymentContent(paymentModel.getPaymentContent())
                .payUrl(paymentModel.getPayUrl())
                .expired_date(paymentModel.getExpired_date())
                .build();
        return this.paymentRepository.saveAndFlush(paymentEntity);
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
                .transTrackNumber(paymentEntity.getTransTrackNumber())
                .payMoney(paymentEntity.getPayMoney())
                .bank(paymentEntity.getBank())
                .paymentContent(paymentEntity.getPaymentContent())
                .payUrl(paymentEntity.getPayUrl())
                .expired_date(paymentEntity.getExpired_date())
                .build();
    }

    @Override
    public List<PaymentEntity> getAll() {
        return this.paymentRepository.findAll();
    }

    public PaymentEntity getById(Long id){
        return this.paymentRepository.findById(id).get();
    }
}
