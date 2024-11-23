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









