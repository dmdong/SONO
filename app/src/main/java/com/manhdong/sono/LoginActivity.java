package com.manhdong.sono;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.manhdong.sono.model.Person;
import com.manhdong.sono.util.GetPreference;

public class LoginActivity extends AppCompatActivity {

    Button btnSignin;
    AutoCompleteTextView autoUser;
    EditText edtPasscode;
    GetPreference pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addControls();

        pref = new GetPreference(this);
        String latestUser = pref.getLatestUsername();
        Log.d("MyLAST", "onCreate: "+ latestUser);
        if (pref.getPasscodeOnOff(latestUser)){
            Log.d("PASSCODE STATUS", "Login: true");
            Intent intent = new Intent(LoginActivity.this, DebtSummary.class);
            if (!latestUser.equals("default")){
                intent.putExtra("username", latestUser); //Gửi username sang DBContext
                Person person = new Person(pref.getUserEmail(latestUser), latestUser, null, "MYSELF");
                intent.putExtra("PERSON", person);
                startActivity(intent);
                finish();
            }

        }


        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }

    private void signIn() {
        pref = new GetPreference(LoginActivity.this);
        String passcode = edtPasscode.getText().toString();
        String username = autoUser.getText().toString();
        if (checkUser(username)){
            if(pref.checkUserPass(username, passcode)){
                pref.setLatestUser(username);
                Intent intent = new Intent(LoginActivity.this, DebtSummary.class);
                intent.putExtra("username", username); //Gửi username sang DBContext
                Person person = new Person(pref.getUserEmail(username), username, null, "MYSELF");
                intent.putExtra("PERSON", person);
                startActivity(intent);
                finish();
            }
            else Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();

        } else {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            if (username == null){
                intent.putExtra("username", "");
            }else {
                intent.putExtra("username", username);
            }

            startActivity(intent);
        };
    }



    private void addControls() {
        btnSignin = (Button) findViewById(R.id.btnSignin);
        autoUser = (AutoCompleteTextView) findViewById(R.id.autoUser);
        edtPasscode = (EditText) findViewById(R.id.edtPasscode);

        edtPasscode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    signIn();
                    Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
    }

    private boolean checkUser(String username) {

        //Kiểm tra nếu user chưa tồn tại thì cho tạo tài khoản
        //Nếu user tồn tại thì kiểm tra mật khẩu
        if (username == null ){
            username = "default";
        }

        //Kiểm tra user trong preference data
        if (pref.hasThisUser(username)){
            return true;
        } else return false;
    }
}
