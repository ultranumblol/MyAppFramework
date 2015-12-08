package com.wgz.ant.myappframework;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.wgz.ant.myappframework.util.CheckLogin;
import com.wgz.ant.myappframework.util.OnDataFinishedListener;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    boolean net;
    private CheckBox autologin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        String flag =  getsp();
        if (flag.equals("true")){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();

        }

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        setTitle("请登陆");
        autologin = (CheckBox) findViewById(R.id.autologin);
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
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                net = checkNetWorkStatus(getApplicationContext());
                if (net == false) {
                    Toast.makeText(LoginActivity.this, "请连接网络登陆！", Toast.LENGTH_LONG).show();
                }else {
                    attemptLogin();
                }

            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }



    /**
     * 判断网络状态
     * @param context
     * @return
     */
    public static boolean checkNetWorkStatus(Context context){
        boolean result;
        ConnectivityManager cm=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        if ( netinfo !=null && netinfo.isConnected() ) {
            result=true;
            Log.i("xml", "The net was connected");
        }else{
            result=false;
            Log.i("xml", "The net was bad!");
        }
        return result;
    }


    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();
        CheckLogin checkLogin = new CheckLogin(email, password);
        checkLogin.execute();


        final boolean[] cancel = {false};
        final View[] focusView = {null};
        checkLogin.setOnDataFinishedListener(new OnDataFinishedListener() {
            @Override
            public void onDataSuccessfully(Object data) {
                if (autologin.isChecked()) {
                    savesp();
                }
                // Check for a valid password, if the user entered one.
                if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
                    mPasswordView.setError(getString(R.string.error_invalid_password));
                    focusView[0] = mPasswordView;
                    cancel[0] = true;
                }

                // Check for a valid email address.
                if (TextUtils.isEmpty(email)) {
                    mEmailView.setError(getString(R.string.error_field_required));
                    focusView[0] = mEmailView;
                    cancel[0] = true;
                }

                if (cancel[0]) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView[0].requestFocus();
                } else {
                    // Show a progress spinner, and kick off a background task to
                    // perform the user login attempt.
                    showProgress(true);
                    mAuthTask = new UserLoginTask(email, password);
                    mAuthTask.execute((Void) null);
                }

            }

            @Override
            public void onDataFailed() {
                Toast.makeText(LoginActivity.this, "请输入正确的用户名和密码！", Toast.LENGTH_LONG).show();
                return;
            }
        });

    }


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);

    }
    public void onPause() {
        super.onPause();

        MobclickAgent.onPause(this);
    }
    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                Thread.sleep(1500);

            } catch (InterruptedException e) {
                return false;
            }


            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {

                savename(mEmail);
                finish();
                Toast.makeText(LoginActivity.this,"登录成功！",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));

            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
    //保存登陆状态
    private void savesp(){
        SharedPreferences preferences = getSharedPreferences("autologin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor  = preferences.edit();
        editor.putString("autologin","true");
        editor.commit();

    }
    //保存登陆号码
    private void savename(String phone){
        SharedPreferences preferences = getSharedPreferences("userphone", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor  = preferences.edit();
        editor.putString("userphone",phone);
        editor.commit();


    }
    private String getsp(){
        SharedPreferences preferences = getSharedPreferences("autologin", Context.MODE_PRIVATE);
        String flag = preferences.getString("autologin","false");
        return flag;
    }
}

