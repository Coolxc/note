UUID会根据你的地址和时间戳生成一个几乎不会重复的ID。身份标识类。

```java
UUID uuid = UUID.randomUUID();
System.out.println(uuid.toString());
//因为时间戳不同，输出的每一个UUID都不同
```

## base64加密

利用这个加密算法可以实现信息的加密处理。

```java
import java.util.Base64;

....main(String[] args){
    String msg = "杨优秀";
    //加密处理，信息要转换成字节
    String enmsg = Base64.getEncoder().encodeToString(msg.getBytes());
    System.out.println("加密后的数据：" + enmsg);
    byte data [] = Base4.getDecoder().decode(enmsg);
    System.out.println("解密后的数据：" + new String(data)); //将数组转换为字符串
}
```

有可能会将加密后数据再进行加密，加加加

以后会将Base64与MD5一块来加密。