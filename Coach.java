import java.time.LocalDate;

public class Coach extends Person
{
    // Attributes
    private String licenseLevel;
    private int yearsExperience;

    // Relationship with FootballTeam
    private FootballTeam team;

    // Default Constructor
    public Coach()
    {
        super();
        this.licenseLevel = "";
        this.yearsExperience = 0;
        this.team = null;
    }

    // Parameterized Constructor
    public Coach(String personID, String firstName, String lastName, LocalDate dateOfBirth,
                 String licenseLevel, int yearsExperience) throws InvalidInputException
    {
        super(personID, firstName, lastName, dateOfBirth);
        setLicenseLevel(licenseLevel);
        setYearsExperience(yearsExperience);
        this.team = null;
    }

    // Mutator Methods
    public void setLicenseLevel(String licenseLevel) throws InvalidInputException
    {
        this.licenseLevel = Validation.requireText(licenseLevel, "License level");
    }
    public void setYearsExperience(int yearsExperience) throws InvalidInputException
    {
        this.yearsExperience = Validation.requireRange(yearsExperience, 0, 60, "Years of experience");
    }
    public void setTeam(FootballTeam team)
    {
        this.team = team;
    }

    // Accessor Methods
    public String getLicenseLevel()
    {
        return licenseLevel;
    }
    public int getYearsExperience()
    {
        return yearsExperience;
    }
    public FootballTeam getTeam()
    {
        return team;
    }

    @Override
    public String getRole()
    {
        return "Coach";
    }

    @Override
    public String toCsv()
    {
        return "COACH," + getPersonID() + "," + getFirstName() + "," + getLastName() + ","
                + getDateOfBirth() + "," + licenseLevel + "," + yearsExperience;
    }

    @Override
    public String toString()
    {
        String teamName = (team == null) ? "no team" : team.getTeamName();
        return super.toString() + ", license: " + licenseLevel + ", " + yearsExperience
                + " yrs experience, team: " + teamName;
    }
}