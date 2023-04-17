package webtoon.payment.services;

import webtoon.payment.dtos.SubscriptionPackDto;
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
}
