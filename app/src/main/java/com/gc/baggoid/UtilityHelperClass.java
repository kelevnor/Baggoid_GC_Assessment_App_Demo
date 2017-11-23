package com.gc.baggoid;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.gc.baggoid.models.gamestate_object.State;
import com.gc.baggoid.models.score_history.OuterItem;
import com.google.gson.Gson;

/**
 * Created by Marios Sifalakis on 8/27/17.
 */

public class UtilityHelperClass {

    Context con;

    public UtilityHelperClass(Context con){
        this.con = con;
    }

    public void saveStateInSharedPreferences(State state){
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(con);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(state);
        prefsEditor.putString("state", json);
        prefsEditor.commit();
    }

    public State getStateFromSharedPreferences(){
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(con);
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("history", "");
        State savedState = gson.fromJson(json, State.class);
        return savedState;
    }

    public void saveHistoryInSharedPreferences(OuterItem item){
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(con);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(item);
        prefsEditor.putString("history", json);
        prefsEditor.commit();
    }

    public OuterItem getHistoryFromSharedPreferences(){
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(con);
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("state", "");
        OuterItem item = gson.fromJson(json, OuterItem.class);
        return item;
    }


    //Save String in SharedPreferences
    public void saveIntegerInPreference(String intName, Integer intValue) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(con);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(intName, intValue);
        editor.commit();
    }
    //Get String from SharedPreferences
    public Integer getIntegerFromPreference(String variable_name) {
        Integer preference_return;
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(con);
        preference_return = preferences.getInt(variable_name, 0);

        return preference_return;
    }

    //Method to detect internet availability
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static int pickStoredGameType(Context con, UtilityHelperClass util){
        int type = util.getIntegerFromPreference(con.getResources().getString(R.string.preference_game_type));
        return type;
    }
}
