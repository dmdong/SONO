package com.manhdong.sono.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.manhdong.sono.R;
import com.manhdong.sono.database.dao.DebtDAO;
import com.manhdong.sono.model.Debt;
import com.manhdong.sono.model.Person;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditDebt extends AppCompatActivity implements View.OnClickListener{

    EditText pName, amount, debtInfo;
    RadioButton selectMoney, selectPromise, selectEvent;
//    TextView selectMoney, selectPromise, selectEvent;
    TextView startDate, endDate;
    Button btnSave, btnCancel, btnDelete;
    ImageView ivStart, ivExp;
    ImageButton btnContact;
    LinearLayout getDescription;
    Debt debt;
    String debtType, currency;
    int position;
    DebtDAO debtDAO;
    Button btnSelectCurrency;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_debt);
        addControl();
        catchEvent();

        Intent intent = getIntent();
        debt = (Debt) intent.getSerializableExtra("Debt");
        pName.setText(debt.getPerson());
        debtType = debt.getDebtType();
//        amount.setText(debt.getAmount().toString());
        currency = debt.getCurrency();
        debtInfo.setText(debt.getReason());
        startDate.setText(debt.getStartDate());
        endDate.setText(debt.getExpDate());
//        debt.getColumnID();
//        position = intent.getIntExtra("DebtPosition", -1);
        position = debt.getColumnID();

        debtDAO = new DebtDAO(this);

        if(debtType!=null){
            if(debtType.equals("MONEY")){
                amount.setText(debt.getAmount().toString());
//                amount.setVisibility(View.VISIBLE);
//                getDescription.setVisibility(View.VISIBLE);
                selectMoney.setChecked(true);
            }else if (debtType.equals("PROMISE")){
                selectPromise.setChecked(true);
            }else if(debtType.equals("EVENT")){
                selectEvent.setChecked(true);
            }

        }

        addCurrency();


    }

    private void catchEvent() {
//        getDescription.setOnClickListener(this);
        btnContact.setOnClickListener(this);
//        selectPromise.setOnClickListener(this);
//        selectMoney.setOnClickListener(this);
//        selectEvent.setOnClickListener(this);
        ivStart.setOnClickListener(this);
        ivExp.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

    }

    private void addControl() {
        pName = (EditText) findViewById(R.id.pName);
        amount = (EditText) findViewById(R.id.amount);
        debtInfo = (EditText) findViewById(R.id.debtInfo);
        startDate = (TextView) findViewById(R.id.startDate);
        endDate = (TextView) findViewById(R.id.expDate);
        ivStart = (ImageView) findViewById(R.id.iVStartDate);
        ivExp = (ImageView) findViewById(R.id.iVExpDate);
//        selectEvent = (TextView) findViewById(R.id.selectEvent);
//        selectMoney = (TextView) findViewById(R.id.selectMoney);
//        selectPromise = (TextView) findViewById(R.id.selectPromise);
        selectMoney = (RadioButton) findViewById(R.id.selectMoney);
        selectEvent = (RadioButton) findViewById(R.id.selectEvent);
        selectPromise = (RadioButton) findViewById(R.id.selectPromise);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        getDescription = (LinearLayout) findViewById(R.id.descDebt);
        btnContact = (ImageButton) findViewById(R.id.selectContact);
    }
    private void addCurrency() {
        btnSelectCurrency = (Button) findViewById(R.id.selectCurrency);
        btnSelectCurrency.setText("CURRENCY: "+currency);
        btnSelectCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(EditDebt.this);
                dialog.setContentView(R.layout.select_currency);
//                final Spinner spinner = (Spinner) dialog.findViewById(R.id.spinnerDanhMuc);
                final List<String> listDanhMuc = new ArrayList<>();
                listDanhMuc.add("VND");
                listDanhMuc.add("USD");
                listDanhMuc.add("JPY");
                listDanhMuc.add("SGD");
                listDanhMuc.add("INR");
                listDanhMuc.add("RUB");
                listDanhMuc.add("SWF");

                ArrayAdapter<String> lvAdapter = new ArrayAdapter<String>(EditDebt.this, android.R.layout.simple_list_item_single_choice, listDanhMuc);
                ListView lv = (ListView) dialog.findViewById(R.id.listCurrency);
                lv.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
                lv.setAdapter(lvAdapter);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        currency = listDanhMuc.get(position);
                        btnSelectCurrency.setText("CURRENCY: "+currency);
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
//            case R.id.selectMoney:
//
//                break;
//
//            case R.id.selectEvent:
//
//                break;
//
//            case R.id.selectPromise:
//
//
//                break;

            case R.id.btnSave:
                saveDebt();

                break;

            case R.id.btnCancel:
                finish();

                break;

            case R.id.iVStartDate:

                try {
                    pickDate("STARTDATE", startDate.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                break;

            case R.id.iVExpDate:
                try {
                    pickDate("EXPIREDATE", endDate.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.selectContact:
                pickContact();

                break;

            case R.id.btnDelete:
                debtDAO.deleteDebt(position);
                finish();

                break;

        }
    }

    private void pickDate(final String datetype, final String date) throws ParseException {

        String dateFormat = "dd/MM/yyyy";
        final SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        int sYear = 0, sMonth = 0, sDay = 0;
        if (!date.equals("") || !date.equals("Không xác định") ||date.equals("Example")){
            sYear = Integer.parseInt(date.substring(6, 10));
            sMonth =  Integer.parseInt(date.substring(3, 5)) -1;  //Lùi 1 tháng do tháng chạy từ 0->11
            sDay = Integer.parseInt(date.substring(0, 2));
        }

       // Toast.makeText(EditDebt.this, sDay +"/" + sMonth + "/" + sYear, Toast.LENGTH_SHORT).show();

        final Calendar cal = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                cal.set(year,monthOfYear,dayOfMonth);
                if (datetype.equals("STARTDATE")){
                    startDate.setText(sdf.format(cal.getTime()));
                }else if(datetype.equals("EXPIREDATE")){
                    endDate.setText(sdf.format(cal.getTime()));
                }
            }
        };
        DatePickerDialog picker = new DatePickerDialog(EditDebt.this, callback,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DATE));
        picker.updateDate(sYear,sMonth,sDay);

        picker.show();

    }

    private void saveDebt() {

        Debt debt = new Debt();
        if (TextUtils.isEmpty(pName.getText().toString())){
            pName.setError("This field cannot be left empty");

        } else if (debtType == null){
            selectEvent.setError("A type is required!");


        }
        else {

            debt.setPerson(pName.getText().toString());
            debt.setDebtType(debtType);
            if (debtType!=null && debtType.equals("MONEY")) {
                debt.setAmount(Double.parseDouble(amount.getText().toString()));
                debt.setCurrency(currency);
            } else {
                debt.setAmount(0.0);
                debt.setCurrency("N/A");
            }
            debt.setReason(debtInfo.getText().toString());
            debt.setExpDate(endDate.getText().toString());
            debt.setStartDate(startDate.getText().toString());
            debtDAO.updateDebt(debt, position);
            finish();
        }
//        Debt debt = new Debt();
//        if (TextUtils.isEmpty(pName.getText().toString())){
//            pName.setError("This field cannot be left empty");
//
//        } else if (debtType == null){
//            selectEvent.setError("A type is required!");
//
//
//        }
//        else {
//
//            debt.setPerson(pName.getText().toString());
//            debt.setDebtType(debtType);
//            if (debtType!=null && debtType.equals("MONEY")) {
//                debt.setAmount(Double.parseDouble(amount.getText().toString()));
//                debt.setCurrency("$");
//            }
//            debt.setReason(debtInfo.getText().toString());
//
//            debt.setExpDate(endDate.getText().toString());
//            debt.setStartDate(startDate.getText().toString());
//
//            debtDAO.updateDebt(debt, position);
//            finish();
//        }

    }

    private void pickContact() {
        Intent selectContact = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(selectContact, CreateDebt.PICK_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Check if result is ok
        if(resultCode == RESULT_OK){
            Person person = new Person();
            // Check if it is the request code we send
            if(requestCode == CreateDebt.PICK_CONTACT){
                Cursor cursor = null;

                Uri uri = data.getData();
                cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int firstNameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
//                    int emailIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);
//                    int familyIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Relation.TYPE);
                    person.setPhoneNumber(cursor.getString(phoneIndex));
                    person.setPersonName(cursor.getString(firstNameIndex));
//                    person.setEmailAddress(cursor.getString(emailIndex));
//                    person.setRelationship(cursor.getString(familyIndex));
                    Log.d("CONTACT", "onActivityResult: "+ person.toString());
                    cursor.close();
                }

            }
            pName.setText(person.getPersonName());
            if(pName.getError()!= null){
                pName.setError(null);
            }
        }

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.selectMoney:
                if (checked){
                    amount.setVisibility(View.VISIBLE);
                    getDescription.setVisibility(View.VISIBLE);
                    debtType = "MONEY";
                }
                    break;
            case R.id.selectPromise:
                if (checked){
                amount.setVisibility(View.GONE);
                debtType = "PROMISE";
                getDescription.setVisibility(View.GONE);
                }
                    break;
            case R.id.selectEvent:
                if (checked){
                    amount.setVisibility(View.GONE);
                    debtType = "EVENT";
                    getDescription.setVisibility(View.GONE);
                }

                    break;
        }

    }
}
