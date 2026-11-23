import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MatchResultTest {

    // M-01
    @Test
    void validateScore_normalResult_returnsTrue() {
        assertTrue(new MatchResult(2, 1).validateScore());
    }

    // M-02
    @Test
    void validateScore_draw_returnsTrue() {
        assertTrue(new MatchResult(1, 1).validateScore());
    }

    // M-03
    @Test
    void validateScore_zeroZero_returnsTrue() {
        assertTrue(new MatchResult(0, 0).validateScore());
    }

    // M-04
    @Test
    void validateScore_negativeHome_returnsFalse() {
        assertFalse(new MatchResult(-1, 0).validateScore());
    }

    // M-05
    @Test
    void validateScore_negativeAway_returnsFalse() {
        assertFalse(new MatchResult(0, -1).validateScore());
    }

    // M-06
    @Test
    void validateScore_bothNegative_returnsFalse() {
        assertFalse(new MatchResult(-1, -1).validateScore());
    }

    // M-07
    @Test
    void getWinner_homeWin() {
        assertEquals("Home Team Wins", new MatchResult(3, 1).getWinner());
    }

    // M-08
    @Test
    void getWinner_awayWin() {
        assertEquals("Away Team Wins", new MatchResult(0, 2).getWinner());
    }

    // M-09
    @Test
    void getWinner_draw() {
        assertEquals("Match Drawn", new MatchResult(2, 2).getWinner());
    }

    // M-10
    @Test
    void getWinner_negativeScore_returnsInvalid() {
        assertEquals("Invalid Score", new MatchResult(-1, 2).getWinner());
    }
}
