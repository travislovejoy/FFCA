package com.example.travis.fantasycalculator;

import android.app.Activity;
import android.content.Intent;
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
        Button move, add;
        TextView name, pos, position;


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

            holder.add= (Button) convertView.findViewById(R.id.addPlayer);
            holder.move = (Button) convertView.findViewById(R.id.move);
            if(!flag) {
                holder.move.setText("Here");
            }
            else{
                holder.move.setText("Move");
            }


            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.pos= (TextView) convertView.findViewById(R.id.pos);
            holder.position=(TextView) convertView.findViewById(R.id.position);


            holder.add.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent myIntent = new Intent(activity, AddPlayer.class);
                    myIntent.putExtra("pos", pos); //Optional parameters
                    myIntent.putExtra("teamName", PlayerArray.getInstance().currentTeam);
                    myIntent.putExtra("position", team.Roster.get(pos));
                   if(starter) {
                       myIntent.putExtra("type", "starter" );
                   }
                    else{
                       myIntent.putExtra("type", "Bench");
                   }
                    TeamScore.editTeamFlag=false;
                    //ListViewAdapter.this.startActivity(myIntent);
                    activity.startActivity(myIntent);
                }
            });

            holder.move.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    TeamScore.movePlayer(pos, starter, activity, lv, lv2);
                }
            });



            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if(starter) {
            holder.name.setText(team.Starters.get(pos).name);
            holder.pos.setText(team.Roster.get(pos));
            holder.position.setText(", "+team.Starters.get(pos).position+" "+team.Starters.get(pos).team);
        }
        else{
            holder.name.setText(team.Bench.get(pos).name);
            holder.pos.setText("Bench");
            holder.position.setText(", "+team.Bench.get(pos).position+" "+team.Bench.get(pos).team);
        }


        return convertView;
    }

}