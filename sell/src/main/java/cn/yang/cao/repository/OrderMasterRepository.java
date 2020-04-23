package cn.yang.cao.repository;

import cn.yang.cao.dataobject.OrderMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderMasterRepository extends JpaRepository<OrderMaster, String> {

    Page<OrderMaster> findByBuyerPhone(String buyerOpenid, Pageable pageable);

    List<OrderMaster> findByBuyerPhone(String buyerOpenid);

}
