import java.time.LocalDate;


final class Validation
{
    private Validation() { }

    
    public static String requireText(String value, String fieldName) throws InvalidInputException
    {
        if (value == null || value.trim().isEmpty())
        {
            throw new InvalidInputException(fieldName + " cannot be blank.");
        }
        if (value.contains(","))
        {
            throw new InvalidInputException(fieldName + " cannot contain a comma.");
        }
        return value.trim();
    }

    public static int requireRange(int value, int min, int max, String fieldName) throws InvalidInputException
    {
        if (value < min || value > max)
        {
            throw new InvalidInputException(fieldName + " must be between " + min + " and " + max + ".");
        }
        return value;
    }

    public static LocalDate requirePastDate(LocalDate date, String fieldName) throws InvalidInputException
    {
        if (date == null || !date.isBefore(LocalDate.now()))
        {
            throw new InvalidInputException(fieldName + " must be a date in the past.");
        }
        return date;
    }
}

class InvalidInputException extends Exception
{
    public InvalidInputException(String message)
    {
        super(message);
    }
}

class DuplicateRegistrationException extends Exception
{
    public DuplicateRegistrationException(String message)
    {
        super(message);
    }
}