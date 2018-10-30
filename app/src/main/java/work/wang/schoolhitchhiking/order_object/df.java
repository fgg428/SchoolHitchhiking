package work.wang.schoolhitchhiking.order_object;

import java.io.Serializable;

public class df implements Serializable {
    int foodID;
    String foodType;
    String foodLocationType;
    String foodDate;
    String buyerName;
    String buyerPhone;
    String buyerLocation;
    String statement;
    int pay;
    int tasked;
    int userID;

    public String getBuyerPhone() {
        return buyerPhone;
    }

    public void setBuyerPhone(String buyerPhone) {
        this.buyerPhone = buyerPhone;
    }

    public int getFoodID() {
        return foodID;
    }

    public void setFoodID(int foodID) {
        this.foodID = foodID;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public String getFoodLocationType() {
        return foodLocationType;
    }

    public void setFoodLocationType(String foodLocationType) {
        this.foodLocationType = foodLocationType;
    }

    public String getFoodDate() {
        return foodDate;
    }

    public void setFoodDate(String foodDate) {
        this.foodDate = foodDate;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerLocation() {
        return buyerLocation;
    }

    public void setBuyerLocation(String buyerLocation) {
        this.buyerLocation = buyerLocation;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public int getPay() {
        return pay;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }

    public int getTasked() {
        return tasked;
    }

    public void setTasked(int tasked) {
        this.tasked = tasked;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
