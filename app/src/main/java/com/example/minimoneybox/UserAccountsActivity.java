package com.example.minimoneybox;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import com.example.minimoneybox.Utils.MoneyBoxJsonUtils;
import com.example.minimoneybox.Utils.PreferenceUtils;
import com.example.minimoneybox.Utils.VolleyRequestUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserAccountsActivity extends AppCompatActivity implements AccountAdapter.ItemClickListener {
    private RecyclerView mRecyclerView;
    private AccountAdapter mAccountAdapter;
    private List<Account> mAccounts;
    private TextView mTotalPlanValueTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_accounts);

        mTotalPlanValueTextView = findViewById(R.id.total_plan_value_textview);
        TextView helloUserTextView = findViewById(R.id.hello_user_textview);

        //if name was used when logging in then display the textview
        String name = PreferenceUtils.getName(this);
        if (name.length() == 0) {
            helloUserTextView.setVisibility(View.GONE);
        } else {
            helloUserTextView.setText(getString(R.string.hello_user, name));
        }

        //set the layout for the RecyclerView to be a linear layout,
        //which measures and positions items within a RecyclerView into a linear list
        mRecyclerView = (RecyclerView) findViewById(R.id.accounts_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initialize the adapter and attach it to the RecyclerView
        mAccountAdapter = new AccountAdapter(this, this);
        mRecyclerView.setAdapter(mAccountAdapter);

        getInvestorProducts();
    }

    @Override
    public void onItemClickListener(int itemId) {
        Account account = mAccounts.get(itemId);

        LogUtils.d("Clicked on " + account.getFriendlyName());

        Intent intent = new Intent(this, IndividualAccountActivity.class);

        //pass the account to the individual account activity
        intent.putExtra(getString(R.string.activity_obj), (Serializable) account);

        startActivity(intent);
    }

    private void getInvestorProducts() {
        String url = MoneyBoxApiUtils.getInvestorProductUrl();

        JsonObjectRequest jsonLoginReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        //LogUtils.d("Response:\n" + response);

                        //get the accounts from the response
                        mAccounts = MoneyBoxJsonUtils.getAccounts(response.toString());

                        //update the total plan value
                        mTotalPlanValueTextView.setText(getString(R.string.total_plan_value,
                                String.valueOf(getTotalPlanValue())));

                        //update the adapter accounts
                        mAccountAdapter.setAccounts(mAccounts);
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

    private double getTotalPlanValue() {
        double totalPlanValue = 0;

        for(Account account : mAccounts) {
            totalPlanValue = totalPlanValue + account.getPlanValue();
        }

        return totalPlanValue;
    }
}
