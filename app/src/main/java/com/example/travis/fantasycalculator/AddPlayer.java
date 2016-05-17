package com.example.travis.fantasycalculator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AddPlayer extends Activity {

    private ProgressDialog dialog = null;
    private String TAG="Connect";
    private String tag_json_array = "json_array_req";
    private String URL = PlayerArray.getInstance().ipAddress+"/getplayerinfo.php?";
    private static final String TAG_NAME = "name";
    private static final String TAG_ID = "id";
    private static final String TAG_POS = "pos";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_TEAM = "team";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_player);
        Intent intent = getIntent();
        final Integer value = intent.getIntExtra("pos", 0);
        final String teamName= intent.getStringExtra("teamName");
        final String type= intent.getStringExtra("type");
        final String pos= intent.getStringExtra("position");
        final Spinner teamSpinner = (Spinner) findViewById(R.id.teamSpinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this, R.array.TEAMS, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamSpinner.setAdapter(adapter);

        teamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                update_player_list(value, teamName, pos, type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        update_player_list(value, teamName, pos,type);
    }

    private void update_player_list(final int value, final String teamName, String pos, final String type) {
        dialog = new ProgressDialog(this);



        dialog.setMessage("Loading...");
        dialog.show();

        final Spinner teamSpinner = (Spinner) findViewById(R.id.teamSpinner);
        String team = teamSpinner.getSelectedItem().toString();
        String players="";

        //final Spinner posSpinner = (Spinner) findViewById(R.id.positionSpinner);
        //String pos = posSpinner.getSelectedItem().toString();

        if(team.equals("ALL")){
            team="'*'";
        }

        if(pos.equals("Flex")||pos.equals("Bench")){
            pos="'QB','WR','TE','RB'";
        }
        else{
            pos="'"+pos+"'";
        }
        for (int i = 0; i < PlayerArray.getInstance().Size(teamName, true); i++) {
            players += "'"+PlayerArray.getInstance().getID(i, teamName) + "'";
            players+=",";
        }
        for (int i = 0; i < PlayerArray.getInstance().Size(teamName, false); i++) {
            players += "'"+PlayerArray.getInstance().getBenchID(i, teamName) + "'";
            if(i<PlayerArray.getInstance().Size(teamName,false)-1){
                players+=",";
            }

        }
        String new_URL= URL+ "id="+players;
        new_URL+= "&team="+ team;

        new_URL+="&pos="+pos;



        JsonObjectRequest request = new JsonObjectRequest(new_URL,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                ArrayList<HashMap<String, String>> list;
                list = new ArrayList<HashMap<String, String>>();
                Log.d(TAG, response.toString());

                try {
                    JSONArray ja = response.getJSONArray("players");
                   // int total = 0;
                    for (int i = 0; i < ja.length(); i++) {
                        HashMap<String, String> contact = new HashMap<String, String>();
                        JSONObject jobj = ja.getJSONObject(i);
                        String name = jobj.getString("name");
                        String id= jobj.getString("GSID");
                        String pos = jobj.getString("pos");
                        String team = jobj.getString("team");
                        String description= "Not yet available";
                        contact.put(TAG_NAME, name);
                        contact.put(TAG_ID, id);
                        contact.put(TAG_POS, pos);
                        contact.put(TAG_DESCRIPTION, description);
                        contact.put(TAG_TEAM, team);
                        list.add(contact);
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                dialog.dismiss();
                ListAdapter adapter = new SimpleAdapter(
                        AddPlayer.this, list,
                        R.layout.player_info, new String[] { TAG_NAME, TAG_POS, TAG_DESCRIPTION, TAG_TEAM}, new int[] { R.id.name,
                        R.id.pos, R.id.description, R.id.team});
                ListView lv = (ListView) findViewById(android.R.id.list);
                lv.setAdapter(adapter);


                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {

                        //String item = Integer.toString(position);
                        HashMap<String, String> player = (HashMap) parent.getItemAtPosition(position);
                        //item += " " + player.get(TAG_ID);
                        PlayerArray.getInstance().SwapId(value, player.get(TAG_ID), player.get(TAG_NAME), player.get(TAG_TEAM), teamName, type, player.get(TAG_POS));

                        //Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();

                        Intent myIntent = new Intent(AddPlayer.this, TeamScore.class);
                        myIntent.putExtra("moveLayout", true);
                        AddPlayer.this.startActivity(myIntent);

                    }
                });



                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error: " + error.getMessage());
                        dialog.dismiss();
                    }
                });

        VolleyController.getInstance().addToRequestQueue(request, tag_json_array);
    }
}