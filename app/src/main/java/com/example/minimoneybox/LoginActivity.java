package com.example.minimoneybox;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.minimoneybox.Utils.*;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private static final String EMAIL_REGEX = "[^@]+@[^.]+\\..+";
    private static final String NAME_REGEX = "[a-zA-Z]{6,30}";
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[A-Z]).{10,50}$";
    private static final int FIRST_MIN_ANIM = 0;
    private static final int FIRST_MAX_ANIM = 109;
    private static final int SECOND_MIN_ANIM = 131;
    private static final int SECOND_MAX_ANIM = 158;

    private Button btn_sign_in;
    private TextInputLayout til_email;
    private EditText et_email;
    private TextInputLayout til_password;
    private EditText et_password;
    private TextInputLayout til_name;
    private EditText et_name;
    private LottieAnimationView pigAnimation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupAnimation();
    }

    private void setupViews() {
        btn_sign_in = findViewById(R.id.btn_sign_in);
        til_email = findViewById(R.id.til_email);
        et_email = findViewById(R.id.et_email);
        til_password = findViewById(R.id.til_password);
        et_password = findViewById(R.id.et_password);
        til_name = findViewById(R.id.til_name);
        et_name = findViewById(R.id.et_name);
        pigAnimation = findViewById(R.id.animation);

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_email.setText("androidtest@moneyboxapp.com");
                et_password.setText("P455word12");
                et_name.setText("Keegan");

                if (allFieldsValid()) {
                    Toast.makeText(v.getContext(), R.string.input_valid, Toast.LENGTH_LONG).show();

                    moneyboxLogin();
                }
            }
        });
    }

    private void moneyboxLogin() {
        //save the name to the preferences
        PreferenceUtils.setName(getApplicationContext(), et_name.getText().toString());

        //get the moneybox login url
        String url = MoneyBoxApiUtils.getLoginUrl();

        //get the moneybox login param
        JSONObject params = MoneyBoxApiUtils.getLoginParams(et_email.getText().toString(),
                et_password.getText().toString());

        JsonObjectRequest jsonLoginReq = new JsonObjectRequest(Request.Method.POST,
                url, params,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        //LogUtils.d("Response:\n" + response);

                        String brokerToken = MoneyBoxJsonUtils.getBrokerToken(response.toString());

                        LogUtils.d("Broker Token = " + brokerToken);

                        //save the broker token to the preferences
                        PreferenceUtils.setBrokerToken(getApplicationContext(), brokerToken);

                        //load the user accounts activity
                        if (brokerToken != null && brokerToken.length() > 0) {
                            Intent intent = new Intent(getApplicationContext(), UserAccountsActivity.class);
                            startActivity(intent);
                        }
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
                HashMap headers = MoneyBoxApiUtils.getHeaders();
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

    private boolean allFieldsValid() {
        boolean allValid = false;

        if (Pattern.matches(EMAIL_REGEX, et_email.getText().toString())) {
            allValid = true;
        } else {
            til_email.setError(getString(R.string.email_address_error));
        }

        if (Pattern.matches(PASSWORD_REGEX, et_password.getText().toString())) {
            allValid = true;
        } else {
            til_password.setError(getString(R.string.password_error));
        }

        //only validate if the name has been input
        if (et_name.getText().toString().length() != 0) {
            if (Pattern.matches(NAME_REGEX, et_name.getText().toString())) {
                allValid = true;
            } else {
                til_name.setError(getString(R.string.full_name_error));
            }
        }

        return allValid;
    }

    private void setupAnimation() {
        pigAnimation.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                //remove the listener
                pigAnimation.removeAllAnimatorListeners();

                //set the second stage animation
                pigAnimation.setMinAndMaxFrame(SECOND_MIN_ANIM, SECOND_MAX_ANIM);

                //repeat animation
                pigAnimation.setRepeatMode(LottieDrawable.RESTART);
                pigAnimation.setRepeatCount(LottieDrawable.INFINITE);
                pigAnimation.playAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });

        //play first animation
        pigAnimation.setMinAndMaxFrame(FIRST_MIN_ANIM, FIRST_MAX_ANIM);
        pigAnimation.setRepeatCount(0);
        pigAnimation.playAnimation();
    }
}
