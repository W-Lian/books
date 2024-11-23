#### 1、equals方法

##### 1.1 什么是equals方法

通过方法中的规则，来判断两个引用的对象是否相等
（Object类中的equals方法用于检测一个对象是否等于另外一个对象。在Object类中，这个方法将判断两个对象是否具有相同的引用。如果两个对象具有相同的引用，它们一定是相等的。）

##### 1.2 为什么要用



##### 1.3 怎么用

###### 1.3.1 ==

1）基本数据类型

```java
public class BaseDataType {
    public static void main(String[] args) {
        int a = 1;
        int b = 2;
        // false
        System.out.println(a==b);
    }
}
```

2）引用类型

```java
public class Employee {
    private String name;
    
    private int age;
}

public class Manager extends Employee{
    private int bonus;
}

class Test{
    public static void main(String[] args) {
        Manager m = new Manager();
        Manager m2 = new Manager();
        Manager m3 = m;
        Employee m4 = m;

        // true
        System.out.println(m == m3 );
        // false
        System.out.println(m == m2 );
        // true (多态的情况，指向子类对象)
        System.out.println(m == m4 );
    }
}
```

###### 1.3.2 instanceof方法

1） 解释：
`m instanceof Employee`
判断`m引用指向的对象`与`Employee`是否相等

2）问题：
该方法在多态的情况下，无法区分父子类

```java
public class Employee {
    private String name;
    
    private int age;
}

public class Manager extends Employee{
    private int bonus;
}

class Test{
    public static void main(String[] args) {
        Manager m = new Manager();
        Employee e = new Employee();
        Employee e2 = new Manager();

        // true (该方法在多态的情况下，无法区分父子类)
        System.out.println(m instanceof Employee);
        // false
        System.out.println(e instanceof Manager);
        // true 
        System.out.println(e2 instanceof Manager);
    }
}
```

#### 2、jdk中部分源码的equals方法

##### 2.1 Object中的equals方法

```java
public boolean equals(Object obj) {
    return (this == obj);
}
```

##### 2.2 String中的equals方法

```java
public boolean equals(Object anObject) {
    if (this == anObject) {
        return true;
    }
    return (anObject instanceof String aString)
            && (!COMPACT_STRINGS || this.coder == aString.coder)
            && StringLatin1.equals(value, aString.value);
}
```

##### 2.3 Date、Timestamp的equals方法

```java
// java.util.Date 父类
public boolean equals(Object obj) {
    return obj instanceof Date && getTime() == ((Date) obj).getTime();
}
```

```java
// java.sql.Timestamp 子类重写
public boolean equals(java.lang.Object ts) {
    if (ts instanceof Timestamp) {
    	return this.equals((Timestamp)ts);
    } else {
    	return false;
    }
}
// 子类的equals方法
public boolean equals(Timestamp ts) {
    if (super.equals(ts)) {
        if  (nanos == ts.nanos) {
            return true;
        } else {
            return false;
        }
    } else {
        return false;
    }
}
```

##### 2.4 AbstractSet类中的equals方法

```java
public boolean equals(Object o) {
    if (o == this)
        return true;

    if (!(o instanceof Set))
        return false;
    Collection<?> c = (Collection<?>) o;
    if (c.size() != size())
        return false;
    try {
        return containsAll(c);
    } catch (ClassCastException | NullPointerException unused) {
        return false;
    }
}
```











#### 3、hashCode方法

super关键字：
super不是一个对象的引用，不能将super赋给另一个对象变量，它只是一个指示编译器调用超类方法的特殊关键字。

重写的方法参数类型、个数，方法名要父类相同

泛型中的方法重写

```java
class Employee<T> {
    private T name;
    
    public T getName(){
        return name;
    }
    
    public void setName(T name){
        this.name = name;
    }
}

class Manager<T> extends Employee<T>{
    private Integer id;
    
    public Integer getId(){
        return id;
    }
    
    public void setId(Integer id){
        this.id = id;
    }

    @Override
    public void setName(T name) {
        System.out.println("Manager.setName");
    }
}
```



#### 4、toString方法









