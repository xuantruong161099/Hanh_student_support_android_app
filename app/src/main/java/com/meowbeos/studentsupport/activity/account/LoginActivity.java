package com.meowbeos.studentsupport.activity.account;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.meowbeos.studentsupport.R;
import com.meowbeos.studentsupport.activity.MainActivity;
import com.meowbeos.studentsupport.constants.STConstant;
import com.meowbeos.studentsupport.model.LoginAccount;
import com.meowbeos.studentsupport.model.RequestObject;
import com.meowbeos.studentsupport.model.ResponseLoginAccount;
import com.meowbeos.studentsupport.model.ResponseObject;
import com.meowbeos.studentsupport.service.ServiceAPI;
import com.meowbeos.studentsupport.utils.SessionManager;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "Login";
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private SessionManager mSession;
    private HashMap<String, String> _hmUser;

    private EditText edUserName, edPassWord;
    private Button btnLogin;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSession = new SessionManager(LoginActivity.this);
        if (mSession.isLoggedIn()) { // đã đăng nhập
            _hmUser = mSession.getUserDetails();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("studentID", String.valueOf(_hmUser.get(SessionManager.ID)));
            startActivity(intent);
        }

        initComponents();
        addEvent();
    }

    private void initComponents() {
        edUserName = findViewById(R.id.edUserName);
        edPassWord = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void addEvent() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = edUserName.getText().toString().trim();
                String password = edPassWord.getText().toString().trim();
                if(edUserName.getText().toString().isEmpty() || edPassWord.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "Lỗi, vui lòng thử lại", Toast.LENGTH_LONG).show();
                }
                else {
                    RequestObject requestObject = new RequestObject();
                    requestObject.setLoginAccount(new LoginAccount(userName, password));
                    postLoginAccount(requestObject);
                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                }

            }
        });
    }

    private void postLoginAccount(RequestObject loginAccount) {

        ServiceAPI serviceAPI = new Retrofit.Builder()
                .baseUrl(STConstant.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ServiceAPI.class);

        compositeDisposable.add(serviceAPI.postLoginAccount(loginAccount)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        );
    }

    private void handleError(Throwable throwable) {
        Log.e(TAG, "handleError: Error", throwable);
    }

    private void handleResponse(ResponseObject responseObject) {
        if (STConstant.RESPONSE_SUCCESS.equals(responseObject.getResultCode())) {
            ResponseLoginAccount responseLoginAccount = responseObject.getResponseLoginAccount();
            //Log.d(TAG, "handleResponse: " + responseObject.getResultCode());
            mSession.createLoginSession(responseLoginAccount.getIdStudent(),"", responseLoginAccount.getNameStudent());
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);

            intent.putExtra("studentID", String.valueOf(responseLoginAccount.getIdStudent()));

            startActivity(intent);
            progressDialog.dismiss();
            finish();
        } else {
            Toast.makeText(LoginActivity.this, "Lỗi, vui lòng thử lại", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}