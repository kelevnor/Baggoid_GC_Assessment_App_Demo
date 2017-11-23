package com.gc.baggoid;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.gc.baggoid.api.GetWeather;
import com.gc.baggoid.models.BagMovedEvent;
import com.gc.baggoid.models.GameState;
import com.gc.baggoid.models.Team;
import com.gc.baggoid.models.gamestate_object.State;
import com.gc.baggoid.models.score_history.HistoryItem;
import com.gc.baggoid.models.score_history.OuterItem;
import com.gc.baggoid.models.weather_object.WeatherObject;
import com.gc.baggoid.views.FieldView;
import com.gc.baggoid.views.GameScoreView;
import com.gc.baggoid.views.RoundScoreView;
import com.google.gson.Gson;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Main Activity for Scoring, serves as the View
 */

public class ScoringActivity extends AppCompatActivity implements FieldView.BagListener, GameContract.View {

    UtilityHelperClass utilityHelper;   //Helper Class for SharedPreferences

    GameScoreView gameScoreView;
    RoundScoreView redRoundScoreView;
    RoundScoreView blueRoundScoreView;
    TextView roundIndicator;
    FieldView fieldView;
    WeatherObject localWeather;
    GameContract.UserActionListener userActionListener;
    Team currentTeam;
    Integer hasStoredState = 0;
    Integer currentGameEngine = Config.GAME_TYPE_1;
    Integer initRound = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoring);
        setupViews(GameEngine.BAGS_PER_ROUND);
        utilityHelper = new UtilityHelperClass(this);
        hasStoredState = utilityHelper.getIntegerFromPreference("stored_state");
        if(hasStoredState == 0){
            startNewGame();
        }
        else{
            continueSavedGame();
        }
    }

    private void setupViews(int bagsPerRound) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_scoring);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        gameScoreView = (GameScoreView) findViewById(R.id.game_score_view);
        redRoundScoreView = (RoundScoreView) findViewById(R.id.round_score_red);
        redRoundScoreView.setBagsPerRound(bagsPerRound);
        blueRoundScoreView = (RoundScoreView) findViewById(R.id.round_score_blue);
        blueRoundScoreView.setBagsPerRound(bagsPerRound);
        roundIndicator = (TextView) findViewById(R.id.tv_round);
        fieldView = (FieldView) findViewById(R.id.field_view);
        fieldView.setBagListener(this);
    }

    private void continueSavedGame(){
        currentGameEngine = utilityHelper.pickStoredGameType(getApplicationContext(), utilityHelper);
        State state = utilityHelper.getStateFromSharedPreferences();
        initRound = utilityHelper.getIntegerFromPreference("round");
        roundIndicator.setText(getString(R.string.round, initRound));
        fieldView.clearBags();
        getCurrentTeam(state);
        instantiateGameEngine(1);

        if(UtilityHelperClass.isNetworkAvailable(getApplicationContext())){
            getWeather();
        }
    }

    private void startNewGame() {
        currentGameEngine = utilityHelper.pickStoredGameType(getApplicationContext(), utilityHelper);
        fieldView.clearBags();
        currentTeam = Team.RED;
        instantiateGameEngine(0);

        if(UtilityHelperClass.isNetworkAvailable(getApplicationContext())){
            getWeather();
        }
    }

    @Override
    public void updateScoreboard(@NonNull GameState gameState) {
        gameScoreView.setBlueTeamScore(gameState.totalScore(Team.BLUE));
        gameScoreView.setRedTeamScore(gameState.totalScore(Team.RED));
        redRoundScoreView.setRoundScore(gameState.currentRoundScore(Team.RED));
        redRoundScoreView.setBagsRemaining(gameState.bagsRemaining(Team.RED));
        blueRoundScoreView.setRoundScore(gameState.currentRoundScore(Team.BLUE));
        blueRoundScoreView.setBagsRemaining(gameState.bagsRemaining(Team.BLUE));
        roundIndicator.setText(getString(R.string.round, gameState.round() + 1));
        saveState(gameState);       //Saving state of the game.
    }

    public void saveState(GameState gameState){
        State state = new State();
        state.setBlueTeamBagsRemaining(gameState.bagsRemaining(Team.BLUE));
        state.setRedTeamBagsRemaining(gameState.bagsRemaining(Team.RED));
        state.setBlueTeamCurrentRoundScore(gameState.currentRoundScore(Team.BLUE));
        state.setRedTeamCurrentRoundScore(gameState.currentRoundScore(Team.RED));
        state.setBlueTeamTotalScore(gameState.totalScore(Team.BLUE));
        state.setRedTeamTotalScore(gameState.totalScore(Team.RED));
        state.setRound(gameState.round() + 1);
        utilityHelper.saveStateInSharedPreferences(state);   //Saving state in SharedPreferences
        utilityHelper.saveIntegerInPreference("stored_state", 1);   //Saving state of Preferences themselves if contain value
        utilityHelper.saveIntegerInPreference("round", gameState.round() + 1);  //save round in SharedPreferences
    }

    @Override
    public void onBagMoved(BagMovedEvent event) {
        userActionListener.onBagMoved(event);
    }

    @Override
    public void clearBags() {
        fieldView.clearBags();
    }

    @Override
    public void showRoundOverDialog(@Nullable Team roundWinner, int roundNumber, int scoreDelta) {
        fieldView.disallowNewBags();
        String title, message;
        if (roundWinner != null) {
            String teamName = getString(roundWinner.getDisplayNameRes());
            title = getString(R.string.dialog_title_round_win, teamName, roundNumber + 1);
            message = getResources().getQuantityString(R.plurals.dialog_msg_round_win, scoreDelta, scoreDelta);
        } else {
            title = getString(R.string.dialog_title_round_tie, roundNumber + 1);
            message = getString(R.string.dialog_msg_round_tie);
        }
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.start_next_round, (di, i) -> userActionListener.onNewRoundClicked())
                .show();
    }

    @Override
    public void showGameOverDialog(@NonNull Team winner, int redScore, int blueScore) {
        String teamName = getString(winner.getDisplayNameRes());
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_title_game_over, teamName))
                .setPositiveButton(R.string.start_new_game, (di, i) -> startNewGame())
                .setMessage(getString(R.string.final_score, redScore, blueScore))
                .setCancelable(false)
                .show();



        OuterItem outerItem = utilityHelper.getHistoryFromSharedPreferences();

        HistoryItem item = new HistoryItem();
        item.setBlueTeamScore(blueScore);
        item.setRedTeamScore(redScore);
        item.setWinner(winner);
        Calendar date = Calendar.getInstance();
        item.setDate(date.getTimeInMillis());
        outerItem.getData().add(item);

        utilityHelper.saveHistoryInSharedPreferences(outerItem);






    }

    private void showRules() {
        startActivity(new Intent(this, RulesActivity.class));
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_scoring, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_rules:
                showRules();
                return true;
            case R.id.menu_item_restart:
                startNewGame();
                return true;

            case R.id.menu_item_pick_game_type:
                Intent i = new Intent(this, SelectGameActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Gets Current Team during game flow
    @Override
    public Team getCurrentTeam() {
        if (currentTeam == Team.RED) {
            currentTeam = Team.BLUE;
            return Team.RED;
        } else {
            currentTeam = Team.RED;
            return Team.BLUE;
        }
    }

    //Gets Current Team after coming back in the app (Resuming the State of the app)
    @Override
    public Team getCurrentTeam(State state) {
        if(state.getBlueTeamBagsRemaining()<state.getRedTeamBagsRemaining()){
            currentTeam = Team.RED;
            return Team.RED;
        }
        else if(state.getBlueTeamBagsRemaining()>state.getRedTeamBagsRemaining()){
            currentTeam = Team.BLUE;
            return Team.BLUE;
        }
        else{
            currentTeam = Team.RED;
            return Team.RED;
        }
    }

    // Method to execute GetWeather API call
    public void getWeather(){
        if(UtilityHelperClass.isNetworkAvailable(getApplicationContext())){
            GetWeather getWeather = new GetWeather(Config.FIXED_LATITUDE, Config.FIXED_LONGITUDE);
            getWeather.setOnResultListener(onAsyncWeather);
            getWeather.execute();
        }
    }

    /**
     *  ResultListener for GetWeather API Call to handle Response
     *  Whenever starting a NEW GAME
     */
    GetWeather.OnAsyncResult onAsyncWeather = new GetWeather.OnAsyncResult() {
        @Override
        public void onResultSuccess(int resultCode, String result) {
            Gson gson = new Gson();
            localWeather = gson.fromJson(result, WeatherObject.class);      //Storing WeatherObject after API Call

            if (localWeather.getWeather().size() != 0) {
                Integer conditionCode = localWeather.getWeather().get(0).getId();           //storing weather condition code
                if (arrayContainsKey(Config.sunnyWeatherConditionCodes, conditionCode)) {
                    fieldView.changeBG(R.drawable.bg_sunny);
                } else if (arrayContainsKey(Config.cloudyWeatherConditionCodes, conditionCode)) {
                    fieldView.changeBG(R.drawable.bg_cloudy);
                } else if (arrayContainsKey(Config.rainyWeatherConditionCodes, conditionCode)) {
                    fieldView.changeBG(R.drawable.bg_rain);
                } else {
                    fieldView.changeBG(R.drawable.bg_sunny);
                }
            }
        }
        @Override
        public void onResultFail(int resultCode, String errorResult) {
            Log.e("FAIL_WEATHER", errorResult);
        }
    };

    private boolean arrayContainsKey(final Integer[] array, final int key) {
        return ArrayUtils.contains(array, key);
    }

    //Instantiate Preferred (Stored in SharedPreferences) by User Game Engine
    public void instantiateGameEngine(int hasStoredState){
        if(hasStoredState==1){
            pickGameEngine(utilityHelper.getStateFromSharedPreferences());      //with state
        }
        else{
            pickGameEngine();                                                   //without state (init)
        }
    }

    //Pick engine if does not have stored state
    private void pickGameEngine(){
        userActionListener = new GameEngine(this, getApplicationContext());
        userActionListener.init(null);
    }
    //Pick engine if does have stored state,
    private void pickGameEngine(State state){
        userActionListener = new GameEngine(this, getApplicationContext());
        userActionListener.resumeState(null, state);
    }
}