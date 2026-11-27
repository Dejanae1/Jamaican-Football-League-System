import java.util.ArrayList;

public class FootballTeam {

    public static final int MAX_SQUAD_SIZE = 25;

    private String teamID;
    private String teamName;
    private String homeTown;

    private Coach coach;
    private ArrayList<Player> players = new ArrayList<>();

    // League information
    private int points = 0;
    private boolean registered = false;
    private boolean suspended = false;

    // Default constructor
    public FootballTeam() {
        this.teamID = "";
        this.teamName = "";
        this.homeTown = "";
        this.coach = null;
    }

    // Parameterized constructor
    public FootballTeam(String teamID, String teamName, String homeTown)
            throws InvalidInputException {

        setTeamID(teamID);
        setTeamName(teamName);
        setHomeTown(homeTown);
        this.coach = null;
    }

    // Mutator methods
    public void setTeamID(String teamID) throws InvalidInputException {
        this.teamID = Validation.requireText(teamID, "Team ID");
    }

    public void setTeamName(String teamName) throws InvalidInputException {
        this.teamName = Validation.requireText(teamName, "Team name");
    }

    public void setHomeTown(String homeTown) throws InvalidInputException {
        this.homeTown = Validation.requireText(homeTown, "Home town");
    }

    // Accessor methods
    public String getTeamID() {
        return teamID;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getHomeTown() {
        return homeTown;
    }

    public Coach getCoach() {
        return coach;
    }

    public ArrayList<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public int getSquadSize() {
        return players.size();
    }

    // League points
    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        if (points < 0) {
            throw new IllegalArgumentException(
                "Points cannot be negative."
            );
        }

        this.points += points;
    }

    // Registration and suspension
    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    public boolean isEligible() {
        return registered && !suspended;
    }

    // Add a player to the team
    public void addPlayer(Player player)
            throws InvalidInputException, DuplicateRegistrationException {

        if (player == null) {
            throw new InvalidInputException(
                "Player cannot be empty."
            );
        }

        if (players.size() >= MAX_SQUAD_SIZE) {
            throw new InvalidInputException(
                teamName + " already has the maximum of "
                + MAX_SQUAD_SIZE + " players."
            );
        }

        if (player.getTeam() != null) {
            throw new DuplicateRegistrationException(
                player.getFullName() + " is already registered to "
                + player.getTeam().getTeamName() + "."
            );
        }

        for (Player p : players) {
            if (p.getJerseyNumber() == player.getJerseyNumber()) {
                throw new DuplicateRegistrationException(
                    "Jersey number " + player.getJerseyNumber()
                    + " is already used on " + teamName + "."
                );
            }
        }

        players.add(player);
        player.setTeam(this);
    }

    // Remove a player
    public boolean removePlayer(String playerID) {

        for (Player p : players) {
            if (p.getPersonID().equals(playerID)) {
                players.remove(p);
                p.setTeam(null);
                return true;
            }
        }

        return false;
    }

    // Find a player
    public Player findPlayerByID(String playerID) {

        for (Player p : players) {
            if (p.getPersonID().equals(playerID)) {
                return p;
            }
        }

        return null;
    }

    // Assign a coach
    public void assignCoach(Coach newCoach)
            throws InvalidInputException, DuplicateRegistrationException {

        if (newCoach == null) {
            throw new InvalidInputException(
                "Coach cannot be empty."
            );
        }

        if (newCoach.getTeam() != null
                && newCoach.getTeam() != this) {

            throw new DuplicateRegistrationException(
                newCoach.getFullName() + " already coaches "
                + newCoach.getTeam().getTeamName() + "."
            );
        }

        if (coach != null) {
            coach.setTeam(null);
        }

        coach = newCoach;
        newCoach.setTeam(this);
    }

    // Convert team information to CSV
    public String toCsv() {

        String coachID = (coach == null)
                ? "NONE"
                : coach.getPersonID();

        return "TEAM," + teamID + "," + teamName + ","
                + homeTown + "," + coachID;
    }

    @Override
    public String toString() {

        String coachName = (coach == null)
                ? "no coach"
                : coach.getFullName();

        return teamName + " [" + teamID + "] from " + homeTown
                + ", coach: " + coachName
                + ", squad: " + players.size()
                + "/" + MAX_SQUAD_SIZE
                + ", points: " + points
                + ", status: "
                + (suspended ? "Suspended" : "Active");
    }
}
