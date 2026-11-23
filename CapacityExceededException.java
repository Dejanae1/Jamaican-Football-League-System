/** Ticket sales would exceed stadium seating capacity. */
public class CapacityExceededException extends LeagueException {
    public CapacityExceededException(String message) { super(message); }
}
