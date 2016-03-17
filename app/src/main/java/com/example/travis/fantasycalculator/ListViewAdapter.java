package com.example.travis.fantasycalculator;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



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
    private boolean starter, mStarter;
    private int mPosition;


    public ListViewAdapter(Activity activity,
                           Team team, boolean flag, boolean starter, ListView lv, ListView lv2, int mPostion, boolean mStarter) {
        super();
        this.activity = activity;
        this.team = team;
        this.flag= flag;
        this.starter=starter;
        this.lv=lv;
        this.lv2=lv2;
        this.mPosition=mPostion;
        this.mStarter=mStarter;


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
            if(flag) {
                //if flag is true set the move button text to "Move" and set ON Click listener
                //for all buttons
                holder.move.setText("Move");
                holder.move.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        TeamScore.movePlayer(pos, starter, activity, lv, lv2);
                    }
                });
            }
            else{
                //if flag is false check each position to see if it is a valid move. If move is valid
                //set button text to "Here" and and Color to green. Also set onclick listener.
                //If position does not pass test, set text to blank and don't set onclick listener
                String P1= PlayerArray.getInstance().getpos(mPosition,PlayerArray.getInstance().currentTeam, mStarter);
                String P2= PlayerArray.getInstance().getpos(pos, PlayerArray.getInstance().currentTeam, starter);
                String R1= PlayerArray.getInstance().getRpos(mPosition, PlayerArray.getInstance().currentTeam, mStarter);
                String R2= PlayerArray.getInstance().getRpos(pos, PlayerArray.getInstance().currentTeam, starter);
                boolean check= PlayerArray.getInstance().checkPos(P1, P2, R1, R2, PlayerArray.getInstance().currentTeam );
                if(check) {
                    holder.move.setText("Here");
                    holder.move.getBackground().setColorFilter(0xFF097A22, PorterDuff.Mode.MULTIPLY);
                    holder.move.setTextColor(Color.WHITE);
                    holder.move.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            TeamScore.movePlayer(pos, starter, activity, lv, lv2);
                        }
                    });
                }
                else{
                    holder.move.setText("");
                }
            }


            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.pos= (TextView) convertView.findViewById(R.id.pos);
            holder.position=(TextView) convertView.findViewById(R.id.position);


            holder.add.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //ONClick listener for add button, sends you to add player class if you are not
                    //over max Roster size
                    Intent myIntent = new Intent(activity, AddPlayer.class);
                    if(starter) {
                        if(team.Starters.get(pos).id==null&& team.RosterSize>= team.maxRosterSize){
                            Toast.makeText(activity,"Too many players on roster",Toast.LENGTH_LONG).show();
                            return;
                        }
                        else {

                            myIntent.putExtra("type", "starter");
                            myIntent.putExtra("position", team.Roster.get(pos));
                        }
                    }
                    else{
                        if(team.Bench.get(pos).id==null&& team.RosterSize>= team.maxRosterSize){
                            Toast.makeText(activity,"Too many players on roster",Toast.LENGTH_LONG).show();
                            return;
                        }
                        else {
                            myIntent.putExtra("position", "Bench");
                            myIntent.putExtra("type", "Bench");
                        }

                    }

                    myIntent.putExtra("pos", pos); //Optional parameters
                    myIntent.putExtra("teamName", PlayerArray.getInstance().currentTeam);


                    TeamScore.editTeamFlag=false;
                    //ListViewAdapter.this.startActivity(myIntent);
                    activity.startActivity(myIntent);
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