package com.example.minimoneybox;

import android.content.Context;

import java.io.Serializable;

public class Account implements Serializable {
    private int id;
    private String friendlyName;
    private double planValue;
    private double moneyBox;
    private double quickAddAmount;

    public Account(int id, String friendlyName, double planValue, double moneyBox) {
        this(id, friendlyName, planValue, moneyBox, 10);
    }

    public Account(int id, String friendlyName, double planValue, double moneyBox, double quickAddAmount) {
        this.id = id;
        this.friendlyName = friendlyName;
        this.planValue = planValue;
        this.moneyBox = moneyBox;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public double getPlanValue() {
        return planValue;
    }

    public void setPlanValue(double planValue) {
        this.planValue = planValue;
    }

    public double getMoneyBox() {
        return moneyBox;
    }

    public void setMoneyBox(double moneyBox) {
        this.moneyBox = moneyBox;
    }

    public double getQuickAddAmount() {
        return quickAddAmount;
    }

    public void setQuickAddAmount(double quickAddAmount) {
        this.quickAddAmount = quickAddAmount;
    }

    public String getPlanValueString(Context context) {
        return context.getString(R.string.plan_value, String.valueOf(planValue));
    }

    public String getMoneyBoxString(Context context) {
        return context.getString(R.string.moneybox, String.valueOf(moneyBox));
    }

    public String getQuickAddAmountString(Context context) {
        return context.getString(R.string.add_money_button, String.valueOf(quickAddAmount));
    }
}
