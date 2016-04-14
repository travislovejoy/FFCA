package com.example.travis.fantasycalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class TestDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    protected DrawerLayout drawer;
    private ArrayList<String> navDrawer;
    private ArrayAdapter mAdapter;
    private ListView mDrawerList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_drawer);




        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        String[] osArray = { "Team Score", "Create Team", "DeleteTeam" };
        navDrawer= PlayerArray.getInstance().keysInArrayList();
        navDrawer.add("Create Team");
        navDrawer.add("Delete Team");
        navDrawer.add("ScoreBoard");
        mDrawerList = (ListView)findViewById(R.id.navList);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navDrawer);
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                setActivity(position);
            }
        });
        mDrawerList.setAdapter(mAdapter);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.test_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void updateDrawer() {
        navDrawer= PlayerArray.getInstance().keysInArrayList();
        navDrawer.add("Create Team");
        navDrawer.add("Delete Team");
        navDrawer.add("ScoreBoard");
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navDrawer));

    }

    private void setActivity(int position){
        if (position-PlayerArray.getInstance().numberOfTeams()<0){
            PlayerArray.getInstance().currentTeam= navDrawer.get(position);
            Intent myIntent = new Intent(TestDrawer.this, TeamScore.class);
            TestDrawer.this.startActivity(myIntent);
        }
        if (position-PlayerArray.getInstance().numberOfTeams()==0){
            Intent myIntent = new Intent(TestDrawer.this, CreateTeam.class);
            TestDrawer.this.startActivity(myIntent);
        }
        if (position-PlayerArray.getInstance().numberOfTeams()==1){
            if(PlayerArray.getInstance().Teams.size()>0) {
                Intent myIntent = new Intent(TestDrawer.this, DeleteTeam.class);
                //myIntent.putExtra("week", "1");
                TestDrawer.this.startActivity(myIntent);
            }
            else{
                String message= "No teams to display";
                Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
            }
        }
        if (position-PlayerArray.getInstance().numberOfTeams()==2){
            Intent myIntent = new Intent(TestDrawer.this, Scoreboard.class);
            TestDrawer.this.startActivity(myIntent);
        }
        else{
            return;
        }
    }
}
