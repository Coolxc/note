package cn.yang.config;

/**
 * 封装了执行的SQL语句和结果类型的全限定类名
 */
public class Mapper {
    private String queryString; //SQL语句
    private String resultType; //返回值实体类的全限定类名

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }
}
