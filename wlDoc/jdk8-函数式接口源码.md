#### 1、函数式接口

1、定义函数式接口（Function Interface）：一个有且仅有一个抽象方法，多个非抽象方法的接口（default方法）。函数式接口可以被隐式转换为lambda表达式。

2、语法定义了一个函数式接口：

```java
@FunctionalInterface
interface GreetingService {
    void sayMessage(String message);
}
```

使用Lambda表达式来表示该接口的一个实现（注：java8之前一般都是用匿名类实现）：

```java
GreetingService greetService1 = message -> System.out.println("Hello " + message);
```

3、Lambdas及函数式接口例子

| 介绍                                        | Lambda示例                                                   | 对应的函数式接口                                             |
| ------------------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 布尔表达式：接受一个入参，返回boolean       | (List<String> list) ->list.isEmpty()                         | **Predicate**<List<String>>                                  |
| 创建对象：无入参，有返回                    | () -> new Apple(10)                                          | **Supplier**<Apple>                                          |
| 消费一个对象：接受一个入参，无返回          | (Apple a) -> System.out.printIn(a.getWeight())               | **Consumer**<Apple>                                          |
| 从一个对象中选择/提取：接受一个入参，有返回 | (String s) -> s.length()                                     | **Function**<String, Integer>或ToIntFunction<String>         |
| 合并两个值                                  | (int a, int b) -> a*b                                        | **IntBinaryOperator**                                        |
| 比较两个值                                  | (Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()) | Comparator<Apple>或BiFunction<Apple, Apple, Integer>或ToIntBiFunction<Apple, Apple> |

示例：

Consumer

```java
public void test1() {
    //未使用Lambda表达式
    Learn("java", new Consumer<String>() {
        @Override
        public void accept(String s) {
            System.out.println("学习什么？ " + s);
        }
    });
    System.out.println("====================");
    //使用Lambda表达
    Learn("html", s -> System.out.println("学习什么？ " + s));
}

private void Learn(String s, Consumer<String> stringConsumer) {
    stringConsumer.accept(s);
}
```

​	

Function

```java
public void test3() {
    //使用Lambda表达式
    Employee employee = new Employee(1001, "Tom", 45, 10000);
    Function<Employee, String> func1 = e -> e.getName();
    System.out.println(func1.apply(employee));
    System.out.println("====================");

    //使用方法引用
    Function<Employee,String> func2 = Employee::getName;
    System.out.println(func2.apply(employee));
}

@Data
@AllArgsConstructor
public class Employee{
    private int id;
    private String name;
    private int age;
    private int salary;
}
```

```java
//使用匿名内部类
Function<Double, Long> func = new Function<Double, Long>() {
    @Override
    public Long apply(Double aDouble) {
        return Math.round(aDouble);
    }
};
System.out.println(func.apply(10.5));
System.out.println("====================");

//使用Lambda表达式
Function<Double, Long> func1 = d -> Math.round(d);
System.out.println(func1.apply(12.3));
System.out.println("====================");

//使用方法引用
Function<Double,Long>func2 = Math::round;
System.out.println(func2.apply(12.6));
```

Supplier

```java
public void test2() {
    //未使用Lambda表达式
    Supplier<String> sp = new Supplier<String>() {
        @Override
        public String get() {
            return new String("我能提供东西");
        }
    };
    System.out.println(sp.get());

    //使用Lambda表达式
    Supplier<String> sp1 = () -> new String("我能通过lambda提供东西");
    System.out.println(sp1.get());
```

#### 2、源码

##### 2.1 Function

```java
package java.util.function;

import java.util.Objects;

@FunctionalInterface
public interface Function<T, R> {
    
    R apply(T t);
   
    default <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }

    default <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }

    static <T> Function<T, T> identity() {
        return t -> t;
    }
}

```

###### 2.1.1 andThen方法

数据流水线

```java
package lambdasinaction.wl._20250104;

import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;

public class FunctionExample {
    public static void main(String[] args) {

        // if(Objects.isNull("等于某种情况"){

        // }
        Function<String, String> targetDeal = paramNotNull().andThen(paramNotPositiveInteger()).andThen(paramFormat());
        System.out.println(targetDeal.apply(null));

    }

    public static Function<String, String> paramNotNull() {
        return (String param) -> {
            if (StringUtils.isEmpty(param)) {
                throw new IllegalArgumentException("参数不能为空");
            }
            return param;
        };
    }

    public static Function<String, String> paramNotPositiveInteger() {
        return (String param) -> {
            if (!StringUtils.isNumeric(param)) {
                throw new IllegalArgumentException("参数不是正整数");
            }
            return param;
        };
    }

    public static Function<String, String> paramFormat() {
        // 0012 -> 12 处理
        return (String param) -> {
            long paramLong = Long.parseLong(param);
            return String.valueOf(paramLong);
        };
    }
}

```



















