package webtoon.payment.entities;

import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import webtoon.payment.enums.EOrderType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tbl_order")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="total_price")
    private Double totalPrice;

    @Column(name="discount_price")
    private Double discountPrice;

    @Column(name="final_discount_price")
    private Double finalDiscountPrice;

    @Column(name="final_price")
    private Double finalPrice;

    @Column(name="payment_method")
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name="order_type")
    private EOrderType orderType;

    @Column(name="note")
    private String note;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date created_at;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date modified_at;
}
