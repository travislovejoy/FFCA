package com.example.travis.fantasycalculator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Travis on 1/21/2016.
 */
public final class PlayerArray {

        private static final PlayerArray instance = new PlayerArray();

        private List<String> PlayerList = new ArrayList<String>();

            //PlayerList.add("00-0024334");

        public static PlayerArray getInstance() {
            return instance;
        }

        private PlayerArray() {
            PlayerList.add("00-0024334");
            PlayerList.add("00-0027948");
        }

        public int Size(){
            return PlayerList.size();

        }

        public String getID(int pos){

            return PlayerList.get(pos);
        }

        public void SwapId(int pos, String id){
            PlayerList.set(pos,id);
            return;
        }

}
