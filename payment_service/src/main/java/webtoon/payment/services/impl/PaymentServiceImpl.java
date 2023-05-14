package webtoon.payment.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webtoon.payment.dtos.PaymentDto;
import webtoon.payment.entities.PaymentEntity;
import webtoon.payment.repositories.IPaymentRepository;
import webtoon.payment.services.IPaymentService;

import java.util.List;

@Service
@Transactional
public class PaymentServiceImpl implements IPaymentService {
    @Autowired
    private IPaymentRepository paymentRepository;

    @Override
    public PaymentEntity add(PaymentEntity paymentModel) {
        this.paymentRepository.saveAndFlush(paymentModel);
        return paymentModel;
    }

    @Override
    public PaymentDto update(PaymentEntity PaymentEntity) {
        return null;
    }

    @Override
    public List<PaymentEntity> getAll() {
        return this.paymentRepository.findAll();
    }

    @Override
    public Long getIdByOrderId(Long id) {
        return this.paymentRepository.getIdByOrderId(id);
    }


    public PaymentEntity getById(Long id) {
        return this.paymentRepository.findById(id).get();
    }
}
