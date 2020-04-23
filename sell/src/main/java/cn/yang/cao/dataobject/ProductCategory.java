package cn.yang.cao.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.Date;

//使用JPA注解配置映射关系
@Entity  //告诉JPA这是一个实体类（和数据表映射的类）还可以有一个@Table注解来表明和哪个表映射，这里可以用类名与表明相同的方法就不用加@Table了
@Proxy(lazy = false)
@DynamicUpdate //动态更新表中数据，如时间
@Data //自动为所有属性生成Getter和Setter和toString方法
public class ProductCategory {
    //这是一个主键，并且自增
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //属性必须要和表字段名保持一致，和类名一样大写的字母会被翻译成下划线加小写字母，第一个字母一定要小写
    //或者加@column注解精准指定
    private Integer categoryId;
    private String categoryName;

    //类目编号，唯一
    private Integer categoryType;

    private Date createTime;
    private Date updateTime;

    //在接口中自定义查询时要定义一个无参的构造方法
    public ProductCategory() {
    }
}
