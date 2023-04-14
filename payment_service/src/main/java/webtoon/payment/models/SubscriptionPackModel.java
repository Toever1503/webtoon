package webtoon.payment.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPackModel {
    private Long id;
    private String name;
    private String desc;
    private Integer dayCount;
    private Double price;
    private Date createdAt;
    private Date modifiedAt;



}
