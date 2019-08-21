package com.example.minimoneybox.Utils;

import com.example.minimoneybox.Account;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MoneyBoxJsonUtils {
    private MoneyBoxJsonUtils() {}

    public static String getBrokerToken (String stringJson) {
        String brokerToken = "";

        //check if the response is blank
        if (stringJson == null || stringJson.length() == 0) {
            return brokerToken;
        }

        try {
            JSONObject jsonResponse = new JSONObject(stringJson);
            JSONObject jsonSession = jsonResponse.getJSONObject("Session");
            brokerToken = jsonSession.getString("BearerToken");
        } catch (JSONException e) {
            LogUtils.e(e.getMessage());
            return brokerToken;
        }

        return brokerToken;
    }

    public static List<Account> getAccounts (String stringJson) {
        List<Account> accounts = new ArrayList<>();

        //check if the response is blank
        if (stringJson == null || stringJson.length() == 0) {
            return accounts;
        }

        try {
            JSONObject jsonResponse = new JSONObject(stringJson);
            JSONArray jsonProductResponses = jsonResponse.getJSONArray("ProductResponses");

            //loop through the products
            for(int i = 0; i < jsonProductResponses.length(); i++) {
                JSONObject jsonProductResponse = jsonProductResponses.getJSONObject(i);

                double planValue = jsonProductResponse.getDouble("PlanValue");
                double moneyBox = jsonProductResponse.getDouble("Moneybox");

                JSONObject jsonProduct = jsonProductResponse.getJSONObject("Product");
                String friendlyName = jsonProduct.getString("FriendlyName");
                int id = jsonProduct.getInt("Id");

                //create the account oject
                Account account = new Account(id, friendlyName, planValue, moneyBox);

                //add the quick add if available
                try {
                    JSONObject jsonPersonalisation = jsonProductResponse.getJSONObject("Personalisation");
                    JSONObject jsonQuickAddDeposit = jsonPersonalisation.getJSONObject("QuickAddDeposit");
                    account.setQuickAddAmount(jsonQuickAddDeposit.getDouble("Amount"));
                } catch (JSONException e) {

                }

                //add the account to the list
                accounts.add(account);
            }

        } catch (JSONException e) {
            LogUtils.e(e.getMessage());
            return accounts;
        }

        return accounts;
    }
}
