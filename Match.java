import java.time.LocalDate;

public class MatchOfficial extends Person
{
    // Official Type Enum
    public enum OfficialType
    {
        REFEREE, ASSISTANT_REFEREE, FOURTH_OFFICIAL
    }

    // Attributes
    private OfficialType officialType;
    private int matchesOfficiated;

    // Default Constructor
    public MatchOfficial()
    {
        super();
        this.officialType = OfficialType.REFEREE;
        this.matchesOfficiated = 0;
    }

    // Parameterized Constructor
    public MatchOfficial(String personID, String firstName, String lastName, LocalDate dateOfBirth,
                         OfficialType officialType) throws InvalidInputException
    {
        super(personID, firstName, lastName, dateOfBirth);
        setOfficialType(officialType);
        this.matchesOfficiated = 0;
    }

    // Mutator Methods
    public void setOfficialType(OfficialType officialType) throws InvalidInputException
    {
        if (officialType == null)
        {
            throw new InvalidInputException("Official type cannot be empty.");
        }
        this.officialType = officialType;
    }
    public void setMatchesOfficiated(int matchesOfficiated) throws InvalidInputException
    {
        this.matchesOfficiated = Validation.requireRange(matchesOfficiated, 0, 10000, "Matches officiated");
    }

    // Accessor Methods
    public OfficialType getOfficialType()
    {
        return officialType;
    }
    public int getMatchesOfficiated()
    {
        return matchesOfficiated;
    }

    @Override
    public String getRole()
    {
        return "Match Official";
    }

    @Override
    public String toCsv()
    {
        return "OFFICIAL," + getPersonID() + "," + getFirstName() + "," + getLastName() + ","
                + getDateOfBirth() + "," + officialType + "," + matchesOfficiated;
    }

    @Override
    public String toString()
    {
        return super.toString() + ", " + officialType + ", matches: " + matchesOfficiated;
    }
}