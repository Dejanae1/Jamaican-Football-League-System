import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MoneyTest {

    private static final double DELTA = 0.001;

    // MO-01
    @Test
    void gctRate_is15Percent() {
        assertEquals(0.15, Money.GCT_RATE, DELTA);
    }

    // MO-02
    @Test
    void round_roundsUp() {
        assertEquals(10.46, Money.round(10.456), DELTA);
    }

    // MO-03
    @Test
    void round_roundsDown() {
        assertEquals(10.45, Money.round(10.454), DELTA);
    }

    // MO-04
    @Test
    void round_zero_returnsZero() {
        assertEquals(0.0, Money.round(0), DELTA);
    }

    // MO-05
    @Test
    void gctIncluded_115_returns15() {
        assertEquals(15.0, Money.gctIncluded(115), DELTA);
    }

    // MO-06
    @Test
    void gctIncluded_230_returns30() {
        assertEquals(30.0, Money.gctIncluded(230), DELTA);
    }

    // MO-07
    @Test
    void gctIncluded_100_returnsRounded() {
        assertEquals(13.04, Money.gctIncluded(100), DELTA);
    }

    // MO-08
    @Test
    void gctIncluded_zero_returnsZero() {
        assertEquals(0.0, Money.gctIncluded(0), DELTA);
    }
}
