package webtoon.payment.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import webtoon.payment.dtos.SubscriptionPackDto;
import webtoon.payment.entities.SubscriptionPackEntity;
import webtoon.payment.models.SubscriptionPackModel;
import webtoon.payment.repositories.ISubscriptionPackRepository;
import webtoon.payment.services.ISubscriptionPackService;
import webtoon.utils.DateUtil;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionPackServiceImpl implements ISubscriptionPackService {

    @Autowired
    private ISubscriptionPackRepository subscriptionPackRepository;

    @Override
    public SubscriptionPackDto addSubscriptionPack(SubscriptionPackModel subscriptionPackModel) {
        SubscriptionPackEntity subscriptionPackEntity = SubscriptionPackModel.toEntity(subscriptionPackModel);
        subscriptionPackEntity.setDayCount(this.getDateCount(subscriptionPackModel.getMonthCount()));
        this.subscriptionPackRepository.saveAndFlush(subscriptionPackEntity);
        return SubscriptionPackDto.toDto(subscriptionPackEntity);
    }

    protected int getDateCount(int dayCount) {
        Calendar currentDate = Calendar.getInstance();
        Calendar futureDate = Calendar.getInstance();
        futureDate.add(Calendar.MONTH, dayCount);
        return Long.valueOf(DateUtil.countDaysBetween2Date(currentDate.getTime(), futureDate.getTime())).intValue();
    }

    @Override
    public SubscriptionPackDto updateSubscriptionPack(SubscriptionPackModel subscriptionPackModel) {
        SubscriptionPackEntity subscriptionPackEntity = this.getById(subscriptionPackModel.getId());
        subscriptionPackEntity.setName(subscriptionPackModel.getName());
        subscriptionPackEntity.setDayCount(this.getDateCount(subscriptionPackModel.getMonthCount()));
        subscriptionPackEntity.setOriginalPrice(subscriptionPackModel.getOriginalPrice());
        subscriptionPackEntity.setDiscountPrice(subscriptionPackModel.getDiscountPrice());
        subscriptionPackEntity.setMonthCount(subscriptionPackModel.getMonthCount());

        this.subscriptionPackRepository.saveAndFlush(subscriptionPackEntity);
        return SubscriptionPackDto.toDto(subscriptionPackEntity);
    }

    @Override
    public SubscriptionPackEntity getById(Long id) {
        return this.subscriptionPackRepository.findById(id).get();
    }

    @Override
    public void deleteById(Long id) {
        try {
            this.subscriptionPackRepository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Page<SubscriptionPackDto> filter(Pageable pageable, Specification<SubscriptionPackEntity> spec) {
        return this.subscriptionPackRepository.findAll(spec, pageable).map(SubscriptionPackDto::toDto);
    }

    @Override
    public List<SubscriptionPackDto> getAll() {
        Pageable pageable = PageRequest.of(0, 10).withSort(Sort.Direction.ASC, "id");
        return subscriptionPackRepository.findAll(pageable).stream().map(subscriptionPackEntity ->
                SubscriptionPackDto.toDto(subscriptionPackEntity)).collect(Collectors.toList());
    }

    @Override
    public SubscriptionPackEntity getByPrice(Double price) {
        return subscriptionPackRepository.getByPrice(price);
    }
}
