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

java核心技术的作者，希望通过`getClass()`去解决，目测jdk源码的开发者只是使用`instanceof`去判断

##### 1.4 相等测试与继承（java核心技术作者的想法）

###### 1.4.1 背景

Java语言规范要求equals方法具有下面的特性：
1）自反性：对于任何非空引用x, x.equals(x)应该返回true。
2）对称性：对于任何引用x和y，当且仅当y.equals(x)返回true, x.equals(y)也应该返回true。
3）传递性：对于任何引用x、y和z，如果x.equals(y)返回true, y.equals(z)返回true,x.equals(z)也应该返回true。
4）一致性：如果x和y引用的对象没有发生变化，反复调用x.equals(y)应该返回同样的结果。
5）对于任意非空引用x, x.equals(null)应该返回false。
这些规则，避免了类库实现者在数据结构中定位一个元素时还要考虑调用x.equals(y)，还是调用y.equals(x)的问题。

###### 1.4.2 观念

● 如果子类能够拥有自己的相等概念，则对称性需求将强制采用getClass进行检测。
● 如果由超类决定相等的概念，那么就可以使用instanceof进行检测，这样可以在不同子类的对象之间进行相等的比较。

在雇员和经理的例子中，只要对应的域相等，就认为两个对象相等。如果两个Manager对象所对应的姓名、薪水和雇佣日期均相等，而奖金不相等，就认为它们是不相同的，因此，可以使用getClass检测。

只需要考虑多态的情况，因为`instanceof`不能识别这一种情况
1）父类、子类的equals方法：使用`getClass`方法和（父子类）类属性值一致，保持一套规则，保证对称性
2）子类都使用父类的equals方法：`instanceof`方法和父类属性值一致

```java
// 任何的子类都调用自己中的equals方法，保持相等的规则由子类定义，保持对称性
// 父类
public class EmployeeAuthor {
    private String name;
    
    private int age;
    
    public EmployeeAuthor(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public EmployeeAuthor() {
        
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null) return false;
        if(getClass() == o.getClass()) {
            EmployeeAuthor e = (EmployeeAuthor) o;
            return Objects.equals(name, e.name) && age == e.age;
        }else {
            return false;
        }

    }
}
```

```java
// 子类
public class ManagerAuthor extends EmployeeAuthor{
    private int bonus;
    
    public ManagerAuthor() {
        super();
    }
    public ManagerAuthor(String name, int age, int bonus) {
        super(name, age);
        this.bonus = bonus;
    }

    @Override
    public boolean equals(Object o){
        if(super.equals(o)) {
            ManagerAuthor m = (ManagerAuthor) o;
            return Objects.equals(bonus, m.bonus);
        }else {
            return false;
        }
    }
}

class Test2{
    public static void main(String[] args) {

        ManagerAuthor mm = new ManagerAuthor();
        EmployeeAuthor ee = new EmployeeAuthor();
        // false
        System.out.println(mm.equals(ee));
        // false
        System.out.println(ee.equals(mm));
    }
}
```

但是，假设使用雇员的ID作为相等的检测标准，并且这个相等的概念适用于所有的子类，就可以使用instanceof进行检测，并应该将Employee.equals声明为final。

```java
// 任何的子类都调用父类中的equals方法，保持相等的规则由父类定义
// 父类
public class Employee {
    private String name;
    
    private int age;
    
    public Employee(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Employee() {
        
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null) return false;
        if(o instanceof Employee) {
            Employee e = (Employee) o;
            return Objects.equals(name, e.name) && age == e.age;
        }else {
            return false;
        }

    }
}
```

```java
public class Manager extends Employee{
    private int bonus;
    
    public Manager() {
        super();
    }
    public Manager(String name, int age, int bonus) {
        super(name, age);
        this.bonus = bonus;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Manager) {
            if(super.equals(o)) {
                Manager m = (Manager) o;
                return Objects.equals(bonus, m.bonus);
            }else {
                return false;
            }
        }else {
            return false;
        }
    }
}

class Test{
    public static void main(String[] args) {
        Manager mm = new Manager();
        Employee ee = new Employee();
        // false
        System.out.println(mm.equals(ee));
        // true
        System.out.println(ee.equals(mm));
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

##### 3.1 黄金三问

- ###### 什么是hashCode

  散列码（hash code）是由对象导出的一个整型值。散列码是没有规律的。如果x和y是两个不同的对象，x.hashCode()与y.hashCode()基本上不会相同。下图列出了几个通过String类的hashCode方法得到的散列码。

  由于hashCode方法定义在Object类中，因此每个对象都有一个默认的散列值，其值为对象的存储地址

- ###### 为什么存在

  

- ###### 怎么用，见下文

##### 3.2 String类中的hashCode

```java
// String类散列值计算
int hash = 0;
for(int i=0; i<length(); i++) 
    hash = 31* hash + charAt(i);
```

例如：

```java
public class StringHashCode {
    public static void main(String[] args) {
        String s = "OK";
        StringBuilder sb = new StringBuilder(s);
        System.out.println(s.hashCode()+" "+ sb.hashCode());
        String t = new String("OK");
        StringBuilder tb = new StringBuilder(t);
        System.out.println(t.hashCode()+" "+ tb.hashCode());
    }
}
输出：
2524 1094834071
2524 1761061602
```

| 对象 | 散列值     |
| ---- | ---------- |
| s    | 2524       |
| sb   | 1094834071 |
| t    | 2524       |
| tb   | 1761061602 |

注意：
1）字符串s与t的散列码相同，因为字符串的散列码是由内容导出的。
2）字符串缓冲sb与tb的散列码不同，因为StringBuffer类中没定义hashCode方法，它的散列码是由Object类的默认hashCode方法导出的对象存储地址。

##### 3.3 重新定义hashCode方法

如果重新定义equals方法，就必须重新定义hashCode方法，以便用户可以将对象插入到散列表中。

hashCode方法应该返回一个整型值（也可以是负数），并合理地组合实例域的散列码，以便能够让各个不同的对象产生的散列码更加均匀。

例如：Employee类的hashCode方法

```java
public class Employee {
    public int hashCode() {
        return 7 * name.hashCode() 
            + 11 * new Double(salary).hashCode()
            + 13 * hireDay.hashCode();
    }
}

// 优化版
// 使用null安全的方法Objects.hashCode。如果其参数为null，这个方法会返回0，否则返回对参数调用hashCode的结果。
// 使用静态方法Double.hashCode来避免创建Double对象。
public class Employee {
    public int hashCode() {
        return 7 * Objects.hashCode(name) 
            + 11 * Double.hashCode(salary)
            + 13 * Objects.hashCode(hireDay);
    }
}

// 使用Objects.hash并提供多个参数。
// 这个方法会对各个参数调用Objects.hasCode，并组合这些散列值。
public class Employee {
    public int hashCode() {
        return Objects.hasCode(name, salary, hireDay);
    }
}
```

Equals与hashCode的定义必须一致：**如果x.equals(y)返回true，那么x.hashCode( )就必须与y.hashCode( )具有相同的值**。例如，如果用定义的Employee.equals比较雇员的ID，那么hashCode方法就需要散列ID，而不是雇员的姓名或存储地址。

如果**存在数组类型的域，那么可以使用静态的Arrays.hashCode方法计算一个散列码**，这个散列码由数组元素的散列码组成。

#### 4、toString方法

##### 4.1 黄金三问

- 什么是toString方法

  用于返回表示对象值的字符串

- 为什么存在toString方法

  方便日志打印

- 怎么用，见下文

##### 4.2 打印的格式

绝大多数（但不是全部）的toString方法都遵循这样的格式：**类的名字，随后是一对方括号括起来的域值**。

Point类的toString方法，`java.awt.Point[x=10,y=20]`

例如：

```java
// Employee类
public String toString() {
    return getClass().getName() 
        + "[name=" + name
        + ",salary=" + salary
        + ",hireDay=" + hireDay
        + "]";
}

// Manager类
public class Manager extends Employee {
    public String toString() {
        return super.toString()
            + "[bonus=" + bonus
            + "]";
    }
}
输出：
    Manager[name=...,salary=...,hireDay=...][bonus=...]
```

##### 4.3 随处可见的toString()

**只要对象与一个字符串通过操作符“+”连接起来，Java编译就会自动地调用toString方法**，以便获得这个对象的字符串描述。例如，

```java
Point p = new Point(10, 20);
// 自动调用p.toString()
String message = "The current position is " + p; 
```

在调用x.toString()的地方可以用`""+x`替代。这条语句将一个空串与x的字符串表示相连接。这里x就是x.toString()。

与toString()不同的是，如果x是基本类型，这条语句照样能够执行。

##### 4.4 Object中的toString()

```java
System.out.println(System.out)
输出：
    java.io.PrintStream@2f6684
// 之所以得到这样的结果是因为PrintStream类没有覆盖toString方法    
```

##### 4.5 数组中的toString()

数组继承了object类的toString方法，数组类型将按照旧的格式打印。

例如：

```java
int[] luckyNumbers = {2,3,5,7,11,13};
String s = "" + luckyNumbers;
// 生成字符串"[l@1a46e30"（前缀[l表明是一个整型数组）。修正的方式是调用静态方法Arrays.toString。
String s = Arrays.toString(luckyNumbers);
// 生成字符串"[2,3,5,7,11,13]"。
```

想要打印多维数组（即，数组的数组）则需要调用Arrays.deepToString方法。











