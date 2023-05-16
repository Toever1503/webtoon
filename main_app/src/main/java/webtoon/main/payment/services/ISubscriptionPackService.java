package webtoon.main.payment.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import webtoon.main.payment.dtos.SubscriptionPackDto;
import webtoon.main.payment.dtos.SubscriptionPackMetadataDto;
import webtoon.main.payment.entities.SubscriptionPackEntity;
import webtoon.main.payment.models.SubscriptionPackModel;

import java.util.List;

public interface ISubscriptionPackService {
    SubscriptionPackDto addSubscriptionPack(SubscriptionPackModel subscriptionPackModel);
    SubscriptionPackDto updateSubscriptionPack(SubscriptionPackModel subscriptionPackModel);
    List<SubscriptionPackDto> getAll();
    SubscriptionPackEntity getByPrice(Double price);
    SubscriptionPackEntity getById(Long id);
    void deleteById(Long id);

    Page<SubscriptionPackDto> filter(Pageable pageable, Specification<SubscriptionPackEntity> spec);

    List<SubscriptionPackMetadataDto> getAllPackMetadata();

    void toggleStatus(Long id);

    List<SubscriptionPackEntity>  findAllEntity(Specification spec);

    void renewSubscriptionPack(Integer userId);
}
