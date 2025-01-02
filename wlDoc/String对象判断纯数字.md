在 Java 中，可以通过以下几种方式校验一个字符串是否只包含数字：

### **方法 1：使用正则表达式**

使用正则表达式 `\\d+` 来检查字符串是否只包含数字：

```
java复制代码public static boolean isNumeric(String str) {
    return str != null && str.matches("\\d+");
}
```

- `\\d` 表示数字字符。
- `+` 表示一个或多个连续的数字。
- 如果 `str` 是 `null`，则直接返回 `false`。

------

### **方法 2：使用 `Character.isDigit`**

遍历字符串中的每个字符，并检查是否是数字：

```
java复制代码public static boolean isNumeric(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }
    for (char c : str.toCharArray()) {
        if (!Character.isDigit(c)) {
            return false;
        }
    }
    return true;
}
```

------

### **方法 3：使用 `Apache Commons Lang` 的工具类**

如果你可以使用第三方库（例如 Apache Commons Lang），可以使用 `StringUtils.isNumeric`：

```
java复制代码import org.apache.commons.lang3.StringUtils;

public static boolean isNumeric(String str) {
    return StringUtils.isNumeric(str);
}
```

`StringUtils.isNumeric` 会返回 `true` 如果字符串只包含数字并且不为空。

------

### **方法 4：使用 `try-catch`**

尝试将字符串转换为数字，如果抛出异常，则说明字符串不是数字。

```
java复制代码public static boolean isNumeric(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }
    try {
        Long.parseLong(str); // 可以用 Integer.parseInt 或 Double.parseDouble 根据需要
        return true;
    } catch (NumberFormatException e) {
        return false;
    }
}
```

------

### **对比与适用场景**

| 方法                    | 优点                       | 缺点                       | 使用场景                   |
| ----------------------- | -------------------------- | -------------------------- | -------------------------- |
| **正则表达式**          | 简洁、清晰                 | 性能可能比循环略差         | 检查是否只包含数字         |
| **`Character.isDigit`** | 性能较优，无需依赖第三方库 | 代码略长                   | 长字符串或性能敏感的场景   |
| **`StringUtils`**       | 简洁、易读                 | 需引入外部依赖             | 已使用 Apache Commons Lang |
| **`try-catch`**         | 简单，适合数字解析的场景   | 不适合单纯校验是否包含数字 | 需要同时校验并解析字符串   |

选择方法时，根据性能要求、代码简洁性或是否需要依赖库来选择合适的实现。



### **方法 1：结合 `instanceof` 和正则表达式**

将 `Object` 转换为 `String`，然后校验：

```
java复制代码public static boolean isNumeric(Object obj) {
    if (obj instanceof String) {
        String str = (String) obj;
        return str.matches("\\d+");
    }
    return false;
}
```

- 如果 `Object` 不是 `String` 类型，直接返回 `false`。
- 如果是 `String` 类型，使用正则表达式校验其内容。

------

### **方法 2：尝试解析为数字**

如果输入的 `Object` 是数字类型或字符串数字，可以尝试解析为数字：

```
java复制代码public static boolean isNumeric(Object obj) {
    if (obj == null) {
        return false;
    }
    try {
        new BigDecimal(obj.toString());
        return true;
    } catch (NumberFormatException e) {
        return false;
    }
}
```

- 使用 `BigDecimal` 的解析能力，可以支持更广泛的数字格式（如科学计数法）。
- 如果解析失败，说明不是数字。

------

### **方法 3：结合 `Character.isDigit`**

当 `Object` 是 `String` 时，可以遍历字符逐个判断：

```
java复制代码public static boolean isNumeric(Object obj) {
    if (obj instanceof String) {
        String str = (String) obj;
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
    return false;
}
```

- 针对 `String` 类型对象，逐个检查每个字符是否是数字。
- 非 `String` 类型直接返回 `false`。

------

### **方法 4：使用 Apache Commons Lang 工具类**

如果项目中已经引入 Apache Commons Lang，可以使用 `StringUtils`:

```
java复制代码import org.apache.commons.lang3.StringUtils;

public static boolean isNumeric(Object obj) {
    return obj instanceof String && StringUtils.isNumeric((String) obj);
}
```

`StringUtils.isNumeric` 会返回 `true` 如果字符串只包含数字且不为空。

------

### **方法 5：支持多种数字类型的校验**

校验 `Object` 是否是某种数字类型（如 `Integer`、`Double` 等）或可转为数字的字符串：

```
java复制代码public static boolean isNumeric(Object obj) {
    if (obj == null) {
        return false;
    }
    if (obj instanceof Number) {
        return true; // 如果已经是数字类型，直接返回 true
    }
    if (obj instanceof String) {
        try {
            Double.parseDouble((String) obj); // 或 Long.parseLong, Integer.parseInt
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    return false;
}
```

------

### **总结**

| 方法                        | 优点                                   | 缺点                 | 使用场景                     |
| --------------------------- | -------------------------------------- | -------------------- | ---------------------------- |
| **正则表达式**              | 简单清晰，专注于 `String` 类型校验     | 仅支持纯数字的字符串 | 校验 `String` 对象是否纯数字 |
| **`BigDecimal` 或 `parse`** | 支持更广泛的数字格式（如科学计数法）   | 性能稍差             | 校验是否为数字               |
| **`Character.isDigit`**     | 性能较好，避免正则表达式开销           | 代码略显冗长         | 校验纯数字字符串             |
| **`StringUtils` 工具类**    | 简洁、易读，复用社区工具               | 需引入第三方库       | 已使用 Apache Commons Lang   |
| **支持多种类型**            | 更通用，支持数字类型和数字字符串的校验 | 实现逻辑略复杂       | 校验多种数字类型             |

可以根据具体需求选择合适的实现方式，比如只校验 `String` 或处理更通用的 `Object` 输入场景。































