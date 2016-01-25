package com.example.travis.fantasycalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Travis on 1/21/2016.
 */
public final class PlayerArray {

        private static PlayerArray instance = new PlayerArray();

        public List<Player> PlayerList = new ArrayList<Player>();

            //PlayerList.add("00-0024334");

        public static PlayerArray getInstance() {

            if (instance== null){
                instance = new PlayerArray();
            }
            return instance;
        }

        public PlayerArray() {
            //PlayerList.add(Player("WR","00-0024334"));

            //Player newplayer= new Player("WR","00-0024334");
            //PlayerList.add("00-0027948");
            //PlayerList.add(newplayer);
            //newplayer= new Player("QB","00-0027948");
            //PlayerList.add(newplayer);
            //PlayerList.add(new Player("QB","0"));
        }

        public int Size(){
            return PlayerList.size();

        }

        public String getID(int pos){

            //Player newplayer= PlayerList.get(pos);
            return PlayerList.get(pos).id;
        }

        public void SwapId(int pos, String newId){
            PlayerList.get(pos).id=newId;
            return;
        }

        public String getpos(int pos){
            return PlayerList.get(pos).position;
        }

        public void addPlayer(String pos, String id){
            PlayerList.add(new Player(pos, id));
            return;
        }

}
