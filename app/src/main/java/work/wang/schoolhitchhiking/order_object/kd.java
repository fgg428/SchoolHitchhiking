package work.wang.schoolhitchhiking.order_object;

import java.io.Serializable;

public class kd implements Serializable {
    int sendID;
    String locationType;
    String sendCompany;
    String contactPerson;
    String phone;
    String goodsType;
    int goodsWeight;
    String sendDate;
    String sendLocation;
    String state;
    int pay;
    int tasked;
    int userID;

    public int getGoodsWeight() {
        return goodsWeight;
    }

    public void setGoodsWeight(int goodsWeight) {
        this.goodsWeight = goodsWeight;
    }

    public int getSendID() {
        return sendID;
    }

    public void setSendID(int sendID) {
        this.sendID = sendID;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getSendCompany() {
        return sendCompany;
    }

    public void setSendCompany(String sendCompany) {
        this.sendCompany = sendCompany;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }


    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getSendLocation() {
        return sendLocation;
    }

    public void setSendLocation(String sendLocation) {
        this.sendLocation = sendLocation;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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
