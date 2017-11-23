
package com.gc.baggoid.models.aggregations;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Aggregations {

    @SerializedName("red_wins")
    @Expose
    private Integer redWins;

    @SerializedName("blue_wins")
    @Expose
    private Integer blueWins;

    @SerializedName("avg_tosses_per_game")
    @Expose
    private Integer avgTossesPerGame;

    public Integer getRedWins() {
        return redWins;
    }

    public void setRedWins(Integer redWins) {
        this.redWins = redWins;
    }

    public Integer getBlueWins() {
        return blueWins;
    }

    public void setBlueWins(Integer blueWins) {
        this.blueWins = blueWins;
    }

    public Integer getAvgTossesPerGame() {
        return avgTossesPerGame;
    }

    public void setAvgTossesPerGame(Integer avgTossesPerGame) {
        this.avgTossesPerGame = avgTossesPerGame;
    }
}
