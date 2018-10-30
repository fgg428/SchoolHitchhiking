package work.wang.schoolhitchhiking.bill;

public class bill {

//    billID int not null auto_increment PRIMARY KEY COMMENT 'billID',
//    number int not NULL,
//    billType VARCHAR(20) not null,
//    billDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
//    type int not null,
//    userID int not null,
    private int billID;
    private int number;
    private String billType;
    private String billDate;
    private int type; //定义订单的正负 0为正 1为负
    int userID;

    public int getBillID() {
        return billID;
    }

    public void setBillID(int billID) {
        this.billID = billID;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
