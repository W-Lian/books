#### 

```java
package lambdasinaction.wl._20250104;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class MapOperate {

    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);

        // 1. 获取键的Stream
        Stream<String> keyStream = map.keySet().stream();
        keyStream.forEach(key -> System.out.println("Key: " + key));

        // 2. 获取值的Stream
        Stream<Integer> valueStream = map.values().stream();
        valueStream.forEach(value -> System.out.println("Value: " + value));

        // 3. 获取键值对(Entry)的Stream
        Stream<Map.Entry<String, Integer>> entryStream = map.entrySet().stream();
        entryStream.forEach(entry -> 
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue())
        );

        // 4. 使用Stream处理Map的示例
        map.entrySet().stream()
           .filter(entry -> entry.getValue() > 1)  // 过滤值大于1的条目
           .map(Map.Entry::getKey)                 // 获取键
           .forEach(System.out::println);          // 打印结果
    }
}

```

