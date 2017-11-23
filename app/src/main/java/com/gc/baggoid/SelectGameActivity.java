package com.gc.baggoid;

import android.app.ActionBar;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class SelectGameActivity extends AppCompatActivity implements ActionBar.OnNavigationListener,View.OnClickListener{
    UtilityHelperClass utilityHelper;
    TextView selectedBtnOne, selectedBtnTwo, selectedSubBtnOne, selectedSubBtnTwo;
    Button btnOne, btnTwo;
    Typeface fontAwesome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_game_type);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_pick_game);
        toolbar.setTitle(R.string.pickgametype);
        toolbar.setNavigationIcon(getDrawable(R.drawable.ic_arrow_back_white_24dp));

        utilityHelper = new UtilityHelperClass(this);

        selectedBtnOne = (TextView) findViewById(R.id.tv_selected1);
        selectedBtnTwo = (TextView) findViewById(R.id.tv_selected2);

        selectedSubBtnOne = (TextView) findViewById(R.id.tv_subtitle1);
        selectedSubBtnTwo = (TextView) findViewById(R.id.tv_subtitle2);

        btnOne = (Button) findViewById(R.id.btn_one);
        btnTwo = (Button) findViewById(R.id.btn_two);

        //Initializing FontAwesome Asset
        fontAwesome = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.fontawesome_typeface_asset));

        //Displaying Stored Game Type
        displayGameType(UtilityHelperClass.pickStoredGameType(getApplicationContext(), utilityHelper));

        selectedBtnOne.setTypeface(fontAwesome);
        selectedBtnTwo.setTypeface(fontAwesome);

        btnOne.setOnClickListener(this);
        btnTwo.setOnClickListener(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_one:
                //Displaying Picked by User Game Type 1
                displayGameType(Config.GAME_TYPE_1);
                utilityHelper.saveIntegerInPreference(getResources().getString(R.string.preference_game_type), Config.GAME_TYPE_1);
                break;

            case R.id.btn_two:
                //Displaying Picked by User Game Type 2
                displayGameType(Config.GAME_TYPE_2);
                utilityHelper.saveIntegerInPreference(getResources().getString(R.string.preference_game_type), Config.GAME_TYPE_2);
                break;
        }
    }

    //Display Correct game type depending on User and SharedPreferences
    public void displayGameType(int type){
        if(type == Config.GAME_TYPE_1){
            btnOne.setTextColor(getResources().getColor(R.color.white));
            selectedSubBtnOne.setTextColor(getResources().getColor(R.color.grayE));
            btnTwo.setTextColor(getResources().getColor(R.color.grayB));
            selectedSubBtnTwo.setTextColor(getResources().getColor(R.color.grayB));
            selectedBtnOne.setVisibility(View.VISIBLE);
            selectedBtnTwo.setVisibility(View.GONE);
            utilityHelper.saveIntegerInPreference(getResources().getString(R.string.preference_game_type), Config.GAME_TYPE_1);
        }
        else if(type == Config.GAME_TYPE_2){
            btnOne.setTextColor(getResources().getColor(R.color.grayB));
            selectedSubBtnOne.setTextColor(getResources().getColor(R.color.grayB));
            btnTwo.setTextColor(getResources().getColor(R.color.white));
            selectedSubBtnTwo.setTextColor(getResources().getColor(R.color.grayE));
            selectedBtnOne.setVisibility(View.GONE);
            selectedBtnTwo.setVisibility(View.VISIBLE);
            utilityHelper.saveIntegerInPreference(getResources().getString(R.string.preference_game_type), Config.GAME_TYPE_2);
        }
        else{
            btnOne.setTextColor(getResources().getColor(R.color.white));
            selectedSubBtnOne.setTextColor(getResources().getColor(R.color.grayE));
            btnTwo.setTextColor(getResources().getColor(R.color.grayB));
            selectedSubBtnTwo.setTextColor(getResources().getColor(R.color.grayB));
            selectedBtnOne.setVisibility(View.VISIBLE);
            selectedBtnTwo.setVisibility(View.GONE);
            utilityHelper.saveIntegerInPreference(getResources().getString(R.string.preference_game_type), Config.GAME_TYPE_1);
        }


    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        return false;
    }
}
