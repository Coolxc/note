**在项目开发中不会有使用Statement，而会使用PreparedStatement，Statement拼接字符串太麻烦了~**

**PreparedStatement接口继承了Statement接口**

如果想取得PreparedStatement接口对象就需要用到Connection接口对象的方法：

`public PreparedStatement prepareStatement(Stirnig sql) throws SQLException`

方法名与返回类型是不一样的。。。

PreparedStatement接口对象的操作数据的方法也是不一样的：并没有Sql参数，在获取PreparedStatement对象时就输入了sql，但是在这个sql中是需要通过占位符“ ？”来进行描述的，通过编号定为占位符位置，从 1 开始。

更新操作：`public int executeUpdate() throws SQLException`

查询操作：`public ResultSet executeQuery() throws SQLException`

填充数据：`public void setXxx(int inidex, 数据类型 变量)` index为占位符编号。

值得注意的是setDate方法接收的是javba.sql.Date对象，它接收一个long类型的参数，我们想用java.util.Date对象就需要用到util.Date对象的getTime()方法接收返回一个long类型的util.Date。

**更新范例：**

```java
	public static void main(String[] args) throws Exception {
		Class.forName(DBDRIVER);
		Connection con = DriverManager.getConnection(URL, DBUSER, PASSWORD);
		int age = 189;
		String name = "阿西";
		Date date = new Date(); //这个date需要时sql.Date类
		String note = "wwwww";
		String sql = "insert into member (name, age, birthday, note)values(?,?,?,?)";
		PreparedStatement state = con.prepareStatement(sql);
		state.setString(1, name);
		state.setInt(2, age);
		state.setDate(3, new java.sql.Date(date.getTime())); //util.Date.getTime可以将Date转换为long
		state.setString(4, note);
        //executeUpdate不需要再添加sql参数
		System.out.println("数据库更新行数：" + state.executeUpdate());
		con.close();
	}
//输出
数据库更新行数：1
```

##### PreparedStatement接口查询（核心）

```java
	public static void main(String[] args) throws Exception {
		Class.forName(DBDRIVER);
		Connection con = DriverManager.getConnection(URL, DBUSER, PASSWORD);
		String sql = "select mid,name, age, birthday, note from member";
		PreparedStatement state = con.prepareStatement(sql);
		ResultSet rs = state.executeQuery();
		while(rs.next()) {
		System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getInt(3) + " " + rs.getDate(4) + " " + rs.getString(5));;
		}
		con.close();
	}
```

在查询sql中也可以使用占位符 ？

```java
String sql = "select mid,name, age, birthday, note from member where mid=?";
PreparedStatement state = con.prepareStatement(sql);
state.setInt(1,2); //1为占位符索引， 2为值
```

**模糊查询：**

```java
		String colunm = "name";
		String nameStart = "杨";
		String sql = "select mid,name, age, birthday, note from member where " + colunm + " like ?"; //sql拼接之间记得添加空格
		PreparedStatement state = con.prepareStatement(sql);
		state.setString(1, nameStart + "%"); //设置占位符
		ResultSet rs = state.executeQuery();
		while(rs.next()) {
		System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getInt(3) + " " + rs.getDate(4) + " " + rs.getString(5));;
		}
//输出
1 杨优秀 23 1997-11-16 哈哈哈哈
2 杨不优秀 20 1997-11-16 草
```

**COUNT查询：**返回模糊查询的记录条数

```java
		String colunm = "name";
		String nameStart = "杨";
		String sql = "select count(*) from member where " + colunm + " like ?";
		PreparedStatement state = con.prepareStatement(sql);
		state.setString(1, nameStart + "%");
		ResultSet rs = state.executeQuery();
		if(rs.next()) {
		System.out.println(rs.getInt(1)); //数据量大时可以换成getLong
		}
		con.close();
//输出
2
```

以上就是所有的最基础的查询与更新操作。