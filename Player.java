import java.time.LocalDate;

public class Player extends Person
{
    public enum Position
    {
        GOALKEEPER, DEFENDER, MIDFIELDER, FORWARD
    }

    // Attributes
    private int jerseyNumber;
    private Position position;
    private int goalsScored;

    // Relationship with FootballTeam (a player belongs to at most one team)
    private FootballTeam team;

    // Default Constructor
    public Player()
    {
        super();
        this.jerseyNumber = 0;
        this.position = Position.MIDFIELDER;
        this.goalsScored = 0;
        this.team = null;
    }

    // Parameterized Constructor
    public Player(String personID, String firstName, String lastName, LocalDate dateOfBirth,
                  int jerseyNumber, Position position) throws InvalidInputException
    {
        super(personID, firstName, lastName, dateOfBirth);
        setJerseyNumber(jerseyNumber);
        setPosition(position);
        this.goalsScored = 0;
        this.team = null;
    }

    // Mutator Methods
    public void setJerseyNumber(int jerseyNumber) throws InvalidInputException
    {
        this.jerseyNumber = Validation.requireRange(jerseyNumber, 1, 99, "Jersey number");
    }
    public void setPosition(Position position) throws InvalidInputException
    {
        if (position == null)
        {
            throw new InvalidInputException("Position cannot be empty.");
        }
        this.position = position;
    }
    public void setGoalsScored(int goalsScored) throws InvalidInputException
    {
        this.goalsScored = Validation.requireRange(goalsScored, 0, 10000, "Goals scored");
    }
    public void addGoals(int goals) throws InvalidInputException
    {
        setGoalsScored(goalsScored + goals);
    }
    public void setTeam(FootballTeam team)
    {
        this.team = team;
    }

    // Accessor Methods
    public int getJerseyNumber()
    {
        return jerseyNumber;
    }
    public Position getPosition()
    {
        return position;
    }
    public int getGoalsScored()
    {
        return goalsScored;
    }
    public FootballTeam getTeam()
    {
        return team;
    }

    @Override
    public String getRole()
    {
        return "Player";
    }

    @Override
    public String toCsv()
    {
        String teamID = (team == null) ? "NONE" : team.getTeamID();
        return "PLAYER," + getPersonID() + "," + getFirstName() + "," + getLastName() + ","
                + getDateOfBirth() + "," + jerseyNumber + "," + position + "," + goalsScored + "," + teamID;
    }

    @Override
    public String toString()
    {
        String teamName = (team == null) ? "no team" : team.getTeamName();
        return super.toString() + ", #" + jerseyNumber + " " + position + ", goals: " + goalsScored
                + ", team: " + teamName;
    }
}