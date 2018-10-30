package work.wang.schoolhitchhiking.taskFragment;

import java.io.Serializable;

public class tasking implements Serializable {
    String type;
    String date;
    int money;
    int tblID;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getTblID() {
        return tblID;
    }

    public void setTblID(int tblID) {
        this.tblID = tblID;
    }
}
