import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class StadiumTest {

    private Stadium stadium;
    private LocalDate date;
    private LocalTime time;

    @BeforeEach
    void setUp() {
        stadium = new Stadium("ST01", "Independence Park", "Kingston", 35000, Stadium.FieldCondition.GOOD, 1500.00);
        date = LocalDate.of(2026, 12, 5);
        time = LocalTime.of(15, 0);
    }

    private Fixtures makeFixture(String id, LocalDate d, LocalTime t) {
        return new Fixtures(id, d, t, null, null, stadium, null);
    }

    // S-01
    @Test
    void checkAvailability_nullDateOrTime_returnsFalse() {
        assertFalse(stadium.checkAvailability(null, time));
        assertFalse(stadium.checkAvailability(date, null));
    }

    // S-02
    @Test
    void checkAvailability_noFixtures_returnsTrue() {
        assertTrue(stadium.checkAvailability(date, time));
    }

    // S-03
    @Test
    void checkAvailability_sameSlotAfterAdd_returnsFalse() {
        Fixtures f = makeFixture("F01", date, time);
        stadium.addFixture(f);
        assertFalse(stadium.checkAvailability(date, time));
    }

    // S-04
    @Test
    void checkAvailability_differentTimeSameDay_returnsTrue() {
        Fixtures f = makeFixture("F01", date, time);
        stadium.addFixture(f);
        assertTrue(stadium.checkAvailability(date, LocalTime.of(18, 0)));
    }

    // S-05
    @Test
    void addFixture_null_isNotAdded() {
        stadium.addFixture(null);
        assertTrue(stadium.checkAvailability(date, time));
    }

    // S-06
    @Test
    void addFixture_duplicate_isOnlyAddedOnce() {
        Fixtures f = makeFixture("F01", date, time);
        stadium.addFixture(f);
        stadium.addFixture(f);
        assertEquals(1, stadium.getFixturesList().size());
    }
    // S-08
    @Test
    void validStadium_capacityZero_returnsFalse() {
        stadium.setseatingCapacity(0);
        assertFalse(stadium.validStadium());
    }

    // S-09
    @Test
    void validStadium_capacityOne_returnsTrue() {
        stadium.setseatingCapacity(1);
        assertTrue(stadium.validStadium());
    }

    // S-10
    @Test
    void validStadium_priceZero_returnsTrue() {
        stadium.setticketPrice(0);
        assertTrue(stadium.validStadium());
    }

    // S-11
    @Test
    void validStadium_negativePrice_returnsFalse() {
        stadium.setticketPrice(-1);
        assertFalse(stadium.validStadium());
    }

    // S-12
    @Test
    void checkAvailability_oneMinuteLater_returnsTrue() {
        Fixtures f = makeFixture("F01", date, time);
        stadium.addFixture(f);
        assertTrue(stadium.checkAvailability(date, time.plusMinutes(1)));
    }

}