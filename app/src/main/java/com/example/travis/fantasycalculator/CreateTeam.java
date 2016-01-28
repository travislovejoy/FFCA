package com.example.travis.fantasycalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Set;

public class CreateTeam extends AppCompatActivity {
    Button CreateTeam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);

        final Spinner qbSpinner = (Spinner) findViewById(R.id.qbSpinner);
        ArrayAdapter qbAdapter = ArrayAdapter.createFromResource(
                this, R.array.posNumbers, android.R.layout.simple_spinner_item);
        qbAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qbSpinner.setAdapter(qbAdapter);

        final Spinner rbSpinner = (Spinner) findViewById(R.id.rbSpinner);
        ArrayAdapter rbAdapter = ArrayAdapter.createFromResource(
                this, R.array.posNumbers, android.R.layout.simple_spinner_item);
        rbAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rbSpinner.setAdapter(rbAdapter);

        final Spinner wrSpinner = (Spinner) findViewById(R.id.wrSpinner);
        ArrayAdapter wrAdapter = ArrayAdapter.createFromResource(
                this, R.array.posNumbers, android.R.layout.simple_spinner_item);
        wrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wrSpinner.setAdapter(wrAdapter);

        final Spinner flexSpinner = (Spinner) findViewById(R.id.flexSpinner);
        ArrayAdapter flexAdapter = ArrayAdapter.createFromResource(
                this, R.array.posNumbers, android.R.layout.simple_spinner_item);
        flexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        flexSpinner.setAdapter(flexAdapter);

        final EditText teamName = (EditText) findViewById(R.id.teamName);
        CreateTeam=(Button)findViewById(R.id.create);
        CreateTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = teamName.getText().toString();

                if(TextUtils.isEmpty(name)|| !PlayerArray.getInstance().checkKey(name)) {
                    teamName.setError("Invalid Key");
                    return;
                }
                else {
                    int qb = Integer.valueOf(qbSpinner.getSelectedItem().toString());
                    int rb = Integer.valueOf(rbSpinner.getSelectedItem().toString());
                    int wr = Integer.valueOf(wrSpinner.getSelectedItem().toString());
                    int flex = Integer.valueOf(flexSpinner.getSelectedItem().toString());
                    //String name = "default";
                    //PlayerArray team= new PlayerArray(name);
                    PlayerArray.getInstance().addTeam(name);
                    for (int i = 0; i < qb; i++) {
                        PlayerArray.getInstance().addPlayer("QB", "0", name);
                    }
                    for (int i = 0; i < rb; i++) {
                        PlayerArray.getInstance().addPlayer("RB", "0", name);
                    }
                    for (int i = 0; i < wr; i++) {
                        PlayerArray.getInstance().addPlayer("WR", "0", name);
                    }
                    for (int i = 0; i < flex; i++) {
                        PlayerArray.getInstance().addPlayer("Flex", "0", name);
                    }
                   Intent myIntent = new Intent(CreateTeam.this, MainActivity.class);
                   CreateTeam.this.startActivity(myIntent);

                }
        }
        });
    }





}
