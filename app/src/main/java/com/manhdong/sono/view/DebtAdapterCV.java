package com.manhdong.sono.view;

import android.content.Context;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.manhdong.sono.R;
import com.manhdong.sono.model.Debt;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saphiro on 8/8/2016.
 */
public class DebtAdapterCV extends RecyclerView.Adapter<DebtAdapterCV.ViewHolder> implements Filterable{

    Context mContext;
    int mResource;
    List<Debt> debts; //Data gốc
    List<Debt> datadebt;

    //Phần này xử lý lấy position cho itemview
//    private int position;
//    public int getPosition() {
//        return position;
//    }
//    public void setPosition(int position) {
//        this.position = position;
//    }

    public DebtAdapterCV(List<Debt> debts, Context mContext, int mResource) {
        this.debts = debts;
        this.mContext = mContext;
        this.mResource = mResource;
        datadebt = new ArrayList<>(debts);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(mResource, parent,false);
        ViewHolder myVH = new ViewHolder(view);
        return myVH;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.pName.setText(debts.get(position).getPerson());
        if (debts.get(position).getDebtType().equals("MONEY")){

            String debtnote =  mContext.getResources().getString(R.string.debtnote)
                    + debts.get(position).getAmount().toString()
                    + " | Lí do: " +debts.get(position).getReason();

            viewHolder.debt.setText(debtnote);
        }
        else viewHolder.debt.setText(debts.get(position).getReason());
        viewHolder.debtTime.setText(debts.get(position).getExpDate());
        //Xử lý cho context menu
//        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                setPosition(viewHolder.getAdapterPosition());
//                return false;
//            }
//        });
//        viewHolder.itemView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
////                View holder = recyclerView.findChildViewUnder(event.getX(), event.getY());
//                //int position2 = viewHolder.getAdapterPosition();
//                setPosition(viewHolder.getAdapterPosition());
//                return false;
//            }
//        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView pName;
        TextView debt;
        TextView debtTime;
        CardView item;

        public ViewHolder(final View itemView) {
            super(itemView);
            item = (CardView) itemView;
            pName = (TextView) itemView.findViewById(R.id.lvpName);
            debt = (TextView) itemView.findViewById(R.id.lvDebt);
            debtTime = (TextView) itemView.findViewById(R.id.debtTime);
//            itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
//                @Override
//                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//                }
//            });
        }
//        @Override
//        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
////            menu.add(Menu.NONE, R.menu.edit_menu, Menu.NONE, "menu");
////            getMenuInflater().inflate(R.menu.edit_menu, menu);
//
//        }
    }

    //Tạo Filter cho Options Menu Search lấy từ amdroid.widget.Filterable
    @Override
    public int getItemCount() {
        return debts.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResult = new FilterResults();
                List<Debt> temp = new ArrayList<>();
                if (constraint != null){
                    String input = constraint.toString().toUpperCase();
                    input = normalizeText(input);
                    for (int i = 0; i < datadebt.size(); i++) {
                        String debtText = datadebt.get(i).toString();
                        debtText = normalizeText(debtText);
                        if(debtText.contains(input)){
                            temp.add(datadebt.get(i));
                        }
                    }
                }
                filterResult.values = temp;
                filterResult.count = temp.size();

                return filterResult;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                List<Debt> temp = new ArrayList<>();
                temp = (List<Debt>) results.values;
                if(results != null && results.count>0){
                    debts.clear();
                    debts.addAll(temp);
                }
                else {
                    if (constraint!= null){
                        Toast.makeText(mContext, "Không tìm thấy kết quả", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        };
    }
    public String normalizeText(String input){
        input = Normalizer.normalize(input, Normalizer.Form.NFD);
        input = input.replaceAll("\\p{M}", "");
        input = input.replaceAll("[đ|Đ]", "D");
        return input;
    }



}
