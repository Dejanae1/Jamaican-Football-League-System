/**
 * The match details a ticket needs. Member C's Fixture/Stadium supply these values
 * when the fixture is created (teams, date and time, stadium name, seating capacity).
 */
public class MatchInfo {
    private final String fixtureId;
    private final String matchName;       // e.g. "Tivoli vs Harbour View"
    private final String matchDateTime;   // e.g. "2026-12-05 15:00"
    private final String stadiumName;
    private final int capacity;

    public MatchInfo(String fixtureId, String matchName, String matchDateTime,
                     String stadiumName, int capacity) throws InvalidDataException {
        FileManager.checkField(fixtureId, "Fixture id");
        FileManager.checkField(matchName, "Match name");
        FileManager.checkField(matchDateTime, "Match date and time");
        FileManager.checkField(stadiumName, "Stadium name");
        if (capacity <= 0) {
            throw new InvalidDataException("Seating capacity must be greater than zero");
        }
        this.fixtureId = fixtureId.trim();
        this.matchName = matchName.trim();
        this.matchDateTime = matchDateTime.trim();
        this.stadiumName = stadiumName.trim();
        this.capacity = capacity;
    }

    public String getFixtureId() { return fixtureId; }
    public String getMatchName() { return matchName; }
    public String getMatchDateTime() { return matchDateTime; }
    public String getStadiumName() { return stadiumName; }
    public int getCapacity() { return capacity; }

    @Override
    public String toString() {
        return fixtureId + ": " + matchName + ", " + matchDateTime + ", " + stadiumName;
    }
}
