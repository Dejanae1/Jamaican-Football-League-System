/** Negative quantity/price, bad format, duplicate id, etc.. */
public class InvalidDataException extends LeagueException {
    public InvalidDataException(String message) { super(message); }
}
