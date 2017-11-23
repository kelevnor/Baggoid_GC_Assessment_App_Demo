package com.gc.baggoid;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.gc.baggoid.models.Team;
import com.gc.baggoid.models.aggregations.Aggregations;
import com.gc.baggoid.models.gamestate_object.State;
import com.gc.baggoid.models.score_history.HistoryItem;
import com.gc.baggoid.models.score_history.OuterItem;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Marios Sifalakis on 8/27/17.
 */

public class MainActivityHelper {

    ArrayList<HistoryItem> items;


    public Aggregations CalculateAggregations(ArrayList<HistoryItem> items){
        this.items = items;

        Aggregations objectAggregations = new Aggregations();

        objectAggregations.setRedWins(redWins());
        objectAggregations.setBlueWins(blueWins());
        objectAggregations.setAvgTossesPerGame(avgTossesPerGame());


        return objectAggregations;
    }


    public int redWins(){

        int counterRedWins = 0;
        for(int i = 0; i<items.size(); i++){
            if(items.get(i).getWinner()==Team.RED){
                counterRedWins++;
            }
        }
        return counterRedWins;
    }

    public int blueWins(){

        int counterBlueWins = 0;
        for(int i = 0; i<items.size(); i++){
            if(items.get(i).getWinner()==Team.RED){
                counterBlueWins++;
            }
        }

        return counterBlueWins;
    }

    public int avgTossesPerGame(){

        return 1;
    }

}
