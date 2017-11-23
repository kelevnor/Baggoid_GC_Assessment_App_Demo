
package com.gc.baggoid.models.gamestate_object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class State {

    @SerializedName("blue_team_total_score")
    @Expose
    private Integer blueTeamTotalScore;

    @SerializedName("red_team_total_score")
    @Expose
    private Integer redTeamTotalScore;

    @SerializedName("blue_team_current_round_score")
    @Expose
    private Integer blueTeamCurrentRoundScore;

    @SerializedName("red_team_current_round_score")
    @Expose
    private Integer redTeamCurrentRoundScore;

    @SerializedName("blue_team_bags_remaining")
    @Expose
    private Integer blueTeamBagsRemaining;

    @SerializedName("red_team_bags_remaining")
    @Expose
    private Integer redTeamBagsRemaining;

    @SerializedName("round")
    @Expose
    private Integer round;



    public Integer getBlueTeamTotalScore() {
        return blueTeamTotalScore;
    }

    public void setBlueTeamTotalScore(Integer blueTeamTotalScore) {
        this.blueTeamTotalScore = blueTeamTotalScore;
    }

    public Integer getRedTeamTotalScore() {
        return redTeamTotalScore;
    }

    public void setRedTeamTotalScore(Integer redTeamTotalScore) {
        this.redTeamTotalScore = redTeamTotalScore;
    }

    public Integer getBlueTeamCurrentRoundScore() {
        return blueTeamCurrentRoundScore;
    }

    public void setBlueTeamCurrentRoundScore(Integer blueTeamCurrentRoundScore) {
        this.blueTeamCurrentRoundScore = blueTeamCurrentRoundScore;
    }

    public Integer getRedTeamCurrentRoundScore() {
        return redTeamCurrentRoundScore;
    }

    public void setRedTeamCurrentRoundScore(Integer redTeamCurrentRoundScore) {
        this.redTeamCurrentRoundScore = redTeamCurrentRoundScore;
    }

    public Integer getBlueTeamBagsRemaining() {
        return blueTeamBagsRemaining;
    }

    public void setBlueTeamBagsRemaining(Integer blueTeamBagsRemaining) {
        this.blueTeamBagsRemaining = blueTeamBagsRemaining;
    }

    public Integer getRedTeamBagsRemaining() {
        return redTeamBagsRemaining;
    }

    public void setRedTeamBagsRemaining(Integer redTeamBagsRemaining) {
        this.redTeamBagsRemaining = redTeamBagsRemaining;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }
}
