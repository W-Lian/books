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



###### 2.1.2 compose

```java
default <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
    Objects.requireNonNull(before); // 确保传入的 before 函数不为 null
    return (V v) -> apply(before.apply(v)); // 先执行 before.apply(v)，再执行 apply(...)
}

```

**参数**：

- `before`：一个 `Function`，其输出类型 `T` 必须与当前函数的输入类型兼容。

**返回值**：

- 一个新的 

  ```
  Function
  ```

  ，其执行顺序是：

  1. 先调用 `before.apply(v)`。
  2. 将其结果传递给当前函数的 `apply(...)` 方法。

**适用场景**：

- 用于定义执行顺序，从后向前组合函数。
- 数据预处理逻辑。

```java
import java.util.function.Function;

public class ComposeExample {
    public static void main(String[] args) {
        // 定义两个函数
        Function<String, Integer> parseInt = Integer::parseInt; // 将字符串转换为整数
        Function<Integer, Integer> square = x -> x * x;         // 计算平方

        // 使用 compose 方法组合
        Function<String, Integer> composedFunction = square.compose(parseInt);

        // 执行组合函数
        System.out.println(composedFunction.apply("4")); // 输出: 16
    }
}

```

**解释**：

1. `parseInt`：将字符串转换为整数。
2. `square`：计算整数的平方。
3. 通过 `compose`，`parseInt` 的结果传递给 `square`。

示例 2：数据预处理	

```java
import java.util.function.Function;

public class DataProcessing {
    public static void main(String[] args) {
        // 定义两个函数
        Function<String, String> trim = String::trim;         // 去掉两端空格
        Function<String, Integer> getLength = String::length; // 计算字符串长度

        // 使用 compose 方法组合
        Function<String, Integer> pipeline = getLength.compose(trim);

        // 执行组合函数
        System.out.println(pipeline.apply("   hello world   ")); // 输出: 11
    }
}

```

**解释**：

1. `trim`：去掉字符串两端的空格。
2. `getLength`：计算字符串长度。
3. 通过 `compose`，`trim` 的结果传递给 `getLength`。

示例 3：处理用户输入

```java
import java.util.function.Function;

public class UserInputProcessing {
    public static void main(String[] args) {
        // 定义两个函数
        Function<String, String> toLowerCase = String::toLowerCase;  // 转换为小写
        Function<String, String> replaceSpaces = s -> s.replace(" ", "_"); // 替换空格为下划线

        // 使用 compose 方法组合
        Function<String, String> processInput = replaceSpaces.compose(toLowerCase);

        // 执行组合函数
        System.out.println(processInput.apply("Hello World")); // 输出: hello_world
    }
}

```

**解释**：

1. `toLowerCase`：将字符串转换为小写。
2. `replaceSpaces`：将字符串中的空格替换为下划线。
3. 通过 `compose`，`toLowerCase` 的结果传递给 `replaceSpaces`。

### **与 `andThen` 的对比**

| 特性         | `compose`                       | `andThen`                      |
| ------------ | ------------------------------- | ------------------------------ |
| 执行顺序     | 从后往前：`before -> current`   | 从前往后：`current -> after`   |
| 用法         | 数据预处理                      | 数据后续处理                   |
| 示例组合顺序 | `before.apply(v) -> apply(...)` | `apply(t) -> after.apply(...)` |

**选择依据**：

- 使用 `compose` 时，先对数据执行预处理，再调用当前函数。
- 使用 `andThen` 时，先调用当前函数，再对结果执行后续处理。

### **注意事项**

1. **输入输出类型**：
   - `before` 的输出类型必须与当前函数的输入类型兼容。
   - 如果类型不兼容，会在编译时报错。
2. **空值检查**：
   - `compose` 会显式调用 `Objects.requireNonNull(before)`，传入的 `before` 函数不能为 `null`。
3. **函数顺序**：
   - 顺序很重要：`compose` 是从后往前，`andThen` 是从前往后。



### **实际应用场景**

1. **数据预处理管道**：
   - `compose` 常用于数据预处理逻辑。
2. **组合复杂逻辑**：
   - 用于将多个步骤组合成一个函数，减少重复代码。
3. **动态组合函数**：
   - 使用 `compose` 组合逻辑，可以根据场景动态调整步骤顺序。

**总结**： `compose` 是函数式编程的重要工具，适用于需要先执行某些预处理逻辑的场景。它与 `andThen` 互补，提供了灵活的函数组合方式。













