
package com.gc.baggoid.models.score_history;

import com.gc.baggoid.models.Team;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HistoryItem {

    @SerializedName("blue_team_score")
    @Expose
    private Integer blueTeamScore;

    @SerializedName("red_team_score")
    @Expose
    private Integer redTeamScore;

    @SerializedName("date")
    @Expose
    private Long date;

    @SerializedName("winner")
    @Expose
    private Team winner;


    public Integer getBlueTeamScore() {
        return blueTeamScore;
    }

    public void setBlueTeamScore(Integer blueTeamScore) {
        this.blueTeamScore = blueTeamScore;
    }

    public Integer getRedTeamScore() {
        return redTeamScore;
    }

    public void setRedTeamScore(Integer redTeamScore) {
        this.redTeamScore = redTeamScore;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Team getWinner() {
        return winner;
    }

    public void setWinner(Team winner) {
        this.winner = winner;
    }

}
