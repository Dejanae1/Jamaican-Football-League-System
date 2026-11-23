import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class FixturesTest {

    private LocalDate date;
    private LocalTime time;
    private FootballTeam home;
    private FootballTeam away;
    private Stadium stadium;
    private MatchOfficial official;

    @BeforeEach
    void setUp() throws Exception {
        date = LocalDate.of(2026, 12, 5);
        time = LocalTime.of(15, 0);
        home = new FootballTeam("T01", "Waterhouse", "Kingston");
        away = new FootballTeam("T02", "Portmore United", "Portmore");
        stadium = new Stadium();
        official = new MatchOfficial("P01", "Ann", "Brown", LocalDate.of(1985, 3, 12), "Referee");
    }

    private Fixtures validFixture() {
        return new Fixtures("F01", date, time, home, away, stadium, official);
    }

    // F-01
    @Test
    void validateFixture_allValid_returnsTrue() {
        assertTrue(validFixture().validateFixture());
    }

    // F-02
    @Test
    void validateFixture_nullId_returnsFalse() {
        Fixtures f = new Fixtures(null, date, time, home, away, stadium, official);
        assertFalse(f.validateFixture());
    }

    // F-03
    @Test
    void validateFixture_nullDate_returnsFalse() {
        Fixtures f = new Fixtures("F01", null, time, home, away, stadium, official);
        assertFalse(f.validateFixture());
    }

    // F-04
    @Test
    void validateFixture_nullTime_returnsFalse() {
        Fixtures f = new Fixtures("F01", date, null, home, away, stadium, official);
        assertFalse(f.validateFixture());
    }

    // F-05
    @Test
    void validateFixture_nullHomeTeam_returnsFalse() {
        Fixtures f = new Fixtures("F01", date, time, null, away, stadium, official);
        assertFalse(f.validateFixture());
    }

    // F-06
    @Test
    void validateFixture_nullAwayTeam_returnsFalse() {
        Fixtures f = new Fixtures("F01", date, time, home, null, stadium, official);
        assertFalse(f.validateFixture());
    }

    // F-07
    @Test
    void validateFixture_sameTeam_returnsFalse() {
        Fixtures f = new Fixtures("F01", date, time, home, home, stadium, official);
        assertFalse(f.validateFixture());
    }

    // F-08
    @Test
    void validateFixture_nullStadium_returnsFalse() {
        Fixtures f = new Fixtures("F01", date, time, home, away, null, official);
        assertFalse(f.validateFixture());
    }

    // F-09
    @Test
    void validateFixture_nullOfficial_returnsFalse() {
        Fixtures f = new Fixtures("F01", date, time, home, away, stadium, null);
        assertFalse(f.validateFixture());
    }

    // F-10
    @Test
    void validateFixture_emptyId_returnsFalse() {
        Fixtures f = new Fixtures("", date, time, home, away, stadium, official);
        assertFalse(f.validateFixture());
    }
}