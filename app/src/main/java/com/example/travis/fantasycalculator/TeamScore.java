package com.example.travis.fantasycalculator;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TeamScore extends TestDrawer {

    private ProgressDialog dialog = null;
    private String TAG = "Tutorial Connect";
    private String STARTER = "starter";
    private String BENCH = "bench";
    private String tag_json_arry = "json_array_req";
    private String url = PlayerArray.getInstance().ipAddress;
    private String url_file = "/getplayerstats.php?";
    private static final String TAG_NAME = "name";
    private static final String TAG_POS = "pos";
    private static final String TAG_RPOS = "rpos";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_SCORE = "score";
    private static boolean mFlag= false; //flag set when first player selected to be moved
    private static int mPosition;//position of first player selected to be moved
    private static boolean mStarter;// roster status of first player selected Starter/Bench
    public static boolean editTeamFlag= false;// flag set to true when edit lineup button selected
    private static boolean initialDisplay;


    Button editTeam;

    ArrayList<HashMap<String, String>> starterList, benchList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_main, null, false);
        drawer.addView(contentView, 0);
        initialDisplay= true;
        Intent intent = getIntent();
        boolean moveLayout=intent.getBooleanExtra("moveLayout", false);
        //Spinner adapter for weeks
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this, R.array.weeks, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if (!PlayerArray.getInstance().week.equals(null)) {
            int spinnerPosition = adapter.getPosition(PlayerArray.getInstance().week);
            spinner.setSelection(spinnerPosition);
        }

        //Current team text view
        final TextView tv = (TextView) findViewById(R.id.Team);
        tv.setText(PlayerArray.getInstance().currentTeam);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(!initialDisplay) {
                    update_score();
                }
                else{
                    initialDisplay=false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }


        });


        //Edit Lineup Button Pressed
        editTeam=(Button)findViewById(R.id.editLineup);
        editTeam.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!editTeamFlag) {
                            //Change to move layout player
                            editTeam.setText("Submit");
                            editTeamFlag = true;
                            ListView lv = (ListView) findViewById(R.id.starters);
                            ListView lv2 = (ListView) findViewById(R.id.bench);
                            String currentTeam = PlayerArray.getInstance().currentTeam;
                            set_move_Layout(lv, lv2, PlayerArray.getInstance().Teams.get(currentTeam), "Move");
                            final TextView tv= (TextView) findViewById(R.id.total);
                            tv.setText("");
                        }
                        else{
                            //Return to team score layout
                            update_score();
                            editTeam.setText("Edit Lineup");
                            editTeamFlag=false;
                            mFlag=false;
                        }
                    }
                });

        if(moveLayout){
            editTeam.performClick();
        }
        else {
            update_score();

        }


    }

    String rushingDescription(int rush_yards, int rush_tds, String description, boolean RB) {
        if (rush_yards > 10|| RB) {
            description += Integer.toString(rush_yards) + " RYDS ";
        }
        if (rush_tds >= 1|| RB) {
            description += Integer.toString(rush_tds) + " RTDS ";

        }
        return description;

    }
    String receivingDescription(int rec_yards, int rec_tds, int receptions, String description, boolean WR) {
        if (rec_yards > 10|| WR) {
            description += Integer.toString(rec_yards) + " RecYDS ";
        }
        if (rec_tds >= 1|| WR) {
            description += Integer.toString(rec_tds) + " RecTDS ";

        }
        if(receptions>3||WR) {
            description+= Integer.toString(receptions)+ " REC ";
        }
        return description;
    }

    String passingDescription(int pass_yards, int pass_tds, String description, boolean QB) {
        if(pass_yards>25||QB) {
            description += Integer.toString(pass_yards) + " PYDS ";
        }
        if (pass_tds >= 1||QB) {
            description += Integer.toString(pass_tds) + " PTDS ";
        }
        return description;
    }
    private String getDescription(JSONObject player, String position){
        try {
            int pass_yards = player.getInt("pass_yards");
            int pass_tds = player.getInt("pass_tds");
            int rush_yards = player.getInt("rush_yards");
            int rush_tds = player.getInt("rush_tds");
            int rec_yards = player.getInt("rec_yards");
            int rec_tds = player.getInt("rec_tds");
            int rec = player.getInt("rec");
            if (position.equals("QB")) {
                String description="";
                description= passingDescription(pass_yards, pass_tds, description, true);
                description= rushingDescription(rush_yards, rush_tds, description, false);
                return description;
            }
            else if(position.equals("RB")) {
                String description="";
                description= passingDescription(pass_yards, pass_tds, description, false);
                description= rushingDescription(rush_yards, rush_tds, description, true);
                description= receivingDescription(rec_yards,rec_tds, rec, description, false);
                return description;
            }
            else if(position.equals("WR")||position.equals("TE")) {
                String description="";
                description= passingDescription(pass_yards, pass_tds, description, false);
                description= rushingDescription(rush_yards, rush_tds, description, false);
                description= receivingDescription(rec_yards,rec_tds, rec, description, true);
                return description;
            }
            else if(position.equals("Flex")) {
                String description="";
                description= passingDescription(pass_yards, pass_tds, description, false);
                description= rushingDescription(rush_yards, rush_tds, description, false);
                description= receivingDescription(rec_yards,rec_tds, rec, description, false);
                return description;
            }

        }
        catch(JSONException e) {
            e.printStackTrace();

        }
        return "";
    }

    private int CalculateScore(JSONObject player, String team) {
        try {
            int pass_yards = player.getInt("pass_yards");
            int pass_tds = player.getInt("pass_tds");
            int rush_yards = player.getInt("rush_yards");
            int rush_tds = player.getInt("rush_tds");
            int rec_yards = player.getInt("rec_yards");
            int rec_tds = player.getInt("rec_tds");
            int rec = player.getInt("rec");
            Team x= PlayerArray.getInstance().getTeam(team) ;
            int pass_score = pass_yards / x.PY + pass_tds * x.PTd;
            int rush_score = rush_yards / x.RY + rush_tds * x.RTD;
            int rec_score = rec_yards / x.RecY + rec_tds * x.RecTD + rec * 1;
            int score = pass_score + rush_score + rec_score;
            return score;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private void update_score() {

        dialog = new ProgressDialog(this);

        dialog.setMessage("Loading...");
        dialog.show();
        HashMap<String, String> params = new HashMap<String, String>();
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);

        final String week = spinner.getSelectedItem().toString();
        PlayerArray.getInstance().week = week;
        final String team = PlayerArray.getInstance().currentTeam;

        String starter_url = url + url_file;
        for (int i = 0; i < PlayerArray.getInstance().Size(team, true); i++) {
            starter_url = starter_url + "name[]=" + PlayerArray.getInstance().getID(i, team) + "&";
        }
        for (int i = 0; i < PlayerArray.getInstance().Size(team, false); i++) {
            starter_url = starter_url + "name[]=" + PlayerArray.getInstance().getBenchID(i, team) + "&";
        }
        starter_url += "week=" + week;
        get_Data(starter_url, team, PlayerArray.getInstance().Size(team, true));

        dialog.dismiss();

    }


    void get_Data(String new_url, final String team, final int starterSize) {
        JsonObjectRequest request = new JsonObjectRequest(new_url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //ArrayList<HashMap<String, String>> list;
                starterList = new ArrayList<HashMap<String, String>>();
                benchList= new ArrayList<HashMap<String, String>>();
                Log.d(TAG, response.toString());

                try {
                    JSONArray ja = response.getJSONArray("players");

                        int total = 0;

                    for (int i = 0; i < ja.length(); i++) {
                        HashMap<String, String> contact = new HashMap<String, String>();
                        JSONObject jobj = ja.getJSONObject(i);
                        String name = jobj.getString("name");
                        //String pos=jobj.getString("pos");
                        contact.put(TAG_RPOS,PlayerArray.getInstance().getpos(i,team));
                        if(i<starterSize) {
                            String pos = PlayerArray.getInstance().getpos(i, team, true);
                            contact.put(TAG_POS, pos);
                            String description = getDescription(jobj, pos);
                            contact.put(TAG_DESCRIPTION, description);
                        }
                        else{
                            String pos = PlayerArray.getInstance().getpos(i-starterSize, team, false);
                            contact.put(TAG_POS, pos);
                            String description= "";
                            contact.put(TAG_DESCRIPTION, description);

                        }
                        //String description = getDescription(jobj);
                        int score = CalculateScore(jobj, team);
                        String scores = Integer.toString(score);
                        contact.put(TAG_NAME, name);

                        //contact.put(TAG_DESCRIPTION, description);
                        contact.put(TAG_SCORE, scores);

                        if(i<starterSize){
                            total += score;
                            starterList.add(contact);
                        }
                        else {
                            benchList.add(contact);

                        }

                    }
                    TextView text = (TextView) findViewById(R.id.total);
                    text.setText("Total   " + total);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(TeamScore.this, "Could not Connect",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
                ListAdapter adapter = new SimpleAdapter(
                       TeamScore.this, starterList,
                        R.layout.list_item, new String[]{TAG_NAME, TAG_RPOS, TAG_DESCRIPTION, TAG_SCORE}, new int[]{R.id.name,
                        R.id.pos, R.id.description, R.id.points});
                ListView lv = (ListView) findViewById(R.id.starters);
               lv.setAdapter(adapter);
                ListUtils.setDynamicHeight(lv);

                ListAdapter  adapter2= new SimpleAdapter(
                        TeamScore.this, benchList,
                        R.layout.list_item, new String[]{TAG_NAME, TAG_RPOS, TAG_DESCRIPTION, TAG_SCORE}, new int[]{R.id.name,
                        R.id.pos, R.id.description, R.id.points});
                ListView lv2 = (ListView) findViewById(R.id.bench);
                lv2.setAdapter(adapter2);
                ListUtils.setDynamicHeight(lv2);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
                dialog.dismiss();
            }
        });


        VolleyController.getInstance().addToRequestQueue(request, tag_json_arry);

    }

    public  void set_move_Layout(ListView lv, ListView lv2, Team team, String status ){

        ListViewAdapter adapter = new ListViewAdapter(TeamScore.this, team, !mFlag, true, lv, lv2, mPosition, mStarter);
        lv.setAdapter(adapter);
        ListUtils.setDynamicHeight(lv);


        ListViewAdapter adapter2 = new ListViewAdapter(TeamScore.this, team, !mFlag, false, lv, lv2, mPosition, mStarter);
        lv2.setAdapter(adapter2);
        ListUtils.setDynamicHeight(lv2);
    }

    public static void movePlayer(int position,boolean Starter, Activity activity, ListView lv, ListView lv2){
        ListViewAdapter adapter, adapter2;
        if (!mFlag) {
            //First Player selected: Set Flags and record position, then reset the move player layout
            mFlag = true;
            boolean fullBench= true;
            mPosition = position;
            mStarter=Starter;


            String currentTeam=PlayerArray.getInstance().currentTeam;
            Team team= PlayerArray.getInstance().Teams.get(currentTeam);

            //if Bench is full add a blank slot to make it easier to set lineup

            for(int i=0;i<team.Bench.size(); i++) {
                if(team.Bench.get(i).id==null){
                    fullBench=false;
                }
            }

            if(fullBench) {
                PlayerArray.getInstance().addBench("", null, "", "", currentTeam);
            }
            adapter= new ListViewAdapter(activity, team , !mFlag, true, lv, lv2, mPosition, mStarter);
            adapter2= new ListViewAdapter(activity, team, !mFlag, false, lv, lv2, mPosition, mStarter);

            lv.setAdapter(adapter);
            ListUtils.setDynamicHeight(lv);
            lv2.setAdapter(adapter2);
            ListUtils.setDynamicHeight(lv2);
        }
        else{
            mFlag=false;
            //Second Player selected: Make Lineup Change
            PlayerArray.getInstance().swap(mPosition, position, mStarter, Starter);




            String currentTeam=PlayerArray.getInstance().currentTeam;
            Team team= PlayerArray.getInstance().Teams.get(currentTeam);

            //Get rid of extra empty bench space that was previously added
            if(team.Bench.size()>team.benchSize) {
                for (int i = 0; i < team.Bench.size(); i++) {
                    if(team.Bench.get(i).id==null){
                        team.Bench.remove(i);
                    }
                }
            }

            //Reset Move layout
            adapter= new ListViewAdapter(activity, PlayerArray.getInstance().Teams.get(currentTeam), !mFlag, true, lv, lv2, mPosition, mStarter);
            adapter2= new ListViewAdapter(activity, PlayerArray.getInstance().Teams.get(currentTeam), !mFlag, false, lv, lv2, mPosition, mStarter);
            lv.setAdapter(adapter);
            ListUtils.setDynamicHeight(lv);
            lv2.setAdapter(adapter2);
            ListUtils.setDynamicHeight(lv2);
            }



    }


}


