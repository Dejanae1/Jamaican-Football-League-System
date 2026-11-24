import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FootballTeamTest {

    private FootballTeam team;

    @BeforeEach
    void setUp() {
        team = new FootballTeam("Test FC");
    }

    // Each player needs a different jersey number (1 to 99), or the team
    // rejects the second one with a DuplicateRegistrationException.
    private void addPlayers(int count) throws Exception {
        for (int i = 1; i <= count; i++) {
            team.addPlayer(new Player("PL" + i, "First" + i, "Last" + i,
                    LocalDate.now().minusYears(20), i, Player.Position.FORWARD));
        }
    }

    // TM-01
    @Test
    void squadOf24IsAccepted() throws Exception {
        addPlayers(24);
        assertEquals(24, team.getSquadSize());
    }

    // TM-02
    @Test
    void squadOfExactly25IsAccepted() throws Exception {
        addPlayers(FootballTeam.MAX_SQUAD_SIZE);
        assertEquals(25, team.getSquadSize());
    }

    // TM-03
    @Test
    void squadOf26IsRejected() {
        assertThrows(InvalidInputException.class, () -> addPlayers(26));
    }

    // TM-04
    @Test
    void addingToFullTeamFailsAndSquadStays25() throws Exception {
        addPlayers(FootballTeam.MAX_SQUAD_SIZE);
        assertThrows(InvalidInputException.class,
                () -> team.addPlayer(new Player("PL26", "Extra", "Player",
                        LocalDate.now().minusYears(20), 26, Player.Position.FORWARD)));
        assertEquals(25, team.getSquadSize());
    }

    // TM-05
    @Test
    void emptySquadDoesNotCrash() {
        assertEquals(0, team.getSquadSize());
    }

    // TM-06
    @Test
    void maxSquadSizeConstantIs25() {
        assertEquals(25, FootballTeam.MAX_SQUAD_SIZE);
    }
}