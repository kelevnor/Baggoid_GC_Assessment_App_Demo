package com.gc.baggoid.models;

import android.support.annotation.StringRes;

import com.gc.baggoid.R;

/**
 * Created by pdv on 2/28/17.
 */

public enum Team {
    RED,
    BLUE;

    @StringRes
    public int getDisplayNameRes() {
        switch (this) {
            case RED:
                return R.string.red;
            case BLUE:
                return R.string.blue;
        }
        return -1;
    }
}
