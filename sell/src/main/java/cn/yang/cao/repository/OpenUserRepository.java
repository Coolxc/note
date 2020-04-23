package cn.yang.cao.repository;

import cn.yang.cao.dataobject.OpenUser;
import cn.yang.cao.dataobject.OrderMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OpenUserRepository extends JpaRepository<OpenUser, String> {

    OpenUser findOpenUserByPhone(String phone);

}
