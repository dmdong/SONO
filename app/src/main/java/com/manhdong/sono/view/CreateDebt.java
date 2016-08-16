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
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.manhdong.sono.R;
import com.manhdong.sono.database.dao.DebtDAO;
import com.manhdong.sono.model.Debt;
import com.manhdong.sono.model.Person;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateDebt extends AppCompatActivity implements View.OnClickListener{

    public static final int PICK_CONTACT = 1023;
    EditText pName, amount, debtInfo;
    RadioButton selectMoney, selectPromise, selectEvent;
    //    TextView selectMoney, selectPromise, selectEvent;
    TextView startDate, endDate;    Button btnSave, btnCancel;
    ImageView ivStart, ivExp;
    ImageButton btnContact;
    LinearLayout getDescription;
    String debtType;
    Button btnSelectCurrency;
    String currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_debt);
        addControl();
        catchEvent();
        addCurrency();

    }

    private void addCurrency() {
        btnSelectCurrency = (Button) findViewById(R.id.selectCurrency);
        btnSelectCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(CreateDebt.this);
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

//                ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(CreateDebt.this,
//                        android.R.layout.simple_spinner_item, listDanhMuc);
//                spinner.setAdapter(spAdapter);
                ArrayAdapter<String> lvAdapter = new ArrayAdapter<String>(CreateDebt.this, android.R.layout.simple_list_item_single_choice, listDanhMuc);
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
//                GridView gv = (GridView) dialog.findViewById(R.id.listCurrency);
//                gv.setAdapter(lvAdapter);
////                gv.setNumColumns(2);
//                Button btnLuu = (Button) dialog.findViewById(R.id.btnSave);
//                btnLuu.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
////                       currency = spinner.getSelectedItem().toString();
//                        dialog.dismiss();
//                    }
//                });
                dialog.show();

            }
        });
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
        getDescription = (LinearLayout) findViewById(R.id.descDebt);
        btnContact = (ImageButton) findViewById(R.id.selectContact);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
//            case R.id.selectMoney:
//                amount.setVisibility(View.VISIBLE);
//                getDescription.setVisibility(View.VISIBLE);
//                debtType = "MONEY";
//                break;
//
//            case R.id.selectEvent:
//                amount.setVisibility(View.GONE);
//                debtType = "EVENT";
//                getDescription.setVisibility(View.GONE);
//
//                break;
//
//            case R.id.selectPromise:
//                amount.setVisibility(View.GONE);
//                debtType = "PROMISE";
//                getDescription.setVisibility(View.GONE);
//
//                break;

            case R.id.btnSave:
                saveDebt();

                break;

            case R.id.btnCancel:
                finish();

                break;

            case R.id.iVStartDate:
                pickDate("STARTDATE");

                break;

            case R.id.iVExpDate:
                pickDate("EXPIREDATE");

                break;
            case R.id.selectContact:
                pickContact();

                break;

        }
    }

    private void pickContact() {
        Intent selectContact = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(selectContact, PICK_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Check if result is ok
        if(resultCode == RESULT_OK){
            Person person = new Person();
            // Check if it is the request code we send
            if(requestCode == PICK_CONTACT){
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

    private void pickDate(final String datetype) {

        String dateFormat = "dd/MM/yyyy";
        final SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
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
        DatePickerDialog picker = new DatePickerDialog(CreateDebt.this, callback,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DATE));

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
            }else {
                debt.setAmount(0.0);
                debt.setCurrency("N/A");
            }
            debt.setReason(debtInfo.getText().toString());

            debt.setExpDate(endDate.getText().toString());
            debt.setStartDate(startDate.getText().toString());


            DebtDAO debtDAO = new DebtDAO(this);
            debtDAO.createDebt(debt);
            finish();
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
