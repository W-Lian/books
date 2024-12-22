#### 1、comparable接口

##### 1.1 示例

```java
package ruoyi.test.wl.java8.javaAbstractClass;

import java.util.Arrays;

public class Employee implements Comparable<Employee> {

    private String name;
    
    private double salary;
    
    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }
    
    public String getName() {
        return name;
    }
    
    public double getSalary() {
        return salary;
    }
    
    public void raiseSalary(double byPercent) {
        double raise = salary * byPercent / 100;
        salary += raise;
    }
    
    @Override
    public int compareTo(Employee o) {
        return Double.compare(salary, o.salary);
    }
}

class EmployeeSortTest {
    public static void main(String[] args) {
        Employee[] staff = new Employee[3];
        staff[0] = new Employee("Harry Hacker", 35000);
        staff[1] = new Employee("Carl Cracker", 75000);
        staff[2] = new Employee("Tony Tester", 38000);
        Arrays.sort(staff);
        for (Employee e : staff){
            System.out.println("name=" + e.getName() + ",salary=" + e.getSalary());
        }
    }
}
```

##### 1.2 Arrays.sort(staff);源码

```java
// 使用mergesort算法对数组a中的元素进行排序。要求数组中的元素必须属于实现了Comparable接口的类，并且元素之间必须是可比较的

public static void sort(Object[] a) {
    if (LegacyMergeSort.userRequested)
        legacyMergeSort(a);
    else
        ComparableTimSort.sort(a, 0, a.length, null, 0, 0);
}
```



#### 2、comparator比较器