import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    private LocalDate dob;

    @BeforeEach
    void setUp() {
        dob = LocalDate.now().minusYears(20);
    }

    private Player makePlayer(int jersey, Player.Position position) throws Exception {
        return new Player("PL01", "Ryan", "Clarke", dob, jersey, position);
    }

    // P-01
    @Test
    void create_validPlayer_isCreated() throws Exception {
        Player p = makePlayer(10, Player.Position.FORWARD);
        assertEquals(10, p.getJerseyNumber());
    }

    // P-02
    @Test
    void jersey_atMinimum_accepted() throws Exception {
        assertEquals(1, makePlayer(1, Player.Position.FORWARD).getJerseyNumber());
    }

    // P-03
    @Test
    void jersey_atMaximum_accepted() throws Exception {
        assertEquals(99, makePlayer(99, Player.Position.FORWARD).getJerseyNumber());
    }

    // P-04
    @Test
    void jersey_belowMinimum_throws() {
        assertThrows(InvalidInputException.class, () -> makePlayer(0, Player.Position.FORWARD));
    }

    // P-05
    @Test
    void jersey_aboveMaximum_throws() {
        assertThrows(InvalidInputException.class, () -> makePlayer(100, Player.Position.FORWARD));
    }

    // P-06
    @Test
    void jersey_negative_throws() {
        assertThrows(InvalidInputException.class, () -> makePlayer(-5, Player.Position.FORWARD));
    }

    // P-07
    @Test
    void position_null_throws() {
        assertThrows(InvalidInputException.class, () -> makePlayer(10, null));
    }

    // P-09
    @Test
    void dateOfBirth_future_throws() {
        assertThrows(InvalidInputException.class, () ->
                new Player("PL01", "Ryan", "Clarke", LocalDate.now().plusDays(1), 10, Player.Position.FORWARD));
    }

    // P-10
    @Test
    void getAge_twentyYears_returnsTwenty() throws Exception {
        assertEquals(20, makePlayer(10, Player.Position.FORWARD).getAge());
    }

    // P-11
    @Test
    void getAge_dayBeforeBirthday_returnsNineteen() throws Exception {
        Player p = new Player("PL01", "Ryan", "Clarke",
                LocalDate.now().minusYears(20).plusDays(1), 10, Player.Position.FORWARD);
        assertEquals(19, p.getAge());
    }
}