package webtoon.payment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import webtoon.account.entities.UserEntity;
import webtoon.payment.dtos.OrderPendingDTO;
import webtoon.payment.entities.OrderEntity;

import java.util.List;

@Repository
public interface IOrderRepository extends JpaRepository<OrderEntity, Long>, JpaSpecificationExecutor<OrderEntity> {
    @Query("SELECT o FROM tbl_order o WHERE o.maDonHang = ?1")
    OrderEntity getByMaDonHang(String maDonHang);

    @Query("SELECT o FROM tbl_order o where o.user_id.id = ?1")
    List<OrderEntity> getByUserId(Long userId);

    @Query("SELECT o.id FROM tbl_order o where o.maDonHang = ?1")
    Long getIdByMaDonHang(String maDonHang);

    @Query("SELECT new webtoon.payment.dtos.OrderPendingDTO(o.id,o.user_id,o.maDonHang, o.subs_pack_id) FROM tbl_order o join tbl_payment p on o.id = p.orderId.id where o.user_id.id = ?1 and o.status = 'PENDING_PAYMENT' and o.paymentMethod='VN_PAY'")
    List<OrderPendingDTO> getPendingPaymentByUserId(Long userId);

    @Query("SELECT o FROM tbl_order o WHERE o.user_id.id = ?1 AND o.status = 'COMPLETED'")
    List<OrderEntity> getPaymentCompletedByUserId(Long userId);

    @Query("select count(o) from tbl_order o where o.status != 'DRAFTED' and function('date_format', o.created_at, '%Y, %m, %d') >= function('date_format', CURRENT_DATE, '%Y, %m, %d')")
    Long countTotalOrderInToday();


    @Query("select sum(o.finalPrice) from tbl_order o where o.status = 'COMPLETED' and function('date_format', o.created_at, '%Y, %m, %d') >= function('date_format', CURRENT_DATE, '%Y, %m, %d')")
    Long sumTotalRevenueInToday();

    @Query("select count(o) from tbl_order o where o.status = 'USER_CONFIRMED_BANKING' and function('date_format', o.created_at, '%Y, %m, %d') >= function('date_format', CURRENT_DATE, '%Y, %m, %d')")
    Long countTotalPaymentPendingInToday();
    @Query("select count(o) from tbl_order o where o.status = 'COMPLETED' and function('date_format', o.created_at, '%Y, %m, %d') >= function('date_format', CURRENT_DATE, '%Y, %m, %d')")
    Long countTotalCompletedOrderInToday();

    @Query("select count(o) from tbl_order o where o.status = 'CANCELED' and function('date_format', o.created_at, '%Y, %m, %d') >= function('date_format', CURRENT_DATE, '%Y, %m, %d')")
    Long countTotalCanceledOrderInToday();



    @Query(value = "SELECT DATE_FORMAT(created_at, \"%Y-%m-%d\") as k, SUM(final_price) as v FROM `tbl_order` \n" +
            "WHERE created_at >= DATE_SUB(CURRENT_DATE, INTERVAL 7 DAY) and status = 'COMPLETED' \n" +
            "GROUP by DATE_FORMAT(created_at, \"%Y-%m-%d\")", nativeQuery = true)
    List<Object[]> sumTotalRevenueInLast7Days();
}
