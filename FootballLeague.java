
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FootballLeague {

    private final List<FootballTeam> teams = new ArrayList<>();
    private final List<Fixtures> fixtures = new ArrayList<>();
    private final List<Stadium> stadiums = new ArrayList<>();

    // Add a team to the league
    public void addTeam(FootballTeam team)
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
                    "Team ID already exists: " + team.getTeamID()
                );
            }

            if (existing.getTeamName().equalsIgnoreCase(
                    team.getTeamName())) {

                throw new DuplicateRegistrationException(
                    "Team name already exists: " + team.getTeamName()
                );
            }
        }

        team.setRegistered(true);
        teams.add(team);
    }

    // Return all teams
    public List<FootballTeam> getTeams() {
        return new ArrayList<>(teams);
    }

    // Register a stadium
    public void addStadium(Stadium stadium)
            throws LeagueException {

        if (stadium == null || !stadium.validStadium()) {
            throw new InvalidDataException(
                "Stadium information is invalid."
            );
        }

        if (findStadium(stadium.getstadiumID1()) != null) {
            throw new DuplicateRegistrationException(
                "Stadium ID already exists: "
                + stadium.getstadiumID1()
            );
        }

        stadiums.add(stadium);
    }

    // Find a stadium by ID
    public Stadium findStadium(String id) {

        if (id == null) {
            return null;
        }

        for (Stadium stadium : stadiums) {

            if (stadium.getstadiumID1().equalsIgnoreCase(id)) {
                return stadium;
            }
        }

        return null;
    }

    // Return all registered stadiums
    public List<Stadium> getStadiums() {
        return new ArrayList<>(stadiums);
    }

    // Schedule a fixture
    public void scheduleFixture(Fixtures fixture)
            throws LeagueException {

        if (fixture == null || !fixture.validateFixture()) {
            throw new InvalidDataException(
                "Fixture is incomplete or invalid."
            );
        }

        if (findFixture(fixture.getFixtureID()) != null) {
            throw new InvalidDataException(
                "Fixture ID already used: "
                + fixture.getFixtureID()
            );
        }

        if (!teams.contains(fixture.getHomeTeam())
                || !teams.contains(fixture.getAwayTeam())) {

            throw new TeamNotEligibleException(
                "Both teams must be registered with the league."
            );
        }

        if (!fixture.getHomeTeam().isEligible()
                || !fixture.getAwayTeam().isEligible()) {

            throw new TeamNotEligibleException(
                "Unregistered or suspended teams cannot be scheduled."
            );
        }

        for (Fixtures existing : fixtures) {

            boolean sameSlot =
                existing.getMatchDate().equals(
                    fixture.getMatchDate()
                )
                && existing.getKickOffTime().equals(
                    fixture.getKickOffTime()
                );

            if (!sameSlot) {
                continue;
            }

            if (involves(existing, fixture.getHomeTeam())
                    || involves(existing, fixture.getAwayTeam())) {

                throw new SchedulingConflictException(
                    "A team already plays at that date and time."
                );
            }

            if (existing.getMatchOfficial().equals(
                    fixture.getMatchOfficial())) {

                throw new SchedulingConflictException(
                    "Official is already assigned at that time."
                );
            }

            if (existing.getStadium().equals(
                    fixture.getStadium())) {

                throw new SchedulingConflictException(
                    "Stadium is already booked at that time."
                );
            }
        }

        fixtures.add(fixture);

        // Add the scheduled fixture to the stadium
        fixture.getStadium().addFixture(fixture);
    }

    // Check whether a team is involved in an existing fixture
    private boolean involves(Fixtures fixture, FootballTeam team) {

        return fixture.getHomeTeam() == team
                || fixture.getAwayTeam() == team;
    }

    // Find a fixture by ID
    public Fixtures findFixture(String id) {

        if (id == null) {
            return null;
        }

        for (Fixtures fixture : fixtures) {

            if (fixture.getFixtureID().equalsIgnoreCase(id)) {
                return fixture;
            }
        }

        return null;
    }

    // Return all fixtures
    public List<Fixtures> getFixtures() {
        return new ArrayList<>(fixtures);
    }

    // Record a match result and update league points
    public void recordResult(String fixtureId, int home, int away)
            throws LeagueException {

        Fixtures fixture = findFixture(fixtureId);

        if (fixture == null) {
            throw new InvalidDataException(
                "Unknown fixture: " + fixtureId
            );
        }

        if (home < 0 || away < 0) {
            throw new InvalidScoreException(
                "Scores cannot be negative."
            );
        }

        if (fixture.getMatchResult() != null) {
            throw new InvalidDataException(
                "Result already recorded for " + fixtureId
            );
        }

        fixture.recordResult(home, away);

        if (fixture.getMatchResult() == null) {
            throw new InvalidScoreException(
                "The match result could not be recorded."
            );
        }

        if (home > away) {
            fixture.getHomeTeam().addPoints(3);

        } else if (away > home) {
            fixture.getAwayTeam().addPoints(3);

        } else {
            fixture.getHomeTeam().addPoints(1);
            fixture.getAwayTeam().addPoints(1);
        }
    }

    // Generate league standings
    public List<FootballTeam> getStandings() {

        List<FootballTeam> table =
                new ArrayList<>(teams);

        table.sort(
            Comparator.comparingInt(
                FootballTeam::getPoints
            ).reversed()
            .thenComparing(FootballTeam::getTeamName)
        );

        return table;
    }

    // Display league standings
    public String standingsReport() {

        StringBuilder report =
                new StringBuilder("--- LEAGUE STANDINGS ---\n");

        int position = 1;

        for (FootballTeam team : getStandings()) {

            report.append(
                String.format(
                    "%2d. %-25s %3d pts%n",
                    position++,
                    team.getTeamName(),
                    team.getPoints()
                )
            );
        }

        return report.toString();
    }
}
