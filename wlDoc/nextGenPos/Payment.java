// 大概所有类都置于命名如下的包中：
package ruoyi.test.wl.nextGenPos;

public class Payment {
    private Money amount;

    public Payment(Money cashTendered) {
        amount = cashTendered;
    }

    public Money getAmount() {
        return amount;
    }
}