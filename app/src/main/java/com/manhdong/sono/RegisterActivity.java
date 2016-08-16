package com.manhdong.sono;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.manhdong.sono.database.DBContext;
import com.manhdong.sono.database.dao.DebtDAO;
import com.manhdong.sono.model.Debt;
import com.manhdong.sono.model.Person;
import com.manhdong.sono.util.GetPreference;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView ivProfile;
    EditText edtEmail, edtUsername, edtPasscode, edtConfirm;
    LinearLayout lnProfile;
    Button btnCreateAcc;
    GetPreference pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        addControls();
        addEvents();

    }

    private void addEvents() {
        lnProfile.setOnClickListener(this);
        btnCreateAcc.setOnClickListener(this);
    }


    private void addControls() {
        ivProfile = (ImageView) findViewById(R.id.ivProfile);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPasscode = (EditText) findViewById(R.id.edtPasscode);
        edtConfirm = (EditText) findViewById(R.id.edtPassConfirm);
        lnProfile = (LinearLayout) findViewById(R.id.lnProfilePic);
        btnCreateAcc = (Button) findViewById(R.id.btnCreateAcc);
        //Nhận username từ bên màn hình login
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        edtUsername.setText(username);

        //Gọi Preference
        pref = new GetPreference(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.lnProfilePic){

        }else if (id == R.id.btnCreateAcc){
            String usermail = edtUsername.getText().toString();
            String user = edtUsername.getText().toString();
            String passcode = edtPasscode.getText().toString();
            String cfPasscode = edtConfirm.getText().toString();
            //Xử lý String url profilePic

            if(validateUser(usermail, user, passcode, cfPasscode)){
                //Tiến hành lưu thông tin login xuống preference
                pref.savethisUser(user, passcode);
                if(usermail != null || !usermail.equals("")){
                    pref.setUserEmail(user, usermail);
                } else pref.setUserEmail(user, "default@sono.com");

                //Thêm các mẫu debt vào database
                addDemoDebt(user);

                //Gọi màn hình Summary
                Intent intent = new Intent(RegisterActivity.this, DebtSummary.class);
                Person person = new Person(pref.getUserEmail(user), user, null, "MYSELF");
                intent.putExtra("PERSON", person);
                intent.putExtra("username", user); //Gửi username sang DBContext
                startActivity(intent);
                finishAffinity();
            };

        }
    }

    private boolean validateUser(String usermail, String user, String passcode, String cfPasscode) {
        //Kiểm tra các thông tin nhập hợp lệ
//        CharSequence chars = usermail.getChars();
//        boolean isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(chars).matches();
        Boolean valid = false;
        if (user.length() <4){
            edtUsername.setError(getResources().getString(R.string.username_4chars));
        }else if(pref.hasThisUser(user)){
            edtUsername.setError(getResources().getString(R.string.user_exists));
        }
        else {
            //User hợp lệ rồi mới kiểm tra passcode
            if (!passcode.equals(cfPasscode)) {
                edtPasscode.setError(getResources().getString(R.string.passcode_must_be_the_same));
            } else if (passcode.length() != 4){
                edtPasscode.setError(getResources().getString(R.string.passcode_4numbers));
            } else valid = true;

        };
        return valid;
    }

    private void addDemoDebt(String username) {
        List<Debt> debts = new ArrayList<>();
        //Add các debt demo --> Double amount, String debtType, String expDate, String person, String reason, String startDate, String currency
        debts.add(new Debt(null, "EVENT", "01/01/2017", "Đây chỉ là ví dụ", "Các vd mẫu sẽ tự động bị xóa sau 3 lần sử dụng", "Example", "N/A"));
        debts.add(new Debt(100000.0, "MONEY", "01/11/2016", "Sam", "Mượn tiền Elena mua điện thoại", "Example", "VND"));
        debts.add(new Debt(200.0, "MONEY", "12/03/2015", "Bé Trân", "Trả tiền công", "Example", "USD"));
        debts.add(new Debt(0.0, "EVENT", "12/03/15", "Trung", "Hôm nay ghim mày cho tao leo cây!", "Example", "SGD"));
        debts.add(new Debt(null , "PROMISE", "Không xác định", "Huy", "Hứa trả điện thoại cho nó", "Example", "N/A"));
        DBContext.TABLE_NAME = username;
        DebtDAO debtdemo = new DebtDAO(this);
        debtdemo.createDebtTable();
        for (Debt debt :
                debts) {
            debtdemo.createDebt(debt);
        }
    }

}


