package webtoon.payment.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import webtoon.payment.dtos.SubscriptionPackDto;
import webtoon.payment.dtos.SubscriptionPackMetadataDto;
import webtoon.payment.entities.SubscriptionPackEntity;
import webtoon.payment.models.SubscriptionPackModel;

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
}
