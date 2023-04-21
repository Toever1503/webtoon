package webtoon.payment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import webtoon.account.entities.UserEntity;
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

}
