package webtoon.main.payment.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webtoon.main.account.configs.security.SecurityUtils;
import webtoon.main.payment.dtos.SubscriptionPackDto;
import webtoon.main.payment.dtos.SubscriptionPackMetadataDto;
import webtoon.main.payment.entities.SubscriptionPackEntity;
import webtoon.main.payment.models.SubscriptionPackModel;
import webtoon.main.payment.repositories.ISubscriptionPackRepository;
import webtoon.main.payment.services.ISubscriptionPackService;
import webtoon.main.utils.DateUtil;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SubscriptionPackServiceImpl implements ISubscriptionPackService {

    @Autowired
    private ISubscriptionPackRepository subscriptionPackRepository;

    @Override
    public SubscriptionPackDto addSubscriptionPack(SubscriptionPackModel subscriptionPackModel) {
        SubscriptionPackEntity subscriptionPackEntity = SubscriptionPackModel.toEntity(subscriptionPackModel);
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
//        subscriptionPackEntity.setName(subscriptionPackModel.getName());
        subscriptionPackEntity.setDescription(subscriptionPackModel.getDescription());
        subscriptionPackEntity.setPrice(subscriptionPackModel.getPrice());
//        subscriptionPackEntity.setMonthCount(subscriptionPackModel.getMonthCount());
        subscriptionPackEntity.setUpdatedBy(SecurityUtils.getCurrentUser().getUser());
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
            SubscriptionPackEntity entity =  this.getById(id);
            entity.setDeletedAt(Calendar.getInstance().getTime());
            entity.setUpdatedBy(SecurityUtils.getCurrentUser().getUser());
            this.subscriptionPackRepository.saveAndFlush(entity);
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
    public List<SubscriptionPackMetadataDto> getAllPackMetadata() {
        return subscriptionPackRepository.findAll().stream().map(subscriptionPackEntity ->
                SubscriptionPackMetadataDto.toDto(subscriptionPackEntity)).collect(Collectors.toList());
    }

    @Override
    public void toggleStatus(Long id) {
        SubscriptionPackEntity subscriptionPackEntity = this.getById(id);
        subscriptionPackEntity.setUpdatedBy(SecurityUtils.getCurrentUser().getUser());
        this.subscriptionPackRepository.saveAndFlush(subscriptionPackEntity);
    }

    @Override
    public List<SubscriptionPackEntity> findAllEntity(Specification spec) {
        return this.subscriptionPackRepository.findAll(spec);
    }

    @Override
    public void renewSubscriptionPack(Integer userId) {

    }

    @Override
    public List<SubscriptionPackDto> getAll() {
        return subscriptionPackRepository.findAll().stream().map(subscriptionPackEntity ->
                SubscriptionPackDto.toDto(subscriptionPackEntity)).collect(Collectors.toList());
    }

    @Override
    public SubscriptionPackEntity getByPrice(Double price) {
        return subscriptionPackRepository.getByPrice(price);
    }
}
