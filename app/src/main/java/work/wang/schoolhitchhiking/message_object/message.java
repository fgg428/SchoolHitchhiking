package work.wang.schoolhitchhiking.message_object;

import java.io.Serializable;

public class message implements Serializable {

//    acceptID int not null auto_increment PRIMARY KEY COMMENT 'acceptID',
//    userID  int not null,
//    acceptType VARCHAR(20) not null,
//    money int not null,
//    acceptDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
//    tblID int not null,
//    workID int not null,
    private int acceptID;
    private int userID;
    private String acceptType;
    private int money;
    private String acceptDate;
    private int tblID;
    private int workID;

    public int getAcceptID() {
        return acceptID;
    }

    public void setAcceptID(int acceptID) {
        this.acceptID = acceptID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getAcceptType() {
        return acceptType;
    }

    public void setAcceptType(String acceptType) {
        this.acceptType = acceptType;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getAcceptDate() {
        return acceptDate;
    }

    public void setAcceptDate(String acceptDate) {
        this.acceptDate = acceptDate;
    }

    public int getTblID() {
        return tblID;
    }

    public void setTblID(int tblID) {
        this.tblID = tblID;
    }

    public int getWorkID() {
        return workID;
    }

    public void setWorkID(int workID) {
        this.workID = workID;
    }
}
