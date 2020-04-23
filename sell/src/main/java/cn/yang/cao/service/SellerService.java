package cn.yang.cao.service;

import cn.yang.cao.dataobject.SellerInfo;

public interface SellerService {

    //通过id查询卖家信息
    SellerInfo findSellerInfoById(String id);

    //通过电话查询卖家信息
    SellerInfo findSellerInfoByPhone(String phone);

    void create(SellerInfo sellerInfo);
}
