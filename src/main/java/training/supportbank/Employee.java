package training.supportbank;
import java.math.BigDecimal;

public class Employee {
    private String name;
    private BigDecimal totalSent = new BigDecimal(0);
    private BigDecimal totalReceived = new BigDecimal(0);

    public Employee(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setName(String newName){
        name = newName;
    }

    public void incrementTotalSent(BigDecimal amount){ totalSent = totalSent.add(amount); }

    public void incrementTotalReceived(BigDecimal amount){
        totalReceived = totalReceived.add(amount);
    }

    public BigDecimal getBalance() { return totalReceived.subtract(totalSent); }

}
