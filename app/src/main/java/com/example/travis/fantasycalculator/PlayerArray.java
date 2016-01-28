package com.example.travis.fantasycalculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Created by Travis on 1/21/2016.
 */
public final class PlayerArray {

        private static PlayerArray instance = new PlayerArray();

        //public List<Player> PlayerList = new ArrayList<Player>();
        public HashMap<String, List<Player>> Teams= new HashMap<>();
                    //PlayerList.add("00-0024334");

        public static PlayerArray getInstance() {

            if (instance== null){
                instance = new PlayerArray();
            }return instance;
        }

        public PlayerArray() {
            //PlayerList.add(Player("WR","00-0024334"));
            //Teams.put(teamName,PlayerList);

            //Player newplayer= new Player("WR","00-0024334");
            //PlayerList.add("00-0027948");
            //PlayerList.add(newplayer);
            //newplayer= new Player("QB","00-0027948");
            //PlayerList.add(newplayer);
            //PlayerList.add(new Player("QB","0"));
        }

        public int Size(String teamName){
            //return PlayerList.size();
            return Teams.get(teamName).size();
        }

        public String getID(int pos, String teamName){

            //Player newplayer= PlayerList.get(pos);
            return Teams.get(teamName).get(pos).id;
        }

        public void SwapId(int pos, String newId, String teamName){
            Teams.get(teamName).get(pos).id=newId;
            return;
        }

        public String getpos(int pos, String teamName){
            return Teams.get(teamName).get(pos).position;
        }

        public void addPlayer(String pos, String id, String teamName){
            Teams.get(teamName).add(new Player(pos, id));
            return;
        }

        public void addTeam(String teamName){
            List<Player> PlayerList = new ArrayList<Player>();
            Teams.put(teamName, PlayerList);
            //Teams.put("blah", PlayerList);
        }

        public boolean checkKey(String teamName){
            if(Teams.containsKey(teamName)){
                return false;
            }
            return true;
        }

        public Set<String> getKeys(){
             return Teams.keySet();
        }

}
