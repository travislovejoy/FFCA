package com.example.travis.fantasycalculator;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
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
import java.util.ListIterator;

/**
 * Created by Travis on 4/13/2016.
 */
public class Scoreboard extends TestDrawer {
    private String TAG = "ScoreBoard Connect";
    private static final String TAG_NAME = "name";
    private static final String TAG_SCORE = "score";
    private String tag_json_arry = "json_array_req";
    private String url = PlayerArray.getInstance().ipAddress;
    private String url_file = "/getplayerstats.php?";
    private ArrayList<HashMap<String, String>> Teams;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.scoreboard, null, false);
        drawer.addView(contentView, 0);

        //Spinner adapter for weeks
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this, R.array.weeks, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        new ArrayList<HashMap<String, String>>();




        updateScoreboard();




    }

    void updateScoreboard() {
        final ArrayList<String> teamNames = PlayerArray.getInstance().keysInArrayList();
        final ListIterator<String> iterator = teamNames.listIterator();
        Teams = new ArrayList<HashMap<String, String>>();
        for(int x=0; x<teamNames.size(); x++) {
            Toast.makeText(Scoreboard.this, iterator.next(),
                    Toast.LENGTH_LONG).show();
            String newURL = url + url_file;
            final String team= teamNames.get(x);
            final int index= x;
            for (int i = 0; i < PlayerArray.getInstance().Size(team, true); i++) {
                newURL = newURL + "name[]=" + PlayerArray.getInstance().getID(i, team) + "&";
            }
            newURL += "week=" + PlayerArray.getInstance().week;

            JsonObjectRequest request = new JsonObjectRequest(newURL,
                    null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {

                    // benchList= new ArrayList<HashMap<String, String>>();
                    Log.d(TAG, response.toString());

                    try {
                        int total = 0;
                        JSONArray ja = response.getJSONArray("players");


                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject player = ja.getJSONObject(i);
                            total += TeamScore.CalculateScore(player, team);

                        }
                        HashMap<String, String> contact = new HashMap<String, String>();
                        contact.put(TAG_NAME, team);
                        contact.put(TAG_SCORE, Integer.toString(total));
                        Teams.add(contact);

                        //Toast.makeText(Scoreboard.this, Teams.get(0).get(TAG_NAME),
                                //Toast.LENGTH_LONG).show();
                        if(index==teamNames.size()-1) {
                            ListAdapter lvAdapter = new SimpleAdapter(
                                    Scoreboard.this, Teams,
                                    R.layout.scores, new String[]{TAG_NAME, TAG_SCORE}, new int[]{R.id.Team, R.id.Score});
                            ListView lv = (ListView) findViewById(R.id.scores);
                            lv.setAdapter(lvAdapter);
                        }



                    } catch (JSONException e) {
                        Toast.makeText(Scoreboard.this, "Could not Connect",
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "Error: " + error.getMessage());

                }
            });
            VolleyController.getInstance().addToRequestQueue(request, tag_json_arry);

        }



    }
}
