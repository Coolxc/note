package cn.yang.cao.repository;

import cn.yang.cao.dataobject.SellerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerInfoRepository extends JpaRepository<SellerInfo, String> {
    SellerInfo findSellerInfoByPhone(String phone);
}
