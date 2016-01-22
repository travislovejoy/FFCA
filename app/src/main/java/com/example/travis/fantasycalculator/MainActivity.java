package com.example.travis.fantasycalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private ProgressDialog dialog=null ;
    private String TAG="Tutorial Connect";
    private String tag_json_arry = "json_array_req";
    private String url = "http://192.168.0.3";
    private String url_file="/getplayerstats.php?";
    private static final String TAG_NAME = "name";
    private static final String TAG_POS = "pos";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_SCORE = "score";
    //public String[] names= {"00-0024334", "00-0027948" };
    //private String[] post= {"WR", "RB" };
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       //list=(ListView) findViewById(R.id.listView1);
       // rowdata=new ArrayList<RowData>();
        //ArrayList<HashMap<String, String>> list;
        //int size= PlayerArray.getInstance().Size();
        //Toast.makeText(getBaseContext(), Integer.toString(size), Toast.LENGTH_LONG).show();

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this, R.array.weeks, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        b1=(Button)findViewById(R.id.submit);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_score();
            }
        });
        //ArrayList<HashMap<String, String>> list;
        //list= new ArrayList<HashMap<String, String>>();

        update_score();


    }

    private int CalculateScore(JSONObject player){
        try{
           int pass_yards= player.getInt("pass_yards");
            int pass_tds=player.getInt("pass_tds");
            int rush_yards= player.getInt("rush_yards");
            int rush_tds=player.getInt("rush_tds");
            int rec_yards= player.getInt("rec_yards");
            int rec_tds= player.getInt("rec_tds");
            int rec= player.getInt("rec");
            int pass_score= pass_yards/25+ pass_tds*4;
            int rush_score= rush_yards/10+ rush_tds*6;
            int rec_score= rec_yards/10+rec_tds*6+rec*1;
            int score= pass_score+rush_score+rec_score;
            return score;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }

   private void update_score(){

       dialog= new ProgressDialog(this);

       dialog.setMessage("Loading...");
       dialog.show();

       HashMap<String, String> params = new HashMap<String, String>();
       final Spinner spinner = (Spinner) findViewById(R.id.spinner);
       String week = spinner.getSelectedItem().toString();
       String new_url=url+url_file;  //+"?name="+name;
       for(int i=0; i<PlayerArray.getInstance().Size(); i++){
           Toast.makeText(getBaseContext(), PlayerArray.getInstance().getID(i), Toast.LENGTH_LONG).show();
           new_url = new_url + "name[]=" + PlayerArray.getInstance().getID(i)+"&";
       }
       new_url+="week="+week;
       JsonObjectRequest request = new JsonObjectRequest(new_url,
               null, new Response.Listener<JSONObject>() {

           @Override
           public void onResponse(JSONObject response) {
               ArrayList<HashMap<String, String>> list;
               list= new ArrayList<HashMap<String, String>>();
               Log.d(TAG, response.toString());

               try {
                   JSONArray ja = response.getJSONArray("players");
                   int total=0;
                   for(int i=0;i<ja.length();i++){
                       HashMap<String, String> contact = new HashMap<String, String>();
                       JSONObject jobj = ja.getJSONObject(i);
                       String name=jobj.getString("name");
                       String pos=jobj.getString("pos");
                      //int pass_yards= jobj.getInt("pass_yards");
                       int score= CalculateScore(jobj);
                       total+=score;
                       //int score= 10;
                       String description= "Not yet available";
                       String scores= Integer.toString(score);
                       contact.put(TAG_NAME, name);
                       contact.put(TAG_POS, pos);
                       contact.put(TAG_DESCRIPTION, description);
                       contact.put(TAG_SCORE, scores);
                       list.add(contact);
                       //String img_url;
                       //String combo = name+ " "+description;

                       //TextView tv1 = (TextView)findViewById(R.id.textveiw);
                       //tv1.setText(combo);

                       //img_url = response.getJSONObject(i).getString("img_url");

                       // rowdata.add(new RowData(title, description));
                   }
                   TextView text = (TextView) findViewById(R.id.total);
                   text.setText("total   "+ total);
               }

               catch (JSONException e) {
                   // TODO Auto-generated catch block
                   e.printStackTrace();
               }
               //adapter=new CustomAdapter(MainActivity.this, rowdata);
               // list.setAdapter(adapter);
               ListAdapter adapter = new SimpleAdapter(
                       MainActivity.this, list,
                       R.layout.list_item, new String[] { TAG_NAME, TAG_POS, TAG_DESCRIPTION, TAG_SCORE}, new int[] { R.id.name,
                       R.id.pos, R.id.description, R.id.points});
               ListView lv = (ListView) findViewById(android.R.id.list);
               lv.setAdapter(adapter);
               // setListAdapter(adapter);

               lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                   @Override
                   public void onItemClick(AdapterView<?> parent, View view, int position,
                                           long id) {

                       String item = Integer.toString(position);
                       HashMap<String, String> player= (HashMap)parent.getItemAtPosition(position);
                       item += " "+player.get(TAG_NAME);

                       //Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();
                       Intent myIntent = new Intent(MainActivity.this, AddPlayer.class);
                       myIntent.putExtra("pos", position); //Optional parameters
                       MainActivity.this.startActivity(myIntent);

                   }
               });


               dialog.dismiss();

           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               Log.d(TAG, "Error: " + error.getMessage());
               dialog.dismiss();
           }
       });

       // Adding request to request queue
       VolleyController.getInstance().addToRequestQueue(request, tag_json_arry);
   }
}