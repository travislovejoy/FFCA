package com.example.travis.fantasycalculator;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Travis on 4/11/2016.
 */
public class ScoringDescription {

    static String rushingDescription(int rush_yards, int rush_tds, String description, boolean RB) {
        if (rush_yards > 10|| RB) {
            description += Integer.toString(rush_yards) + " RYDS ";
        }
        if (rush_tds >= 1|| RB) {
            description += Integer.toString(rush_tds) + " RTDS ";

        }
        return description;

    }
    static String receivingDescription(int rec_yards, int rec_tds, int receptions, String description, boolean WR) {
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

    static String passingDescription(int pass_yards, int pass_tds, String description, boolean QB) {
        if(pass_yards>25||QB) {
            description += Integer.toString(pass_yards) + " PYDS ";
        }
        if (pass_tds >= 1||QB) {
            description += Integer.toString(pass_tds) + " PTDS ";
        }
        return description;
    }

    public static String getDescription(JSONObject player, String position){
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
}
