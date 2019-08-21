package com.example.minimoneybox.Utils;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MoneyBoxApiUtils {
    private static final String MONEYBOX_BASE_URL = "https://api-test01.moneyboxapp.com/";

    //login
    private static final String LOGIN_URL = MONEYBOX_BASE_URL + "users/login";
    private static final String LOGIN_EMAIL_PARAM = "Email";
    private static final String LOGIN_PASSWORD_PARAM = "Password";
    private static final String LOGIN_IDFA_PARAM = "Idfa";

    //investor products
    private static final String INVESTOR_PRODUCT_URL = MONEYBOX_BASE_URL + "investorproducts";

    //one off payment
    private static final String ONE_OFF_PAYMENT_URL = MONEYBOX_BASE_URL + "oneoffpayments";
    private static final String PAYMENT_AMOUNT_PARAM = "Amount";
    private static final String PAYMENT_INVESTOR_PRODUCT_ID_PARAM = "InvestorProductId";

    //header
    private static final String APP_ID_KEY = "AppId";
    private static final String APP_ID_VALUE = "3a97b932a9d449c981b595";
    private static final String CONTENT_TYPE_KEY = "Content-Type";
    private static final String CONTENT_TYPE_JSON_VALUE = "application/json";
    private static final String APP_VERSION_KEY = "appVersion";
    private static final String APP_VERSION_VALUE = "5.10.0";
    private static final String API_VERSION_KEY = "apiVersion";
    private static final String API_VERSION_VALUE = "3.0.0";
    private static final String AUTHORIZATION_KEY = "Authorization";

    private MoneyBoxApiUtils() {};

    public static String getLoginUrl() {
        return LOGIN_URL;
    }

    /*Build the authenticate login url*/
    public static JSONObject getLoginParams(String email, String password) {
        JSONObject loginParams = new JSONObject();

        try {
            loginParams.put(LOGIN_EMAIL_PARAM, email);
            loginParams.put(LOGIN_PASSWORD_PARAM, password);
            loginParams.put(LOGIN_IDFA_PARAM, "ANYTHING");
        } catch (JSONException e) {
            LogUtils.e(e.getMessage());
        }

        return loginParams;
    }

    public static String getInvestorProductUrl() {
        return INVESTOR_PRODUCT_URL;
    }

    public static String getOneOffPaymentUrl() {
        return ONE_OFF_PAYMENT_URL;
    }

    /*Build the authenticate login url*/
    public static JSONObject getPaymentParams(double amount, int id) {
        JSONObject loginParams = new JSONObject();

        try {
            loginParams.put(PAYMENT_AMOUNT_PARAM, amount);
            loginParams.put(PAYMENT_INVESTOR_PRODUCT_ID_PARAM, id);
        } catch (JSONException e) {
            LogUtils.e(e.getMessage());
        }

        return loginParams;
    }

    public static HashMap getHeaders() {
        HashMap headers = new HashMap();

        headers.put(APP_ID_KEY, APP_ID_VALUE);
        headers.put(CONTENT_TYPE_KEY, CONTENT_TYPE_JSON_VALUE);
        headers.put(APP_VERSION_KEY, APP_VERSION_VALUE);
        headers.put(API_VERSION_KEY, API_VERSION_VALUE);

        return headers;
    }

    public static HashMap getAuthorizedHeaders(Context context) {
        HashMap headers = getHeaders();
        String brokerToken = PreferenceUtils.getBrokerToken(context);

        headers.put(AUTHORIZATION_KEY, "Bearer " + brokerToken);

        return headers;
    }
}
