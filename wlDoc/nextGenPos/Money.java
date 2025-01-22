package ruoyi.test.wl.nextGenPos;

public class Money {
    private double amount;

    public Money() {
        this.amount = 0.0;
    }

    public Money(double amount) {
        this.amount = amount;
    }

    public Money add(Money other) {
        this.amount += other.amount;
        return this;
    }

    public Money minus(Money other) {
        this.amount -= other.amount;
        return this;
    }

    public Money times(int multiplier) {
        this.amount *= multiplier;
        return this;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return String.format("%.2f", amount);
    }
}
