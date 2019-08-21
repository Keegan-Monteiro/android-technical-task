package com.example.minimoneybox;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.minimoneybox.Utils.LogUtils;
import com.example.minimoneybox.Utils.MoneyBoxApiUtils;
import com.example.minimoneybox.Utils.VolleyRequestUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class IndividualAccountActivity extends AppCompatActivity {
    private Account mAccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_account);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(getString(R.string.activity_obj))) {
            mAccount = (Account)intent.getSerializableExtra(getString(R.string.activity_obj));
        }

        TextView planNameTextView = findViewById(R.id.ia_plan_name_textview);
        TextView planValueTextView = findViewById(R.id.ia_plan_value_textview);
        TextView moneyboxTextView = findViewById(R.id.ia_moneybox_textview);
        Button quickAddButton = findViewById(R.id.ia_quick_add_button);

        if (mAccount != null) {
            planNameTextView.setText(mAccount.getFriendlyName());
            planValueTextView.setText(mAccount.getPlanValueString(this));
            moneyboxTextView.setText(mAccount.getMoneyBoxString(this));
            quickAddButton.setText(mAccount.getQuickAddAmountString(this));

            quickAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addAmount();
                }
            });
        }
    }

    private void addAmount() {
        //get the moneybox login url
        String url = MoneyBoxApiUtils.getOneOffPaymentUrl();

        //get the moneybox login param
        JSONObject params = MoneyBoxApiUtils.getPaymentParams(mAccount.getQuickAddAmount(),
                mAccount.getId());

        JsonObjectRequest jsonLoginReq = new JsonObjectRequest(Request.Method.POST,
                url, params,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        //LogUtils.d("Response:\n" + response);

                        Toast.makeText(getApplicationContext(), getString(R.string.payment_successful), Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LogUtils.e("Error:\n" + error);
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            /** Passing some request headers* */
            @Override
            public Map getHeaders() throws AuthFailureError {
                //get the moneybox api headers
                HashMap headers = MoneyBoxApiUtils.getAuthorizedHeaders(getApplicationContext());
                return headers;
            }
        };

        //set timeout
        jsonLoginReq.setRetryPolicy(new DefaultRetryPolicy(R.integer.API_QUERY_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add the request to the RequestQueue.
        VolleyRequestUtils.getInstance(this).addToRequestQueue(jsonLoginReq);
    }
}
