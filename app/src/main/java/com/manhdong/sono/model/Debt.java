package com.manhdong.sono.model;

import java.io.Serializable;

/**
 * Created by Saphiro on 6/22/2016.
 */
public class Debt implements Serializable{

    String person;
    String debtType;
    Double amount;
    String currency;
    String reason;
    String expDate;
    String startDate;
    int columnID;
    String debtStatus;

    public String getDebtStatus() {
        return debtStatus;
    }

    public void setDebtStatus(String debtStatus) {
        this.debtStatus = debtStatus;
    }

    public int getColumnID() {
        return columnID;
    }

    public void setColumnID(int columnID) {
        this.columnID = columnID;
    }

    public Debt() {
        person = "";
        debtType = "";
        currency = "";
        amount = 0.0;
        reason = "";
        expDate ="";
        startDate ="";
        debtStatus = "UNRESOLVED";

    }


    public Debt(Double amount, String debtType, String expDate, String person, String reason, String startDate, String currency) {
        this.amount = amount;
        this.debtType = debtType;
        this.expDate = expDate;
        this.person = person;
        this.reason = reason;
        this.startDate = startDate;
        this.currency = currency;

    }

    @Override
    public String toString() {
        String debtText = getPerson() + " | " + getReason() +" | "
                + getAmount() + " | " + getDebtType() + " | " +
                getCurrency()+ " | " + getExpDate() + getStartDate();
        return debtText.toUpperCase();
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDebtType() {
        return debtType;
    }

    public void setDebtType(String debtType) {
        this.debtType = debtType;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
