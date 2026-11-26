import java.util.*;

public class FootballLeague {
    private final List<FootballTeam> teams = new ArrayList<>();
    private final List<Fixtures> fixtures = new ArrayList<>();

    public void addTeam(FootballTeam t) { teams.add(t); }

    public void scheduleFixture(Fixtures f) throws LeagueException {
        if (f == null || !f.validateFixture())
            throw new InvalidDataException("Fixture is incomplete or invalid.");
        for (Fixtures g : fixtures)
            if (g.getFixtureID().equals(f.getFixtureID()))
                throw new InvalidDataException("Fixture ID already used: " + f.getFixtureID());

        if (!f.getHomeTeam().isEligible() || !f.getAwayTeam().isEligible())
            throw new TeamNotEligibleException(
                "Unregistered or suspended teams cannot be scheduled.");

        for (Fixtures g : fixtures) {
            boolean sameSlot = g.getMatchDate().equals(f.getMatchDate())
                            && g.getKickOffTime().equals(f.getKickOffTime());
            if (!sameSlot) continue;
            if (involves(g, f.getHomeTeam()) || involves(g, f.getAwayTeam()))
                throw new SchedulingConflictException("A team already plays at that date and time.");
            if (g.getMatchOfficial().equals(f.getMatchOfficial()))
                throw new SchedulingConflictException("Official is already assigned at that time.");
            if (g.getStadium() == f.getStadium())
                throw new SchedulingConflictException("Stadium is already booked at that time.");
        }
        fixtures.add(f);
    }

    private boolean involves(Fixtures g, FootballTeam t) {
        return g.getHomeTeam() == t || g.getAwayTeam() == t;
    }

    public Fixtures findFixture(String id) {
        for (Fixtures f : fixtures) if (f.getFixtureID().equals(id)) return f;
        return null;
    }

    public void recordResult(String fixtureId, int home, int away) throws LeagueException {
        Fixtures f = findFixture(fixtureId);
        if (f == null) throw new InvalidDataException("Unknown fixture: " + fixtureId);
        if (home < 0 || away < 0) throw new InvalidScoreException("Scores cannot be negative.");
        if (f.getMatchResult() != null)
            throw new InvalidDataException("Result already recorded for " + fixtureId);

        f.recordResult(home, away);
        if (home > away)      f.getHomeTeam().addPoints(3);
        else if (away > home) f.getAwayTeam().addPoints(3);
        else { f.getHomeTeam().addPoints(1); f.getAwayTeam().addPoints(1); }
    }

    public List<FootballTeam> getStandings() {
        List<FootballTeam> table = new ArrayList<>(teams);
        table.sort(Comparator.comparingInt(FootballTeam::getPoints).reversed()
                             .thenComparing(FootballTeam::getTeamName));
        return table;
    }

    public String standingsReport() {
        StringBuilder sb = new StringBuilder("--- LEAGUE STANDINGS ---\n");
        int pos = 1;
        for (FootballTeam t : getStandings())
            sb.append(String.format("%2d. %-25s %3d pts%n", pos++, t.getTeamName(), t.getPoints()));
        return sb.toString();
    }
}
