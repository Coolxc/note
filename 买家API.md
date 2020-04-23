## 创建用户 POST

```http
http://yangyouxiu.natapp1.cc/user/create
```

**参数：**

```json
"username": "杨优秀",
"password": "202cb962ac59075b964b07152d234b70",
"address": "安阳",
"phone": "18811111111"
```

**返回：**

```json
{
    "code": 0,
    "msg": "成功",
    "data": {
        "id": "1",
        "username": "杨优秀",
        "password": "202cb962ac59075b964b07152d234b70",
        "address": "安阳",
        "phone": "18811111111"
    }
}
```





## 删除用户  GET

```http
http://yangyouxiu.natapp1.cc/user/delete?phone=18811111111
```

**参数：phone**

**返回：**

```json
{
    "code": 0,
    "msg": "成功",
    "data": {
        "Id:{}": "1",
        "UserName:{}": "杨优秀"
    }
}
```





## 登录  POST

```http
http://yangyouxiu.natapp1.cc/user/login
```



**参数：**

```json
phone:18811111111
password:123
```

**返回：**重定向至商品列表页面

```http
redirect:/buyer/product/list
```



## 登出 GET

```http
http://yangyouxiu.natapp1.cc/user/logout
```

**无参数**

**返回：成功页面，并跳转到登录页面**

## 查看所有商品

```http
http://yangyouxiu.natapp1.cc/buyer/product/list
```



## 创建订单 POST

```http
POST /sell/buyer/order/create
```

参数   其中items包含商品id和商品数量

```json
name: "杨优秀"
phone: "18868822111"
address: "安阳"
id: "111"
items: [{
    productId: "1423113435324",
    productQuantity: 2 //购买数量
},productId: "634653623423",
    productQuantity: 3 //购买数量]

```

返回

```json
{
  "code": 0,
  "msg": "成功",
  "data": {
      "orderId": "147283992738221" 
  }
}
```



## 查看订单列表 GET

```http
http://yangyouxiu.natapp1.cc/buyer/order/list?phone=18868822111
```

**参数：phone。根据phone查询该用户的所有订单**

**返回：**

```json
{
    "code": 0,
    "msg": "成功",
    "data": [
        {
            "orderId": "158520261867863993",
            "buyerName": "杨优秀",
            "buyerPhone": "18868822111",
            "buyerAddress": "安阳",
            "buyerId": "1",
            "orderAmount": 24.00,
            "orderStatus": 1,
            "payStatus": 0,
            "createTime": 1585249419,
            "updateTime": 1585409442
        },
        {
            "orderId": "1585202662071100237",
            "buyerName": "杨优秀",
            "buyerPhone": "18868822111",
            "buyerAddress": "安阳",
            "buyerId": "1",
            "orderAmount": 24.00,
            "orderStatus": 1,
            "payStatus": 1,
            "createTime": 1585249462,
            "updateTime": 1585409430
        },
        {
            "orderId": "158520271758529325",
            "buyerName": "杨优秀",
            "buyerPhone": "18868822111",
            "buyerAddress": "安阳",
            "buyerId": "1",
            "orderAmount": 24.00,
            "orderStatus": 1,
            "payStatus": 0,
            "createTime": 1585249517,
            "updateTime": 1585409285
        }
    ]
}
```





## 查看订单详细信息  GET

```html
http://yangyouxiu.natapp1.cc/buyer/order/detail?orderid=158512259761739509&id=1
```

**参数：订单id 和 用户id       传入用户id是为了验证身份**

**返回：**

```json
{
    "code": 0,
    "msg": "成功",
    "data": {
        "orderId": "158512259761739509",
        "buyerName": "杨优秀",
        "buyerPhone": "18888888",
        "buyerAddress": "安阳",
        "buyerId": "1",
        "orderAmount": 63.60,
        "orderStatus": 1,
        "payStatus": 1,
        "createTime": 1585169397,
        "updateTime": 1585409838,
        "orderDetailList": [
            {
                "detailId": "1585122597722103077",
                "orderId": "158512259761739509",
                "productId": "1",
                "productName": "cookie",
                "productPrice": 12.00,
                "productQuantity": 2
            },
            {
                "detailId": "158512259776041492",
                "orderId": "158512259761739509",
                "productId": "2",
                "productName": "苹果",
                "productPrice": 9.90,
                "productQuantity": 4
            }
        ]
    }
}
```

## 取消订单 GET

```http
http://yangyouxiu.natapp1.cc/buyer/order/cancel?orderId=1585202662071100237&buyerId=1
```

**参数：订单 Id 与 用户 Id**

**返回：**

```json
{
    "code": 0,
    "msg": "成功"
}
```

