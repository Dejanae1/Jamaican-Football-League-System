import java.time.LocalDate;

final class Validation {

    private Validation() {
        // Prevents objects from being created from this utility class.
    }

    // Check that text is not blank and does not contain commas
    public static String requireText(
            String value, String fieldName)
            throws InvalidInputException {

        if (value == null || value.trim().isEmpty()) {
            throw new InvalidInputException(
                fieldName + " cannot be blank."
            );
        }

        if (value.contains(",")) {
            throw new InvalidInputException(
                fieldName + " cannot contain a comma."
            );
        }

        return value.trim();
    }

    // Check that a number is within an allowed range
    public static int requireRange(
            int value, int min, int max, String fieldName)
            throws InvalidInputException {

        if (value < min || value > max) {
            throw new InvalidInputException(
                fieldName + " must be between "
                + min + " and " + max + "."
            );
        }

        return value;
    }

    // Check that a date is in the past
    public static LocalDate requirePastDate(
            LocalDate date, String fieldName)
            throws InvalidInputException {

        if (date == null || !date.isBefore(LocalDate.now())) {
            throw new InvalidInputException(
                fieldName + " must be a date in the past."
            );
        }

        return date;
    }
}

// Custom exception for invalid input
class InvalidInputException extends LeagueException {

    public InvalidInputException(String message) {
        super(message);
    }
}
