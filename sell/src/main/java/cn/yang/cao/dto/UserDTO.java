package cn.yang.cao.dto;

import cn.yang.cao.dataobject.OrderMaster;
import cn.yang.cao.utils.serializer.Date2Long;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.Date;
import java.util.List;

@Data
public class UserDTO {

    private String openid;

    private String username;

    private String address;

    private Double money;

    @JsonSerialize(using = Date2Long.class)
    private Date createTime;

    List<OrderMaster> orderMasterList;
}
