package com.gc.baggoid.views;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.Px;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gc.baggoid.R;
import com.gc.baggoid.models.Team;

import java.util.EnumMap;

/**
 * Created by pdv on 3/1/17.
 */

public class BagView extends ImageView {

    @DimenRes
    private static final int BAG_SIZE = R.dimen.large;

    private static final EnumMap<Team, Integer> SOURCES;
    static {
        SOURCES = new EnumMap<>(Team.class);
        SOURCES.put(Team.BLUE, R.drawable.bag_blue);
        SOURCES.put(Team.RED, R.drawable.bag_red);
    }

    private Team team;

    public BagView(Context context) {
        this(context, Team.RED);
    }

    public BagView(Context context, @NonNull Team team) {
        super(context);
        this.team = team;
        setImageResource(SOURCES.get(team));
        @Px int bagSize = getResources().getDimensionPixelSize(BAG_SIZE);
        setLayoutParams(new ViewGroup.LayoutParams(bagSize, bagSize));
        setRotation((float) Math.random() * 180);
    }

    public Team getTeam() {
        return team;
    }

    public void centerAround(float x, float y) {
        setX(x - getLayoutParams().width / 2);
        setY(y - getLayoutParams().width / 2);
    }

    public Point getCenter() {
        int halfWidth = getLayoutParams().width / 2;
        return new Point((int) getX() + halfWidth, (int) getY() + halfWidth);
    }

}
