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

    public Team(int PY, int PTd, int PInt, int RY, int RTd, int RecTD, int RecY){


        this.PY = PY;
        this.PTd= PTd;
        this.Pint= PInt;
        this.RY= RY;
        this.RTD= RTd;
        this.RecY= RecY;
        this.RecTD= RecTD;
    }



}