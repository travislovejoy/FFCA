package com.example.travis.fantasycalculator;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class DeleteTeam extends TestDrawer {

    static View contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         contentView= inflater.inflate(R.layout.delete_team, null, false);
        drawer.addView(contentView, 0);

        Set<String> keys = PlayerArray.getInstance().getKeys();
        String[] keyArray = keys.toArray(new String[keys.size()]);

        ArrayList<String> list= new ArrayList<String>(Arrays.asList(keyArray));

        MyCustomAdapter adapter = new MyCustomAdapter(list,this, DeleteTeam.this);

        ListView lView = (ListView)findViewById(android.R.id.list);
        lView.setAdapter(adapter);



    }


}
