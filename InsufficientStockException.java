/** Not enough stock for the requested quantity. */
public class InsufficientStockException extends LeagueException {
    public InsufficientStockException(String message) { super(message); }
}
