package com.example.travis.fantasycalculator;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Travis on 3/2/2016.
 *
 *
 */


public class ListViewAdapter extends BaseAdapter {
    //public ArrayList<HashMap<String, String>> list;
    Team team;
    Activity activity;
    private static final String TAG_NAME = "name";
    private static final String TAG_POS = "pos";
    private static final String TAG_RPOS = "rpos";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_SCORE = "score";
    ListView lv, lv2;
    private boolean flag;
    private boolean starter;


    public ListViewAdapter(Activity activity,
                           Team team, boolean flag, boolean starter, ListView lv, ListView lv2) {
        super();
        this.activity = activity;
        this.team = team;
        this.flag= flag;
        this.starter=starter;
        this.lv=lv;
        this.lv2=lv2;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if(starter) {
            return team.Starters.size();
        }
        else{
            return team.Bench.size();
        }
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        if(starter) {
            return team.Starters.get(position);
        }
        else{
            return team.Bench.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    class ViewHolder {
        Button move;
        TextView name, pos;


    }

    @Override
    public View getView(final int pos, View convertView, final ViewGroup parent) {
        // TODO Auto-generated method stub

        // TODO Auto-generated method stub
        final ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.move_player, null);
            holder = new ViewHolder();

            holder.move = (Button) convertView.findViewById(R.id.move);
            if(!flag) {
                holder.move.setText("Here");
            }
            else{
                holder.move.setText("Move");
            }


            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.pos= (TextView) convertView.findViewById(R.id.pos);

            holder.move.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //holder.move.setText("Here");
                    //holder.move.setText("");
                    TeamScore.movePlayer(pos, starter, activity, lv, lv2);






                }
            });



            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

       // HashMap<String, String> map = list.get(position);
       //holder.name.setText(map.get(TAG_NAME));
       //holder.pos.setText(map.get(TAG_RPOS));
        if(starter) {
            holder.name.setText(team.Starters.get(pos).name);
            holder.pos.setText(team.Roster.get(pos));
        }
        else{
            holder.name.setText(team.Bench.get(pos).name);
            holder.pos.setText("Bench");
        }
       // holder.item_total.setText(map.get(TOTAL_COLUMN));
       // holder.et_quantity.setText(map.get(ITEM_QUANTITY_COLUMN));

        return convertView;
    }

}