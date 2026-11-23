/** Placeholder base exception. Member A owns the real one: delete this class (keep the four
 *  below) and change their "extends LeagueException" to extend A's base class instead. */
public class LeagueException extends Exception {
    public LeagueException(String message) { super(message); }
}

/** Negative quantity/price, bad format, duplicate id, etc. */
class InvalidDataException extends LeagueException {
    public InvalidDataException(String message) { super(message); }
}

/** Ticket sales would exceed stadium seating capacity. */
class CapacityExceededException extends LeagueException {
    public CapacityExceededException(String message) { super(message); }
}

/** Payment is less than the total due. */
class InsufficientPaymentException extends LeagueException {
    public InsufficientPaymentException(String message) { super(message); }
}

/** Not enough stock for the requested quantity. */
class InsufficientStockException extends LeagueException {
    public InsufficientStockException(String message) { super(message); }
}
