package com.gc.baggoid;

import com.gc.baggoid.models.BagStatus;
import com.gc.baggoid.models.BagMovedEvent;
import com.gc.baggoid.models.GameState;
import com.gc.baggoid.models.Team;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static junit.framework.Assert.*;

/**
 * Created by pdv on 2/28/17.
 */

public class GameEngineTests {

    @Test
    public void testNoEvents() {
        GameState before = GameState.freshState(2);
        GameState after = GameEngine.processEvents(before, new ArrayList<>());
        assertEquals(2, after.bagsRemaining(Team.RED));
    }

    @Test
    public void testSimpleRound() {
        GameState before = GameState.freshState(2);
        GameState after = GameEngine.processEvents(before, Arrays.asList(
                new BagMovedEvent(BagStatus.IN_HAND, BagStatus.ON_BOARD, Team.RED),
                new BagMovedEvent(BagStatus.IN_HAND, BagStatus.ON_BOARD, Team.BLUE),
                new BagMovedEvent(BagStatus.IN_HAND, BagStatus.ON_BOARD, Team.RED),
                new BagMovedEvent(BagStatus.IN_HAND, BagStatus.ON_BOARD, Team.BLUE)
        ));

        assertEquals(2, after.currentRoundScore(Team.RED));
        assertEquals(2, after.currentRoundScore(Team.BLUE));
        assertTrue(after.roundOver());
    }

}
