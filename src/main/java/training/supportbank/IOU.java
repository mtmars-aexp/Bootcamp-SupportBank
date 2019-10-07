package training.supportbank;
import java.time.LocalDate;
import java.util.Date;
import java.math.*;
import java.time.format.*;

public class IOU {
    private String date;
    private String senderName;
    private String recipientName;
    private String memo;
    private BigDecimal amount;


    public IOU(String date, String senderName, String recipientName, String memo, BigDecimal amount){
        this.date = date;
        this.senderName = senderName;
        this.recipientName = recipientName;
        this.memo = memo;
        this.amount = amount;
    }

    public String getSenderName(){
        return senderName;
    }

    public String getRecipientName(){ return recipientName; }

    public String getDate(){ return date; }

    public String getMemo(){
        return memo;
    }

    public BigDecimal getAmount(){
        return amount;
    }

}
