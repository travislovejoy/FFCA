package com.example.travis.fantasycalculator;

import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Travis on 2/23/2016.
 */
public class Team {

    ArrayList<Player> Starters = new ArrayList<Player>();
    ArrayList<Player> Bench= new ArrayList<Player>();
    int PY, PTd, Pint, RY, RTD, RecY, RecTD;

    public Team(EditText PY, EditText PTd, EditText PInt, EditText RY, EditText RTd, EditText RecTD, EditText RecY){


        this.PY = etConverter(PY);
        this.PTd= etConverter(PTd);
        this.Pint= etConverter(PInt);
        this.RY= etConverter(RY);
        this.RTD= etConverter(RTd);
        this.RecY= etConverter(RecY);
        this.RecTD= etConverter(RecTD);
    }

    private int etConverter(EditText x){
        String myEditValue = x.getText().toString();
        return Integer.parseInt(myEditValue);

    }

}
