package webtoon.payment.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.account.dtos.UserMetadataDto;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserSubscriptionPackStatusDto {
    private Long userId;
    private UserMetadataDto user;

    private SubscriptionPackDto subscriptionPack;

    private Date expiredDate;

    private Boolean hasSendRenewalEmail;
}
