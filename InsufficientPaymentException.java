/** Payment is less than the total due. */
public class InsufficientPaymentException extends LeagueException {
    public InsufficientPaymentException(String message) { super(message); }
}
