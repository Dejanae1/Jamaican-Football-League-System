import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;

public class TeamManager {

    private ArrayList<FootballTeam> teams = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Coach> coaches = new ArrayList<>();
    private ArrayList<MatchOfficial> officials = new ArrayList<>();

    // Register a football team
    public void registerTeam(FootballTeam team)
            throws DuplicateRegistrationException {

        if (team == null) {
            throw new IllegalArgumentException(
                "Team cannot be null."
            );
        }

        for (FootballTeam existing : teams) {

            if (existing.getTeamID().equalsIgnoreCase(
                    team.getTeamID())) {

                throw new DuplicateRegistrationException(
                    "Team ID " + team.getTeamID()
                    + " already exists."
                );
            }

            if (existing.getTeamName().equalsIgnoreCase(
                    team.getTeamName())) {

                throw new DuplicateRegistrationException(
                    "Team name " + team.getTeamName()
                    + " already exists."
                );
            }
        }

        team.setRegistered(true);
        team.setSuspended(false);
        teams.add(team);
    }

    // Register a player
    public void registerPlayer(Player player)
            throws DuplicateRegistrationException {

        if (player == null) {
            throw new IllegalArgumentException(
                "Player cannot be null."
            );
        }

        checkPersonIDUnused(player.getPersonID());
        players.add(player);
    }

    // Register a coach
    public void registerCoach(Coach coach)
            throws DuplicateRegistrationException {

        if (coach == null) {
            throw new IllegalArgumentException(
                "Coach cannot be null."
            );
        }

        checkPersonIDUnused(coach.getPersonID());
        coaches.add(coach);
    }

    // Register a match official
    public void registerOfficial(MatchOfficial official)
            throws DuplicateRegistrationException {

        if (official == null) {
            throw new IllegalArgumentException(
                "Official cannot be null."
            );
        }

        checkPersonIDUnused(official.getPersonID());
        officials.add(official);
    }

    // Check that a person ID is unique
    private void checkPersonIDUnused(String id)
            throws DuplicateRegistrationException {

        if (findPerson(id) != null) {
            throw new DuplicateRegistrationException(
                "Person ID " + id + " is already registered."
            );
        }
    }

    // Assign a player to a team
    public void assignPlayerToTeam(
            String playerID, String teamID)
            throws InvalidInputException,
                   DuplicateRegistrationException {

        Player player = findPlayer(playerID);
        FootballTeam team = findTeam(teamID);

        if (player == null) {
            throw new InvalidInputException(
                "No player with ID " + playerID + "."
            );
        }

        if (team == null) {
            throw new InvalidInputException(
                "No team with ID " + teamID + "."
            );
        }

        team.addPlayer(player);
    }

    // Assign a coach to a team
    public void assignCoachToTeam(
            String coachID, String teamID)
            throws InvalidInputException,
                   DuplicateRegistrationException {

        Coach coach = findCoach(coachID);
        FootballTeam team = findTeam(teamID);

        if (coach == null) {
            throw new InvalidInputException(
                "No coach with ID " + coachID + "."
            );
        }

        if (team == null) {
            throw new InvalidInputException(
                "No team with ID " + teamID + "."
            );
        }

        team.assignCoach(coach);
    }

    // Find a team by ID
    public FootballTeam findTeam(String teamID) {

        for (FootballTeam team : teams) {

            if (team.getTeamID().equalsIgnoreCase(teamID)) {
                return team;
            }
        }

        return null;
    }

    // Find a player by ID
    public Player findPlayer(String id) {

        for (Player player : players) {

            if (player.getPersonID().equalsIgnoreCase(id)) {
                return player;
            }
        }

        return null;
    }

    // Find a coach by ID
    public Coach findCoach(String id) {

        for (Coach coach : coaches) {

            if (coach.getPersonID().equalsIgnoreCase(id)) {
                return coach;
            }
        }

        return null;
    }

    // Find an official by ID
    public MatchOfficial findOfficial(String id) {

        for (MatchOfficial official : officials) {

            if (official.getPersonID().equalsIgnoreCase(id)) {
                return official;
            }
        }

        return null;
    }

    // Search all registered people
    public Person findPerson(String id) {

        Person found = findPlayer(id);

        if (found == null) {
            found = findCoach(id);
        }

        if (found == null) {
            found = findOfficial(id);
        }

        return found;
    }

    // Search players by name
    public ArrayList<Player> searchPlayersByName(String text) {

        ArrayList<Player> matches = new ArrayList<>();

        if (text == null) {
            return matches;
        }

        for (Player player : players) {

            if (player.getFullName().toLowerCase()
                    .contains(text.toLowerCase())) {

                matches.add(player);
            }
        }

        return matches;
    }

    // Return registered teams
    public ArrayList<FootballTeam> getTeams() {
        return new ArrayList<>(teams);
    }

    // Return registered players
    public ArrayList<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    // Return registered coaches
    public ArrayList<Coach> getCoaches() {
        return new ArrayList<>(coaches);
    }

    // Return registered officials
    public ArrayList<MatchOfficial> getOfficials() {
        return new ArrayList<>(officials);
    }

    // Display a team report
    public void printTeamReport() {

        System.out.println("=== TEAM REPORT ===");

        for (FootballTeam team : teams) {

            System.out.println(team);

            for (Player player : team.getPlayers()) {
                System.out.println("    " + player);
            }
        }
    }

    // Save information to a CSV file
    public void saveToFile(String fileName)
            throws IOException {

        try (PrintWriter out =
                new PrintWriter(new FileWriter(fileName))) {

            for (Coach coach : coaches) {
                out.println(coach.toCsv());
            }

            for (MatchOfficial official : officials) {
                out.println(official.toCsv());
            }

            for (Player player : players) {
                out.println(player.toCsv());
            }

            for (FootballTeam team : teams) {
                out.println(team.toCsv());
            }
        }
    }

    // Load information from a CSV file
    public void loadFromFile(String fileName)
            throws IOException,
                   InvalidInputException,
                   DuplicateRegistrationException {

        teams.clear();
        players.clear();
        coaches.clear();
        officials.clear();

        ArrayList<String[]> teamLines = new ArrayList<>();
        ArrayList<String[]> playerLines = new ArrayList<>();

        try (BufferedReader in =
                new BufferedReader(new FileReader(fileName))) {

            String line;

            while ((line = in.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] fields = line.split(",", -1);

                try {

                    switch (fields[0]) {

                        case "COACH":

                            registerCoach(
                                new Coach(
                                    fields[1],
                                    fields[2],
                                    fields[3],
                                    LocalDate.parse(fields[4]),
                                    fields[5],
                                    Integer.parseInt(fields[6])
                                )
                            );

                            break;

                        case "OFFICIAL":

                            MatchOfficial official =
                                new MatchOfficial(
                                    fields[1],
                                    fields[2],
                                    fields[3],
                                    LocalDate.parse(fields[4]),
                                    MatchOfficial.OfficialType.valueOf(
                                        fields[5]
                                    )
                                );

                            official.setMatchesOfficiated(
                                Integer.parseInt(fields[6])
                            );

                            registerOfficial(official);
                            break;

                        case "PLAYER":

                            Player player =
                                new Player(
                                    fields[1],
                                    fields[2],
                                    fields[3],
                                    LocalDate.parse(fields[4]),
                                    Integer.parseInt(fields[5]),
                                    Player.Position.valueOf(fields[6])
                                );

                            player.setGoalsScored(
                                Integer.parseInt(fields[7])
                            );

                            registerPlayer(player);
                            playerLines.add(fields);
                            break;

                        case "TEAM":

                            FootballTeam team =
                                new FootballTeam(
                                    fields[1],
                                    fields[2],
                                    fields[3]
                                );

                            registerTeam(team);
                            teamLines.add(fields);
                            break;

                        default:

                            throw new InvalidInputException(
                                "Unknown record type: " + fields[0]
                            );
                    }

                } catch (RuntimeException e) {

                    throw new InvalidInputException(
                        "Bad line in " + fileName + ": " + line
                    );
                }
            }
        }

        // Restore coach-to-team relationships
        for (String[] fields : teamLines) {

            if (fields.length > 4
                    && !fields[4].equals("NONE")) {

                assignCoachToTeam(fields[4], fields[1]);
            }
        }

        // Restore player-to-team relationships
        for (String[] fields : playerLines) {

            if (fields.length > 8
                    && !fields[8].equals("NONE")) {

                assignPlayerToTeam(fields[1], fields[8]);
            }
        }
    }
}
