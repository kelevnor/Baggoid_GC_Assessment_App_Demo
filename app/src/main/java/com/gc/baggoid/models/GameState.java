package com.gc.baggoid.models;

import com.gc.baggoid.models.gamestate_object.State;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Immutable pojo representing the current state of the game
 *
 * Created by pdv on 2/28/17.
 */

public class GameState {

    // Poor man's typealias
    private static class Scores extends EnumMap<Team, Integer> {
        Scores() {
            super(Team.class);
        }
    }

    private Integer round;
    private final Scores totalScores;
    private final List<Scores> roundScores;
    private final Scores bagsRemaining;

    private GameState(Scores totalScores,
                      List<Scores> roundScores,
                      Scores bagsRemaining) {
        this.totalScores = totalScores;
        this.roundScores = roundScores;
        this.bagsRemaining = bagsRemaining;
    }

    //Testing a new constructor
    private GameState(Scores totalScores,
                      List<Scores> roundScores,
                      Scores bagsRemaining,
                      Integer round) {
        this.totalScores = totalScores;
        this.roundScores = roundScores;
        this.bagsRemaining = bagsRemaining;
        this.round = round;

    }

    public static GameState freshState(int bagsPerRound) {
        Scores totalScores = new Scores();
        Scores firstRoundScores = new Scores();
        Scores bagsRemaining = new Scores();
        for (Team team : Team.values()) {
            totalScores.put(team, 0);
            firstRoundScores.put(team, 0);
            bagsRemaining.put(team, bagsPerRound);
        }
        return new GameState(totalScores, Arrays.asList(firstRoundScores), bagsRemaining);
    }

    public static GameState resumeState(State state ) {
        Scores totalScores = new Scores();
        Scores firstRoundScores = new Scores();
        Scores bagsRemaining = new Scores();
        totalScores.put(Team.RED, state.getRedTeamTotalScore());
        totalScores.put(Team.BLUE, state.getBlueTeamTotalScore());
        firstRoundScores.put(Team.RED, state.getRedTeamCurrentRoundScore());
        firstRoundScores.put(Team.BLUE, state.getBlueTeamCurrentRoundScore());

        bagsRemaining.put(Team.RED, state.getRedTeamBagsRemaining());
        bagsRemaining.put(Team.BLUE, state.getBlueTeamBagsRemaining());


        return new GameState(totalScores, Arrays.asList(firstRoundScores), bagsRemaining, state.getRound());
    }

    public int round() {
        return roundScores.size() - 1;
    }

    public int totalScore(Team team) {
        return totalScores.get(team);
    }

    public void putTotalScore(Team team) {
        totalScores.put(team, 11);
    }

    public int currentRoundScore(Team team) {
        return roundScore(round(), team);
    }

    private int roundScore(int round, Team team) {
        if (round > round()) {
            throw new IllegalArgumentException("Round " + round + " hasn't happened yet");
        }
        return roundScores.get(round).get(team);
    }

    public int bagsRemaining(Team team) {
        return bagsRemaining.get(team);
    }

    public boolean roundOver() {
        for (Team team : Team.values()) {
            if (bagsRemaining(team) != 0) {
                return false;
            }
        }
        return true;
    }

    // Manipulation - static "pure" methods

    public static GameState decrementRemaining(final GameState previous, final Team team) {
        GameState newState = copyOf(previous);
        int remainingBefore = newState.bagsRemaining(team);
        if (remainingBefore == 0) {
            throw new IllegalStateException(String.format("Team %s has thrown too many bags", team.toString()));
        }
        newState.bagsRemaining.put(team, remainingBefore - 1);
        return newState;
    }

    public static GameState modifyCurrentRoundScore(final GameState previous, final Team team, int delta) {
        GameState newState = copyOf(previous);
        int oldScore = newState.currentRoundScore(team);
        newState.roundScores.get(newState.round()).put(team, oldScore + delta);
        return newState;
    }

    public static GameState modifyTotalScore(final GameState previous, final Team team, int delta) {
        GameState newState = copyOf(previous);
        int oldScore = newState.totalScore(team);
        newState.totalScores.put(team, oldScore + delta);
        return newState;
    }

    public static GameState modifyTotalScoreToEleven(final GameState previous, final Team team) {
        GameState newState = copyOf(previous);
        newState.totalScores.put(team, 11);
        return newState;
    }

    public static GameState endRound(final GameState previous, int bagsPerRound) {
        GameState newState = copyOf(previous);
        Scores newRound = new Scores();
        for (Team team : Team.values()) {
            newRound.put(team, 0);
            newState.bagsRemaining.put(team, bagsPerRound);
        }
        newState.roundScores.add(newRound);
        return newState;
    }

    private static Scores copyOf(Scores original) {
        Scores ret = new Scores();
        for (Map.Entry<Team, Integer> entry : original.entrySet()) {
            ret.put(entry.getKey(), entry.getValue());
        }
        return ret;
    }

    private static GameState copyOf(GameState originalState) {
        Scores totalScoresCopy = copyOf(originalState.totalScores);
        List<Scores> roundScoresCopy = new ArrayList<>();
        for (Scores roundScore : originalState.roundScores) {
            roundScoresCopy.add(copyOf(roundScore));
        }
        Scores bagsRemainingCopy = copyOf(originalState.bagsRemaining);
        return new GameState(totalScoresCopy, roundScoresCopy, bagsRemainingCopy);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof GameState)) {
            return false;
        }
        GameState otherState = (GameState) other;
        if (this.round() != otherState.round()) {
            return false;
        }
        for (Team team : Team.values()) {
            if (this.totalScore(team) != otherState.totalScore(team)) {
                return false;
            }
            for (int round = 0; round < round(); round++) {
                if (!this.roundScores.get(round).get(team).equals(otherState.roundScores.get(round).get(team))) {
                    return false;
                }
            }
            if (!this.bagsRemaining.get(team).equals(otherState.bagsRemaining.get(team))) {
                return false;
            }
        }
        return true;
    }

}
