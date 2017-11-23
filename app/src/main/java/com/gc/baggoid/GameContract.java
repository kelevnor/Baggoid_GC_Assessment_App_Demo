package com.gc.baggoid;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gc.baggoid.models.BagMovedEvent;
import com.gc.baggoid.models.GameState;
import com.gc.baggoid.models.Team;
import com.gc.baggoid.models.gamestate_object.State;

/**
 * A contract that defines the interfaces for the view and user action listener.
 *
 * Created by pdv on 3/2/17.
 */

interface GameContract {

    interface View {

        /**
         * Update the scoreboard UI to reflect the current state of the game
         * @param state The current state
         */
        void updateScoreboard(@NonNull GameState state);


        /**
         * Show a dialog prompting the user to roll over the round. Can be dismissed.
         * Also locks the field so that no new bags can be thrown.
         * @param roundWinner Who won, null if tie
         * @param roundNumber Which round it is
         * @param victoryMargin How many points are going to be added to their score
         */
        void showRoundOverDialog(@Nullable Team roundWinner, int roundNumber, int victoryMargin);

        /**
         * Clears the thrown bags off the field
         */
        void clearBags();

        /**
         * Show a dialog with the winner
         * @param winner The winning team
         */
        void showGameOverDialog(@NonNull Team winner, int redScore, int blueScore);

    }

    interface UserActionListener {

        /**
         * Starts the engine
         * @param initialState The state to start in, or null for a fresh state
         */
        void init(@Nullable GameState initialState);

        /**
         * Called when the user moves a bag, or throws a new one
         * @param event The event
         */
        void onBagMoved(@NonNull BagMovedEvent event);

        /**
         * Called when the user accepts the round over dialog
         */
        void onNewRoundClicked();

        /**
         * Starts the engine
         * @param savedState The state to start in, or null for a fresh state
         */
        void resumeState(@Nullable GameState initialState, @Nullable State savedState);

    }

}
