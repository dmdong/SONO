package com.manhdong.sono;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceActivity;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.manhdong.sono.database.DBContext;
import com.manhdong.sono.database.dao.DebtDAO;
import com.manhdong.sono.model.Debt;
import com.manhdong.sono.model.Person;
import com.manhdong.sono.util.GetPreference;
import com.manhdong.sono.view.CreateDebt;
import com.manhdong.sono.view.DebtAdapter;
import com.manhdong.sono.view.DebtAdapterCV;
import com.manhdong.sono.view.EditDebt;

import java.util.ArrayList;
import java.util.List;

public class DebtSummary extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    RecyclerView recyclerView;
    double sumAmount;
    public List<Debt> debts = new ArrayList<>();
//    FloatingActionButton addDebt;
    DebtAdapterCV adapter;
//    ImageView btnDelete;
    DebtDAO debtDAO;
    Toolbar toolbar;
    String user;
    MenuItem mnSearch, itemEdit;
    SearchView searchView;
    NavigationView navigationView;
    ImageView ivUserPic;
    TextView tvUserEmail, tvUsername;
    Person person;
    GetPreference pref;
    Button addDebt;
    TextView debtSUM;

    float historicX = Float.NaN, historicY = Float.NaN;
    static final int DELTA = 50;
    int idEditOld;
    int idDeleteOld;
//    enum Direction {LEFT, RIGHT;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        pref = new GetPreference(this);

        //Thêm các view cơ bản
        user = getIntent().getStringExtra("username");
        Log.d("USERTAG", "onCreate: "+ user);
//        person = new Person();
        person = (Person) getIntent().getSerializableExtra("PERSON");
        Log.d("PERSONTAG", "onCreate: "+ person.toString());
        addView();

        //Set TABLE DATA theo user đăng nhập
        DBContext.TABLE_NAME = user;
        debtDAO = new DebtDAO(this);
        debtDAO.createDebtTable();
        debtDAO.getDebts(debts, debtSUM);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        adapter = new DebtAdapterCV(debts, this, R.layout.debt_item);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Đăng ký contextMenu cho RecyclerView
//        registerForContextMenu(recyclerView);

        swipeToDel(); //Thêm chức năng kéo qua trái để xóa item
        //Xóa - archive các debt cũ paidDebts();

    }

    private void createActionMode() {
             toolbar.startActionMode(new ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        getMenuInflater().inflate(R.menu.edit_menu, menu);
                        showListViewCheckBox(View.VISIBLE);
                        return true;
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        return true;
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.mnDelete){
                            new AlertDialog.Builder(DebtSummary.this)
                                    .setTitle("Bạn đang xóa")
                                    .setIcon(android.R.drawable.star_on)
                                    .setMessage("Bạn có chắc rằng mình muốn xóa?")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete
                                            paidDebts();
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                        }
                                    })
                                    .show();

                        }

                        return false;
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {
                        showListViewCheckBox(View.GONE);

                    }
                });

    }

    private void paidDebts() {
        List<Debt> toDelete = new ArrayList<>();
        for (int i = 0; i < debts.size(); i++) {
            CheckBox checkBox = (CheckBox) recyclerView.getChildAt(i).findViewById(R.id.checkBox);
            if (checkBox.isChecked()){
                toDelete.add(debts.get(i));
            }
        }
        debtDAO.clearDebts(toDelete);
        refreshRecyclerView();

    }

    //Thêm các view như drawer, toolbar, fab
    private void addView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.welcome)+ " " + user.toUpperCase());

//        addDebt = (FloatingActionButton) findViewById(R.id.addDebt);
//        addDebt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(DebtSummary.this, CreateDebt.class);
//                startActivity(intent);
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);


        ivUserPic = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView);
        tvUsername = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvUsername);
        tvUserEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textView);
        String personName = "Hello " + person.getPersonName().replaceAll("[0-9]", "").toUpperCase()+ "!";

        tvUsername.setText(personName);
        tvUserEmail.setText(person.getEmailAddress());

        navigationView.setNavigationItemSelectedListener(this);

        addDebt = (Button) findViewById(R.id.addDebt);
        addDebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Mở trang tạo debt
                Intent intent = new Intent(DebtSummary.this, CreateDebt.class);
                startActivity(intent);
            }
        });

        debtSUM = (TextView) findViewById(R.id.tvNumberSum);
    }

    //Chức năng xóa & update các item trong recyclerview bằng cách slide qua trái
    private void swipeToDel() {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                TextView itemDel = null;
                TextView itemEdit = null;
                View holder = recyclerView.findChildViewUnder(event.getX(), event.getY());
                int position = recyclerView.getChildLayoutPosition(holder);
//                int position = adapter.getPosition();
//                View holder = recyclerView.getChildAt(position);
                if (holder != null && holder.isInTouchMode()) {
                    //Nếu holder được touch tồn tại mới findViewById
                    itemDel = (TextView) holder.findViewById(R.id.itemDel);
                    itemEdit = (TextView) holder.findViewById(R.id.itemEdit);

                    if (idEditOld != itemEdit.getId()){
                        ImageView oldEdit = (ImageView) findViewById(idEditOld);
                        ImageView oldDelete = (ImageView) findViewById(idDeleteOld);
                        if (oldEdit != null){
                            oldDelete.setVisibility(View.GONE);
                            oldEdit.setVisibility(View.GONE);
                        }

                    }


                    // TODO Auto-generated method stub
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            historicX = event.getX();
                            historicY = event.getY();
                            break;

                        case MotionEvent.ACTION_UP:
                            if (event.getX() - historicX < -DELTA) {
                                //Hiện button delete lên
                                itemDel.setVisibility(View.VISIBLE);
                                itemEdit.setVisibility(View.VISIBLE);
                                DeleteRowSlideRight(position, debts, itemDel);
                                EditOnSlide(position, debts, itemEdit);

                                idEditOld = itemEdit.getId();
                                idDeleteOld = itemEdit.getId();


                                return true;

                            } else if (event.getX() - historicX > DELTA) {
                                //Ẩn button delete đi
                                itemDel.setVisibility(View.GONE);
                                itemEdit.setVisibility(View.GONE);
                                return true;
                            }
                            break;

                        default: return false;
                    }
                }



                return false;
            }
        });
    }
    private void EditOnSlide(final int position, final List<Debt> debts, TextView itemEdit) {
        itemEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DebtSummary.this, EditDebt.class);
                intent.putExtra("Debt", debts.get(position));
//                intent.putExtra("DebtPosition", position);
                startActivity(intent);
            }
        });

    }
    //Listener để xóa item khỏi recyclerview và data
    private void DeleteRowSlideRight(final int position, final List<Debt> debts, TextView itemDel) {
        itemDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                debtDAO.deleteDebt(debts.get(position).getColumnID());
                double debtItem = debts.get(position).getAmount();
                if (debtItem != 0){
                    sumAmount = Double.parseDouble(debtSUM.getText().toString());
                    sumAmount -= debtItem;
                    debtSUM.setText(String.valueOf(sumAmount));
                }
                debts.remove(position);

                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override   //Xử lý khi user bấm back trên drawer và màn hình chính
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            showAlertDialog(this, "Bạn có muốn thoát", "Are you sure you want to exit the application?", true);
        }
    }

    private void showAlertDialog(Context context, String title, String message, final Boolean status) {
        new AlertDialog.Builder(context)
                .setTitle("Exit Application")
                .setIcon(android.R.drawable.star_on)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        getMenuInflater().inflate(R.menu.edit_menu, menu);
//    }

//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        //Phần dưới này sử dụng cho list view
////        int id = item.getItemId();
////        AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
////        int position = contextMenuInfo.position;
////
////        if (id == R.id.mnEdit){
////            //Mở activity editinfo
////            Intent intent = new Intent(DebtSummary.this, EditDebt.class);
////
////            intent.putExtra("Debt", debts.get(position));
////            startActivity(intent);
////
////        }else if (id == R.id.mnDelete){
////            //Xóa thông tin của item
////            debts.remove(position);
////            debtDAO.deleteDebt(position);
////            adapter.notifyDataSetChanged();
////        }
//        int position = -1;
//        position = adapter.getPosition();
//
//        switch (item.getItemId()) {
//            case R.id.mnEdit:
//                //Mở activity editinfo
//                Intent intent = new Intent(DebtSummary.this, EditDebt.class);
//                intent.putExtra("Debt", debts.get(position));
//                startActivity(intent);
//                break;
//            case R.id.mnDelete:
//                //Xóa thông tin của item
//                debts.remove(position);
//                debtDAO.deleteDebt(position);
//                adapter.notifyDataSetChanged();
//                break;
//        }
//
//        return super.onContextItemSelected(item);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen, menu);
        mnSearch = menu.findItem(R.id.icoSearch);
        searchView = (SearchView) mnSearch.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchView.setSubmitButtonEnabled(true);
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        MenuItemCompat.setOnActionExpandListener(mnSearch, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                debts.clear();
                debtDAO.getDebts(debts, debtSUM);
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        MenuItem itemEdit = menu.findItem(R.id.multiple_delete);
        MenuItemCompat.setOnActionExpandListener(itemEdit, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                showListViewCheckBox(View.VISIBLE);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                showListViewCheckBox(View.GONE);
                return true;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            pref.setLatestUser("default");
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finishAffinity();
            return true;
        }
        else if(id == R.id.icoSearch){
            searchView.onActionViewExpanded();
        }
        else if (id == R.id.multiple_delete){
            createActionMode();
            return true;
        }
//        else if (id == R.id.addDebt){
//
//        }

        return super.onOptionsItemSelected(item);
    }

    private void showListViewCheckBox(int visible){
        for (int i = 0; i < debts.size(); i++) {
            CheckBox checkBox = (CheckBox) recyclerView.getChildAt(i).findViewById(R.id.checkBox);
            checkBox.setVisibility(visible);
            //Uncheck All Boxes
            checkBox.setChecked(false);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshRecyclerView();

    }

    private void refreshRecyclerView() {
        debts.clear();
        debtDAO.getDebts(debts, debtSUM);
        adapter.notifyDataSetChanged();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else
        if (id == R.id.nav_manage) {
            boolean status = pref.getPasscodeOnOff(user);
            if (status){
                pref.setPasscodeOnOff(status, user);
                Toast.makeText(DebtSummary.this, "Đã Tắt Passcode", Toast.LENGTH_SHORT).show();
            } else {
                pref.setPasscodeOnOff(status, user);
                Toast.makeText(DebtSummary.this, "Đã Mở Passcode", Toast.LENGTH_SHORT).show();
            }
            pref.setLatestUser(user);

        }
//        else if (id == R.id.nav_share) {
//
//        }
//        else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences destroy = getSharedPreferences("Demomode", MODE_PRIVATE);
        int democount = destroy.getInt("Democount" + user, 0);
        if (democount < 4) {
            democount++;
            destroy.edit().putInt("Democount" + user, democount).apply();
            if (democount >= 3) {
                //demo 3 lần, xóa demodata
                for (int i = debts.size() - 1; i >= 0; i--) {
                    if (debts.get(i).getStartDate().equals("Example")) {
                        debtDAO.deleteDebt(i);
                    }
                }
            }
        }

    }

}
