package com.gc.baggoid;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.util.Log;

import com.gc.baggoid.models.BagStatus;
import com.gc.baggoid.models.BagMovedEvent;
import com.gc.baggoid.models.GameState;
import com.gc.baggoid.models.Team;
import com.gc.baggoid.models.gamestate_object.State;

import java.util.EnumMap;
import java.util.List;

/**
 * The "presenter", that holds state and makes decisions
 *
 * Created by pdv on 2/28/17.
 */

class GameEngine implements GameContract.UserActionListener {

    static final int BAGS_PER_ROUND = 4;
    private static final int SCORE_TO_WIN = 21;
    private static final int WIN_BY = 2;

    static UtilityHelperClass utilityHelper;
    private static Context context = null;
    private static final EnumMap<BagStatus, Integer> POINT_VALUES;
    static {
        POINT_VALUES = new EnumMap<>(BagStatus.class);
        POINT_VALUES.put(BagStatus.OFF_BOARD, 0);
        POINT_VALUES.put(BagStatus.ON_BOARD, 1);
        POINT_VALUES.put(BagStatus.IN_HOLE, 3);
    }

    @NonNull private final GameContract.View view;
    private GameState state;

    GameEngine(@NonNull GameContract.View view, Context context) {
        this.view = view;
        this.context = context;
        utilityHelper = new UtilityHelperClass(context);

    }

    @Override
    public void init(@Nullable GameState initialState) {
        this.state = initialState != null ? initialState : GameState.freshState(BAGS_PER_ROUND);
        view.updateScoreboard(state);
    }

    @Override
    public void resumeState(@Nullable GameState initialGameState, @Nullable State initialState) {
//        this.state = new GameState().
        this.state = initialGameState != null ? initialGameState : GameState.resumeState(initialState); //freshState(BAGS_PER_ROUND);
        view.updateScoreboard(state);
    }

    @Override
    public void onBagMoved(@NonNull BagMovedEvent event) {
        state = processEvent(state, event);
        if (state.roundOver()) {
            onRoundOver();
        }
        view.updateScoreboard(state);
    }

    private static Pair<Team, Integer> roundWinnerAndDelta(GameState state) {
        int redScore = state.currentRoundScore(Team.RED);
        int blueScore = state.currentRoundScore(Team.BLUE);
        Team roundWinner = null;
        if (redScore != blueScore) {
            roundWinner = redScore > blueScore ? Team.RED : Team.BLUE;
        }
        return new Pair<>(roundWinner, Math.abs(redScore - blueScore));
    }

    private void onRoundOver() {
        Pair<Team, Integer> winnerAndDelta = roundWinnerAndDelta(state);
        view.showRoundOverDialog(winnerAndDelta.first, state.round(), winnerAndDelta.second);
    }

    @Nullable
    private static Team getWinnerOrNull(GameState state) {
        int redTotal = state.totalScore(Team.RED);
        int blueTotal = state.totalScore(Team.BLUE);
        //Determine winner when game type is Config.GAME_TYPE_1
        if(utilityHelper.pickStoredGameType(context, utilityHelper) ==Config.GAME_TYPE_1){
            if (redTotal >= SCORE_TO_WIN && redTotal - blueTotal >= WIN_BY) {
                return Team.RED;
            } else if (blueTotal >= SCORE_TO_WIN && blueTotal - redTotal >= WIN_BY) {
                return Team.BLUE;
            }
        }
        //Determine winner when game type is Config.GAME_TYPE_2
        else{

            if (redTotal == SCORE_TO_WIN) {
                return Team.RED;
            } else if (blueTotal == SCORE_TO_WIN) {
                return Team.BLUE;
            }
        }

        return null;
    }

    @Override
    public void onNewRoundClicked() {
        Pair<Team, Integer> winnerAndDelta = roundWinnerAndDelta(state);
        if (winnerAndDelta.first != null) {
            state = GameState.modifyTotalScore(state, winnerAndDelta.first, winnerAndDelta.second);
        }

        Team winner = getWinnerOrNull(state);
        if (winner != null) {
            view.showGameOverDialog(winner, state.totalScore(Team.RED), state.totalScore(Team.BLUE));
        } else {

            if(utilityHelper.pickStoredGameType(context, utilityHelper) ==Config.GAME_TYPE_1){

                state = GameState.endRound(state, BAGS_PER_ROUND);
                view.clearBags();
                view.updateScoreboard(state);
            }
            else{
                int redTotal = state.totalScore(Team.RED);
                int blueTotal = state.totalScore(Team.BLUE);

                if(redTotal >= SCORE_TO_WIN) {
                    state.putTotalScore(Team.RED);
                }
                else if(blueTotal >= SCORE_TO_WIN){
                    state.putTotalScore(Team.BLUE);
                }

                state = GameState.endRound(state, BAGS_PER_ROUND);
                view.clearBags();
                view.updateScoreboard(state);
            }


        }
    }

    private static GameState processOrigin(GameState before, Team team, BagStatus origin) {
        switch (origin) {
            case IN_HAND:
                return GameState.decrementRemaining(before, team);
            default:
                return GameState.modifyCurrentRoundScore(before, team, -1 * POINT_VALUES.get(origin));
        }
    }

    private static GameState processResult(GameState before, Team team, BagStatus result) {
        switch (result) {
            case IN_HAND:
                throw new IllegalArgumentException("You can't pick the bags up until the round is over, silly");
            default:
                return GameState.modifyCurrentRoundScore(before, team, POINT_VALUES.get(result));
        }
    }

    static GameState processEvent(@NonNull GameState before, @NonNull BagMovedEvent event) {
        GameState intermediate = processOrigin(before, event.team, event.origin);
        return processResult(intermediate, event.team, event.result);
    }

    static GameState processEvents(@NonNull GameState before, @NonNull List<BagMovedEvent> events) {
        GameState state = before;
        for (BagMovedEvent event : events) {
            state = processEvent(state, event);
        }
        return state;
    }

}
