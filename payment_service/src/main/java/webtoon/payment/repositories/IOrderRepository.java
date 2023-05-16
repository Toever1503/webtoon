package webtoon.payment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import webtoon.payment.dtos.OrderPendingDTO;
import webtoon.payment.entities.OrderEntity;
import webtoon.payment.enums.EOrderStatus;

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

    @Query("select o From tbl_order o where o.user_id.id= ?1 AND o.status = ?2")
    List<OrderEntity> getPaymentConfirmByUserId(Long userId,EOrderStatus status);

    @Query("select o from tbl_order o where o.user_id.id = ?1 and o.status = ?2 and(?3 IS NULL OR o.maDonHang LIKE %?3% or o.subs_pack_id.name like %?3%) ")
    List<OrderEntity> searchByUserId(Long userId,EOrderStatus status, String search);

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
            "WHERE created_at between DATE_SUB(CURRENT_DATE, INTERVAL 7 DAY) and CURRENT_DATE  and status = 'COMPLETED' \n" +
            "GROUP by DATE_FORMAT(created_at, \"%Y-%m-%d\")", nativeQuery = true)
    List<Object[]> sumTotalRevenueInLast7Days();

    @Query("select sum(o.finalPrice) from tbl_order o where o.status = 'COMPLETED' and function('date_format', o.created_at, '%Y, %m') = function('date_format', CURRENT_DATE, '%Y, %m')")
    Long totalRevenueThisMonth();

    @Query("select count(o) from tbl_order o where o.status = 'COMPLETED' AND function('date_format', o.created_at, '%Y, %m') >= function('date_format', CURRENT_DATE, '%Y, %m')")
    Long countTotalRegisterThisMonth();


    @Query(value = "SELECT DATE_FORMAT(created_at, \"%Y-%m\") as k, SUM(final_price) as v FROM `tbl_order` \n" +
            "WHERE DATE_FORMAT(created_at, '%Y') = :year and status = 'COMPLETED' \n" +
            "GROUP by DATE_FORMAT(created_at, \"%Y-%m\")", nativeQuery = true)
    List<Object[]> sumRevenuePerMonthByYear(@Param("year") String monthDate);

    @Query(value = "SELECT DATE_FORMAT(created_at, \"%Y-%m-%d\") as k, SUM(final_price) as v FROM `tbl_order` \n" +
            "WHERE DATE_FORMAT(created_at, '%Y-%m') = :month and status = 'COMPLETED' \n" +
            "GROUP by DATE_FORMAT(created_at, \"%Y-%m-%d\")", nativeQuery = true)
    List<Object[]> sumRevenuePerDayByMonth(@Param("month") String monthDate);

    @Query(value = "SELECT S.ID, s.subs_name, sum(o.final_price) FROM `tbl_order` as o\n" +
            "JOIN tbl_subscription_pack as s on o.subs_pack_id = s.id\n" +
            "WHERE o.status = 'COMPLETED' and DATE_FORMAT(o.created_at, '%Y-%m') = :month\n" +
            "GROUP BY s.id", nativeQuery = true)
    List<Object[]> sumRevenuePerSubsPackByMonth(@Param("month") String monthDate);

    @Query(value = "WITH RECURSIVE month_list (d) \n" +
            "\tAS (\n" +
            "\t\t  SELECT 1\n" +
            "\t\t  UNION ALL\n" +
            "\t\t  SELECT d+1\n" +
            "\t\t  FROM month_list \n" +
            "\t\t  WHERE d <= 12\n" +
            "\t\t)    \n" +
            "SELECT m.d AS k, COUNT(o.id) as v, 'Gia hạn thêm' \n" +
            "FROM month_list as m\n" +
            "LEFT JOIN `tbl_order` as o \n" +
            "on m.d = DATE_FORMAT(o.created_at, '%m') and o.order_type = 'RENEW' AND :year = DATE_FORMAT(o.created_at, '%Y')\n" +
            "GROUP BY m.d\n" +
            "\n", nativeQuery = true)
    List<Object[]> calcRenewOrderPerMonthByYear(@Param("year") String year);

    @Query(value = "WITH RECURSIVE month_list (d) \n" +
            "\tAS (\n" +
            "\t\t  SELECT 1\n" +
            "\t\t  UNION ALL\n" +
            "\t\t  SELECT d+1\n" +
            "\t\t  FROM month_list \n" +
            "\t\t  WHERE d <= 12\n" +
            "\t\t)    \n" +
            "SELECT m.d AS k, COUNT(u.id) as v, 'Không gia hạn' \n" +
            "FROM month_list as m\n" +
            "LEFT JOIN `tbl_user` as u \n" +
            "on m.d = DATE_FORMAT(u.can_read_until_date, '%m') AND DATE_FORMAT(u.can_read_until_date, '%y') = :year AND u.can_read_until_date is not null AND u.can_read_until_date <= CURRENT_DATE\n" +
            "GROUP BY m.d\n" +
            "\n", nativeQuery = true)
    List<Object[]> calcNotRenewOrderPerMonthByYear(@Param("year") String year);


}
