public class LeagueException extends Exception {

    public LeagueException(String message) {
        super(message);
    }
}

// Invalid or incomplete data
class InvalidDataException extends LeagueException {

    public InvalidDataException(String message) {
        super(message);
    }
}

// Invalid match score
class InvalidScoreException extends LeagueException {

    public InvalidScoreException(String message) {
        super(message);
    }
}

// Team is not eligible to play
class TeamNotEligibleException extends LeagueException {

    public TeamNotEligibleException(String message) {
        super(message);
    }
}

// Scheduling conflict
class SchedulingConflictException extends LeagueException {

    public SchedulingConflictException(String message) {
        super(message);
    }
}

// Ticket sales exceed stadium capacity
class CapacityExceededException extends LeagueException {

    public CapacityExceededException(String message) {
        super(message);
    }
}

// Payment is insufficient
class InsufficientPaymentException extends LeagueException {

    public InsufficientPaymentException(String message) {
        super(message);
    }
}

// Requested stock is unavailable
class InsufficientStockException extends LeagueException {

    public InsufficientStockException(String message) {
        super(message);
    }
}

// Duplicate registration
class DuplicateRegistrationException extends LeagueException {

    public DuplicateRegistrationException(String message) {
        super(message);
    }
}
