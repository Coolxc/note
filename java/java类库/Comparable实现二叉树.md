```java
package cn.yang.demo;

import java.util.Arrays;

class BinaryTree{ //中序有序的递增二叉树
	private class Node{
		@SuppressWarnings("rawtypes") //压制Comparable<>不加泛型。不能Comparable<?>，加了问号是可以
		//接收任意类型，但不能向里面写东西(会出现类型混乱)。只能读。
		private Comparable data; //保存的操作数据，因为必须是Comparable的子类，要比较大小
		private Node left;
		private Node right;
		@SuppressWarnings("unused")
		public Node(Comparable data) { //传入一个可比较对象
			this.data = data;
		}
		public void addNode(Node newNode) { //添加节点（从根节点）
			if (this.data.compareTo(newNode.data) > 0) { //如果比current节点小则进入判断
				if (this.left == null) {  //左节点为空那么直接插入。
					this.left = newNode;
				}else {  //否则一直向左找直到null或比current节点大则进入下一个else(右子树)
					this.left.addNode(newNode);
				}
			}else {  //右子树
				if (this.right == null) {  //右子树为空则直接插入
					this.right = newNode;
				}else {   //否则向右子树下一个节点找合适位置。回到判断的start，还是先看左子树
					this.right.addNode(newNode);
				}
			}
			
		}
		public void toArrayNode() {   //二叉树转换为数组
			if (this.left != null) {  //如果左子树不为空则向左递归，找到最左侧节点
				this.left.toArrayNode();
			}
			BinaryTree.this.retData[BinaryTree.this.foot ++] = this.data; //从最左节点开始依次将节点放入数组
			if (this.right != null) { //中序遍历方式。 最后处理右子树。这样可以得到有序的数组
				this.right.toArrayNode();
			}
		}
	}
	private Node root;  //根节点
	private int count;  //节点数量
	private int foot = 0;  //保存输出数组的脚标
	private Object [] retData; //保存输出数组
	public Object [] toArray() {
		this.foot = 0;
		this.retData = new Object [this.count];
		this.root.toArrayNode();
		return this.retData;
	}
	public void add(Object data) {
		if (data == null) {
			return ;
		}
		Node newNode = new Node((Comparable) data);
		if (this.root == null) {
			this.root = newNode;
		}else {
			this.root.addNode(newNode); //从根节点开始判断插入位置
		}
		this.count++;
	}
}
//节点类型我们传入Perosn对象
class Person implements Comparable<Person>{   //comparable泛型指的是想要哪种类型相比较
	private String name;
	private int age;
	public Person(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}
	@Override
	public String toString() {
		return "Person [name=" + name + ", age=" + age + "]";
	}
	@Override
	public int compareTo(Person o) { //根据年龄比较大小
		if (this.age > o.age) {
			return 1;
		}else if(this.age == o.age){
			return 0;
		}else {
			return -1;
		}
	}
}

public class TestDemo {
	public static void main(String[] args) throws Exception{
		BinaryTree bt = new BinaryTree();
		bt.add(new Person("杨优秀", 20));
		bt.add(new Person("李", 19));
		bt.add(new Person("方", 21));
		System.out.println(Arrays.deepToString(bt.toArray()));
	}
}
//输出
[Person [name=李, age=19], Person [name=杨优秀, age=20], Person [name=方, age=21]]
```

