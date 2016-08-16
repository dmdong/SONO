package com.manhdong.sono.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.manhdong.sono.R;
import com.manhdong.sono.database.DBContext;
import com.manhdong.sono.database.dao.DebtDAO;
import com.manhdong.sono.model.Debt;

import java.util.List;

/**
 * Created by Saphiro on 7/4/2016.
 */
public class DebtAdapter extends ArrayAdapter<Debt> {

    //Adapter này sử dụng cho ListView, đã không còn dùng nữa, xem DebtAdapterCV

    Context mContext;
    int mResource;
    List<Debt> debts;

    public DebtAdapter(Context context, int resource, List<Debt> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        debts = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.pName = (TextView) convertView.findViewById(R.id.lvpName);
            viewHolder.debt = (TextView) convertView.findViewById(R.id.lvDebt);
            viewHolder.debtTime = (TextView) convertView.findViewById(R.id.debtTime);
//            viewHolder.btnDel = (ImageView) convertView.findViewById(R.id.btnDelete);
//            viewHolder.btnDel.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    DebtDAO deleteDB = new DebtDAO(mContext);
//                    deleteDB.deleteDebt(position);
//                    //debts.remove(position);
//                }
//            });

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.pName.setText(debts.get(position).getPerson());
        if (debts.get(position).getDebtType().equals("MONEY")){

            String debtnote =  mContext.getResources().getString(R.string.debtnote) + debts.get(position).getAmount().toString() + " | Reason: " +debts.get(position).getReason();
            viewHolder.debt.setText(debtnote);
        }
        else viewHolder.debt.setText(debts.get(position).getReason());
        viewHolder.debtTime.setText(debts.get(position).getExpDate());


        return convertView;
    }

    class ViewHolder{
  //      ImageView btnDel;
        TextView pName;
        TextView debt;
        TextView debtTime;
    }
}
