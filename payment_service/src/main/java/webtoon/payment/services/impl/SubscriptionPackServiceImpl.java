package webtoon.payment.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import webtoon.payment.dtos.SubscriptionPackDto;
import webtoon.payment.entities.SubscriptionPackEntity;
import webtoon.payment.models.SubscriptionPackModel;
import webtoon.payment.repositories.ISubscriptionPackRepository;
import webtoon.payment.services.ISubscriptionPackService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionPackServiceImpl implements ISubscriptionPackService {

    @Autowired
    private ISubscriptionPackRepository subscriptionPackRepository;

    @Override
    public SubscriptionPackDto addSubscriptionPack(SubscriptionPackModel subscriptionPackModel) {
        SubscriptionPackEntity subscriptionPackEntity = SubscriptionPackEntity.builder()
                .name(subscriptionPackModel.getName())
                .desc(subscriptionPackModel.getDesc())
                .dayCount(subscriptionPackModel.getDayCount())
                .price(subscriptionPackModel.getPrice())
                .createdAt(subscriptionPackModel.getCreatedAt())
                .modifiedAt(subscriptionPackModel.getModifiedAt())
                .build();
        this.subscriptionPackRepository.saveAndFlush(subscriptionPackEntity);
        return SubscriptionPackDto.builder()
                .name(subscriptionPackEntity.getName())
                .desc(subscriptionPackEntity.getDesc())
                .dayCount(subscriptionPackEntity.getDayCount())
                .price(subscriptionPackEntity.getPrice())
                .createdAt(subscriptionPackEntity.getCreatedAt())
                .modifiedAt(subscriptionPackEntity.getModifiedAt())
                .build();
    }

    @Override
    public SubscriptionPackDto updateSubscriptionPack(SubscriptionPackModel subscriptionPackModel) {
        SubscriptionPackEntity subscriptionPackEntity = this.getById(subscriptionPackModel.getId());
        subscriptionPackEntity.setName(subscriptionPackModel.getName());
        subscriptionPackEntity.setDesc(subscriptionPackModel.getDesc());
        subscriptionPackEntity.setDayCount(subscriptionPackModel.getDayCount());
        subscriptionPackEntity.setPrice(subscriptionPackModel.getPrice());
        subscriptionPackEntity.setCreatedAt(subscriptionPackModel.getCreatedAt());
        subscriptionPackEntity.setModifiedAt(subscriptionPackModel.getModifiedAt());
        this.subscriptionPackRepository.saveAndFlush(subscriptionPackEntity);
        return SubscriptionPackDto.builder()
                .name(subscriptionPackEntity.getName())
                .desc(subscriptionPackEntity.getDesc())
                .dayCount(subscriptionPackEntity.getDayCount())
                .price(subscriptionPackEntity.getPrice())
                .createdAt(subscriptionPackEntity.getCreatedAt())
                .modifiedAt(subscriptionPackEntity.getModifiedAt())
                .build();
    }
   @Override
   public SubscriptionPackEntity getById(Long id) {
        return this.subscriptionPackRepository.findById(id).get();
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
