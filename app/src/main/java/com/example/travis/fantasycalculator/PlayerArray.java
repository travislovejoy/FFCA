package com.example.travis.fantasycalculator;

import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
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
        public HashMap<String, Team> Teams= new HashMap<>();
        public String week= "1";
        public String currentTeam;
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

        public int Size(String teamName, boolean Starter){
            //return PlayerList.size();
            if(Starter){
                return Teams.get(teamName).Starters.size();}
            else{
                return Teams.get(teamName).Bench.size();
            }
        }

        public String getID(int pos, String teamName){

            //Player newplayer= PlayerList.get(pos);
            return Teams.get(teamName).Starters.get(pos).id;
        }

        public String getBenchID(int pos, String teamName){

        //Player newplayer= PlayerList.get(pos);
        return Teams.get(teamName).Bench.get(pos).id;
        }

        public void SwapId(int pos, String newId, String newName, String newTeam, String teamName, String type, String newPos){
            if (type.equals("starter")){
                Teams.get(teamName).Starters.get(pos).id=newId;
                Teams.get(teamName).Starters.get(pos).position=newPos;
                Teams.get(teamName).Starters.get(pos).name=newName;
                Teams.get(teamName).Starters.get(pos).team=newTeam;
            }
            else{
                Teams.get(teamName).Bench.get(pos).id=newId;
                Teams.get(teamName).Bench.get(pos).position=newPos;
                Teams.get(teamName).Bench.get(pos).name=newName;
                Teams.get(teamName).Bench.get(pos).team=newTeam;
            }

            return;
        }

        public String getpos(int pos, String teamName, boolean Starter){
            if (Starter){
                return Teams.get(teamName).Starters.get(pos).position;}
            else{
                return Teams.get(teamName).Bench.get(pos).position;}

            }

        public String getRpos(int pos, String teamName, boolean Starter){
            if (Starter){
                return Teams.get(teamName).Roster.get(pos);}
            else{
                return Teams.get(teamName).Roster.get(pos+ PlayerArray.getInstance().Size(teamName,true));}

        }

        public String getpos(int pos, String teamName){
            return Teams.get(teamName).Roster.get(pos);

    }

        public void addPlayer(String pos, String id, String name, String team, String teamName){

            Teams.get(teamName).Starters.add(new Player("", id, name, team));
            Teams.get(teamName).Roster.add(pos);
            return;
        }

        public void addBench(String pos, String id, String name, String team,String teamName){
            Teams.get(teamName).Bench.add(new Player("", id, name, team));
            Teams.get(teamName).Roster.add("Bench");
            return;
        }

        public void addTeam(String teamName, int PY, int PTd, int PInt, int RY, int RTd, int RecTD, int RecY){
            //List<Player> PlayerList = new ArrayList<Player>();
            Team newTeam= new Team(PY, PTd, PInt, RY, RTd, RecY, RecTD);
            Teams.put(teamName, newTeam);
            currentTeam= teamName;
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

        public void deleteTeam(String key){
            Teams.remove(key);
        }

        public ArrayList<String> keysInArrayList(){
            Set<String> keys = Teams.keySet();
            String[] keyArray = keys.toArray(new String[keys.size()]);
            ArrayList<String> list= new ArrayList<String>(Arrays.asList(keyArray));
            return list;
        }

        public void swap(int mposition, int position, boolean mStarter, boolean Starter){
            ArrayList<Player> tmp;
            Player temp;
            ArrayList<Player> second;



                if (mStarter) {
                    tmp = Teams.get(currentTeam).Starters;
                    temp = Teams.get(currentTeam).Starters.get(mposition);
                } else {
                    tmp = Teams.get(currentTeam).Bench;
                    temp = Teams.get(currentTeam).Bench.get(mposition);
                }
                if (Starter) {
                    second = Teams.get(currentTeam).Starters;
                } else {
                    second = Teams.get(currentTeam).Bench;
                }


                tmp.set(mposition, second.get(position));
                second.set(position, temp);



        }

        public int numberOfTeams(){
            return Teams.size();
        }

        public Team getTeam(String teamName){
            return Teams.get(teamName);
        }

        public boolean checkPos(String pos1, String pos2, String R1, String R2, String team){
            /*
            pos1= The position of the first player selected
            pos2= The position of the second player selected
            R1= The position designated for the first slot
            R2= The position designated for the second slot
            */

            if(!(R1.equals(pos2)||R1.equals("Bench")|| R1.equals("Flex")||pos2.equals(""))){
                return false;
            }
            if(!(R2.equals(pos1)||R2.equals("Bench")|| R2.equals("Flex")||pos1.equals(""))){
                return false;
            }
            return true;
        }

        public void setMaxRosterSize(String teamName){
            int starters= PlayerArray.getInstance().Teams.get(teamName).Starters.size();
            int bench=    PlayerArray.getInstance().Teams.get(teamName).Bench.size();
            PlayerArray.getInstance().Teams.get(teamName).maxRosterSize= starters+ bench;
            PlayerArray.getInstance().Teams.get(teamName).benchSize= bench;
        }

        public int getMaxRosterSize(String teamName){

            return PlayerArray.getInstance().Teams.get(teamName).maxRosterSize;
        }





}
