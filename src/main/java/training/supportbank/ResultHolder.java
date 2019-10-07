package training.supportbank;

public class ResultHolder {

    private String holderString;
    private int holderInt;
    private boolean holderBool;
    private Employee holderEmployee;


    public void setValues(String newString, int newInt, boolean newBool){
        holderString = newString;
        holderInt = newInt;
        holderBool = newBool;

    }

    public ResultHolder(String newString, int newInt, boolean newBool, Employee newEmployee){
        holderString = newString;
        holderInt = newInt;
        holderBool = newBool;
        holderEmployee = newEmployee;
    }

    public String getHolderString(){
        return holderString;
    }

    public int getHolderInt(){
        return holderInt;
    }

    public boolean getHolderBool(){
        return holderBool;
    }

    public Employee getHolderEmployee() { return holderEmployee; }
}
