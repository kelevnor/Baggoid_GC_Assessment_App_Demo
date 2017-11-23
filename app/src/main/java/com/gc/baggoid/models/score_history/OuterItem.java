
package com.gc.baggoid.models.score_history;

import com.gc.baggoid.models.Team;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OuterItem {

    @SerializedName("data")
    @Expose
    private ArrayList<HistoryItem> data;

    public ArrayList<HistoryItem> getData() {
        return data;
    }

    public void setData(ArrayList<HistoryItem> data) {
        this.data = data;
    }

}
