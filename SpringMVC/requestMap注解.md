RequestMapping注解用来将URL映射到方法上。

当在类上使用@RequestMapping时映射的一级目录，然后再类中的方法上使用@RequestMapping就是指定的二级目录

```java
@RequestMapping(path="/user")
public class User{
    @RequestMapping("/hello")
    public String hello(){
        return "success"
    }
}
此时想要访问hello方法，路径就为：/user/hello
```

**RequestMapping属性：**

> value与path这两个属性是一个功能。都是指映射的url
>
> mathod：用于指定该放法可以接受的请求的方式
>
> > method={RequestMethod.POST, RequestMethod.GET}
>
> params：用于指定限制请求参数的条件,请求者必须传该属性定义的参数
>
> > params={"username"}  //请求必须带username参数
> >
> > 并且可以指定username的值必须等于设定值 : params={"username=hh"}
> >
> > 还可以指定参数的值不可以是设定值：params={"money!100"}
>
> headers：用于指定发送的请求中必须包含的请求头
>
> > headers={"Accept"} //请求头中必须包含Accrpt字段

