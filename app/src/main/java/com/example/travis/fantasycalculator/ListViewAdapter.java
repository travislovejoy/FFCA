package com.example.travis.fantasycalculator;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Travis on 3/2/2016.
 */
public class ListViewAdapter extends BaseAdapter {
    public ArrayList<HashMap<String, String>> list;
    Activity activity;

    public ListViewAdapter(Activity activity,
                           ArrayList<HashMap<String, String>> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    class ViewHolder {
        Button btn_fav, btn_plus, btn_minus;
        TextView name;


    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        // TODO Auto-generated method stub

        // TODO Auto-generated method stub
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.move_player, null);
            holder = new ViewHolder();

            holder.btn_fav = (Button) convertView.findViewById(R.id.move);


            holder.name = (TextView) convertView.findViewById(R.id.name);



            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HashMap<String, String> map = list.get(position);
       // holder.item_name.setText(map.get(ITEM_NAME_COLUMN));
       // holder.item_price.setText(map.get(ITEM_PRICE_COLUMN));
       // holder.item_total.setText(map.get(TOTAL_COLUMN));
       // holder.et_quantity.setText(map.get(ITEM_QUANTITY_COLUMN));

        return convertView;
    }

}