package com.pruebaandroid.misterhouse.ui.Activitys;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.pruebaandroid.misterhouse.R;
import com.pruebaandroid.misterhouse.Utils.Utils;
import com.pruebaandroid.misterhouse.WebServices.Endpoints;

import io.fabric.sdk.android.Fabric;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


/**
 * A login screen that offers login via user/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String LOG_TAG = LoginActivity.class.getName();
    // UI references.
    private AutoCompleteTextView mUserView;
    private EditText mPasswordView;
    private View mProgressView;
    private RequestQueue mRequestQueue;
    private Activity mActivity;
    public LinearLayout mLoginForm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_login);
        mActivity = this;
        mLoginForm = (LinearLayout) findViewById(R.id.login_form);
        // Set up the login form.
        mUserView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mProgressView = findViewById(R.id.login_progress);
        String service = Utils.getsessionToken(this);
        if (!service.equals(getString(R.string.sessionToken))) {
            starMainActivity();
        }
    }

    private void attemptLogin() {

        mUserView.setError(null);
        mPasswordView.setError(null);

        String email = mUserView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(email)) {
            mUserView.setError(getString(R.string.error_field_required));
            focusView = mUserView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            if (Utils.isConnected(this)) {
                loginRequest(mUserView.getText().toString(), mPasswordView.getText().toString());
            }
            else{
                showErrorMessage(getString(R.string.error_no_intertet_connectio));
            }

        }
    }

    private void starMainActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        LoginActivity.this.finish();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginForm.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    public void loginRequest(String user, String password) {
        showProgress(true);
        mRequestQueue = Volley.newRequestQueue(this);
        String url = Endpoints.BASEURL + String.format(Endpoints.LOGIN, user, password);
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        try {
                            String token = response.getString(getString(R.string.sessionToken));
                            Utils.setsessionToken(mActivity, token);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d(String.format("successful response call endpoint %s", Endpoints.LOGIN), response.toString());
                        showProgress(false);
                        starMainActivity();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        showProgress(false);
                        if (error.networkResponse.statusCode == 404) {
                            showErrorMessage(getString(R.string.error_loggin_credentials));
                        } else {
                            Log.d(String.format("ERROR response call endpoint %s", Endpoints.LOGIN), "onErrorResponse");
                        }
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = Endpoints.getHeaders();
                return params;
            }
        };
        mRequestQueue.add(postRequest);
    }

    private void showErrorMessage(String messgge) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.Opps)).setMessage(messgge)
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }
}

