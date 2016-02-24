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
    private String url = "http://192.168.0.11";
    private String url_file = "/getplayerstats.php?";
    private static final String TAG_NAME = "name";
    private static final String TAG_POS = "pos";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_SCORE = "score";
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_main, null, false);
        drawer.addView(contentView, 0);

        Intent intent = getIntent();
        //final String teamName= intent.getStringExtra("teamName");
        //final String week= intent.getStringExtra("week");

        Set<String> keys = PlayerArray.getInstance().getKeys();
        String[] keyArray = keys.toArray(new String[keys.size()]);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this, R.array.weeks, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if (!PlayerArray.getInstance().week.equals(null)) {
            int spinnerPosition = adapter.getPosition(PlayerArray.getInstance().week);
            spinner.setSelection(spinnerPosition);
        }

        final TextView tv = (TextView) findViewById(R.id.Team);
        tv.setText("Team Name: "+ PlayerArray.getInstance().currentTeam);


           /* final Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item,keyArray);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter2);

            if (!PlayerArray.getInstance().currentTeam.equals(null)) {
                int spinnerPosition = adapter2.getPosition(PlayerArray.getInstance().currentTeam);
                spinner2.setSelection(spinnerPosition);
            }*/

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                update_score();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });




        update_score();


    }

    private int CalculateScore(JSONObject player) {
        try {
            int pass_yards = player.getInt("pass_yards");
            int pass_tds = player.getInt("pass_tds");
            int rush_yards = player.getInt("rush_yards");
            int rush_tds = player.getInt("rush_tds");
            int rec_yards = player.getInt("rec_yards");
            int rec_tds = player.getInt("rec_tds");
            int rec = player.getInt("rec");
            int pass_score = pass_yards / 25 + pass_tds * 4;
            int rush_score = rush_yards / 10 + rush_tds * 6;
            int rec_score = rec_yards / 10 + rec_tds * 6 + rec * 1;
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
        //final Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        final String week = spinner.getSelectedItem().toString();
        PlayerArray.getInstance().week = week;
        final String team = PlayerArray.getInstance().currentTeam;
        //PlayerArray.getInstance().currentTeam=team;
        String starter_url = url + url_file;
        for (int i = 0; i < PlayerArray.getInstance().Size(team, STARTER); i++) {
            starter_url = starter_url + "name[]=" + PlayerArray.getInstance().getID(i, team) + "&";
        }
        starter_url += "week=" + week;
        ListView lv = (ListView) findViewById(R.id.starters);
        get_Data(starter_url, team, lv, STARTER);


        String bench_url = url + url_file;
        for (int i = 0; i < PlayerArray.getInstance().Size(team, BENCH); i++) {
            bench_url = bench_url + "name[]=" + PlayerArray.getInstance().getBenchID(i, team) + "&";
        }
        bench_url += "week=" + week;
        lv = (ListView) findViewById(R.id.bench);
        get_Data(bench_url, team, lv, BENCH);
        dialog.dismiss();

    }


    void get_Data(String new_url, final String team, final ListView lv, final String type) {
        JsonObjectRequest request = new JsonObjectRequest(new_url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                ArrayList<HashMap<String, String>> list;
                list = new ArrayList<HashMap<String, String>>();
                Log.d(TAG, response.toString());

                try {
                    JSONArray ja = response.getJSONArray("players");

                        int total = 0;

                    for (int i = 0; i < ja.length(); i++) {
                        HashMap<String, String> contact = new HashMap<String, String>();
                        JSONObject jobj = ja.getJSONObject(i);
                        String name = jobj.getString("name");
                        //String pos=jobj.getString("pos");
                        String pos = PlayerArray.getInstance().getpos(i, team, type);
                        int score = CalculateScore(jobj);
                        if(type.equals(STARTER)){
                            total += score;
                            }
                        String description = "Not yet available";
                        String scores = Integer.toString(score);
                        contact.put(TAG_NAME, name);
                        contact.put(TAG_POS, pos);
                        contact.put(TAG_DESCRIPTION, description);
                        contact.put(TAG_SCORE, scores);
                        list.add(contact);

                    }
                    TextView text = (TextView) findViewById(R.id.total);
                    if(type.equals(STARTER)) {
                        text.setText("total   " + total);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                ListAdapter adapter = new SimpleAdapter(
                        TeamScore.this, list,
                        R.layout.list_item, new String[]{TAG_NAME, TAG_POS, TAG_DESCRIPTION, TAG_SCORE}, new int[]{R.id.name,
                        R.id.pos, R.id.description, R.id.points});
                //ListView lv = (ListView) findViewById(android.R.id.starters);
                lv.setAdapter(adapter);


                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {

                        String item = Integer.toString(position);
                        HashMap<String, String> player = (HashMap) parent.getItemAtPosition(position);
                        String getPos = player.get(TAG_POS);

                        //Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();
                        Intent myIntent = new Intent(TeamScore.this, AddPlayer.class);
                        myIntent.putExtra("pos", position); //Optional parameters
                        myIntent.putExtra("teamName", team);
                        myIntent.putExtra("position", getPos);
                        myIntent.putExtra("type", type);
                        //myIntent.putExtra("week", week);
                        TeamScore.this.startActivity(myIntent);

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


        VolleyController.getInstance().addToRequestQueue(request, tag_json_arry);

    }


}