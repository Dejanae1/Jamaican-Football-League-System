import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;

public class ExceptionDemo
{
    private interface Action
    {
        void run() throws Exception;
    }

    private static int failures = 0;

    private static void expect(String rule, Class<? extends Exception> expected, Action action)
    {
        try
        {
            action.run();
            failures++;
            System.out.println("FAIL  " + rule + "  (nothing was thrown)");
        }
        catch (Exception e)
        {
            if (expected.isInstance(e))
            {
                System.out.println("PASS  " + rule + "\n        -> " + e.getClass().getSimpleName() + ": " + e.getMessage());
            }
            else
            {
                failures++;
                System.out.println("FAIL  " + rule + "  (got " + e.getClass().getSimpleName() + ": " + e.getMessage() + ")");
            }
        }
    }

    private static void check(String what, boolean ok)
    {
        if (!ok)
        {
            failures++;
        }
        System.out.println((ok ? "PASS  " : "FAIL  ") + what);
    }

    public static void main(String[] args) throws Exception
    {
        LeagueSystem sys = SampleData.build();
        TeamManager tm = sys.getTeamManager();
        FootballLeague league = sys.getLeague();
        LocalDate day = LocalDate.now().plusDays(30);
        LocalTime time = LocalTime.of(14, 0);

        System.out.println("=== BUSINESS RULE DEMONSTRATION ===\n");

        // 1. unregistered team
        FootballTeam newTeam = new FootballTeam("T05", "Waterhouse Rovers", "Kingston");
        tm.registerTeam(newTeam);
        expect("Unregistered team cannot be assigned to a fixture", TeamNotEligibleException.class,
                () -> sys.scheduleFixture(new Fixtures("X1", day, time, newTeam, tm.findTeam("T01"),
                        league.findStadium("S01"), tm.findOfficial("O03"))));

        // 2. suspended team
        tm.findCommissioner("LC01").suspendTeam(tm.findTeam("T04"));
        expect("Suspended team cannot be assigned to a fixture", TeamNotEligibleException.class,
                () -> sys.scheduleFixture(new Fixtures("X2", day, time, tm.findTeam("T04"), tm.findTeam("T01"),
                        league.findStadium("S01"), tm.findOfficial("O03"))));
        tm.findCommissioner("LC01").approveTeam(tm.findTeam("T04"));

        // 3-5. conflicts: set up one fixture, then try to clash with it
        sys.scheduleFixture(new Fixtures("X3", day, time, tm.findTeam("T01"), tm.findTeam("T02"),
                league.findStadium("S01"), tm.findOfficial("O03")));
        expect("A team cannot play two fixtures at the same day and time", SchedulingConflictException.class,
                () -> sys.scheduleFixture(new Fixtures("X4", day, time, tm.findTeam("T01"), tm.findTeam("T03"),
                        league.findStadium("S02"), tm.findOfficial("O02"))));
        expect("An official cannot be assigned to conflicting fixtures", SchedulingConflictException.class,
                () -> sys.scheduleFixture(new Fixtures("X5", day, time, tm.findTeam("T03"), tm.findTeam("T04"),
                        league.findStadium("S02"), tm.findOfficial("O03"))));
        expect("A stadium cannot host two fixtures at the same day and time", SchedulingConflictException.class,
                () -> sys.scheduleFixture(new Fixtures("X6", day, time, tm.findTeam("T03"), tm.findTeam("T04"),
                        league.findStadium("S01"), tm.findOfficial("O02"))));

        // 6-7. scores and completion
        expect("Match scores cannot be negative", InvalidScoreException.class,
                () -> league.recordResult("X3", -1, 2));
        expect("A fixture cannot be completed until a result is recorded", FixtureNotCompletableException.class,
                () -> league.markCompleted("X3"));

        // 8-10. player rules
        expect("Players must satisfy the minimum age", UnderageException.class,
                () ->
                {
                    Player kid = new Player("PX1", "Young", "Talent", LocalDate.now().minusYears(12), 30,
                            Player.Position.FORWARD);
                    tm.registerPlayer(kid);
                    tm.assignPlayerToTeam("PX1", "T01");
                });
        expect("A squad cannot exceed the maximum size", SquadFullException.class,
                () ->
                {
                    for (int i = 0; i < FootballTeam.MAX_SQUAD_SIZE; i++)
                    {
                        String id = "PF" + i;
                        tm.registerPlayer(new Player(id, "Filler", "Player" + i, LocalDate.of(1995, 1, 1),
                                13 + i, Player.Position.MIDFIELDER));
                        tm.assignPlayerToTeam(id, "T01");
                    }
                });
        expect("Two players on a team cannot share a jersey number", DuplicateRegistrationException.class,
                () ->
                {
                    tm.registerPlayer(new Player("PX2", "Same", "Number", LocalDate.of(1995, 1, 1), 1,
                            Player.Position.DEFENDER));
                    tm.assignPlayerToTeam("PX2", "T02");   // T02 already has #1
                });

        // 11-14. tickets and shop
        expect("Ticket sales cannot exceed stadium capacity", CapacityExceededException.class,
                () ->
                {
                    Stadium tiny = new Stadium("S99", "Tiny Ground", "Kingston", 2, Stadium.FieldCondition.GOOD, 500);
                    league.addStadium(tiny);
                    sys.scheduleFixture(new Fixtures("X7", day.plusDays(1), time, tm.findTeam("T02"),
                            tm.findTeam("T03"), tiny, tm.findOfficial("O01")));
                    sys.getTicketOffice().sellTickets("X7", "SP01", TicketCategory.GENERAL, 3, 500, 100000);
                });
        expect("A ticket sale cannot go through when payment is insufficient", InsufficientPaymentException.class,
                () -> sys.getTicketOffice().sellTickets("F03", "SP02", TicketCategory.GENERAL, 2, 1500, 10));
        expect("Quantities cannot be negative", InvalidDataException.class,
                () -> sys.getTicketOffice().sellTickets("F03", "SP02", TicketCategory.GENERAL, -1, 1500, 5000));
        expect("A shop sale cannot take more than the stock", InsufficientStockException.class,
                () -> sys.getShop().processSale(java.util.Collections.singletonMap("M01", 9999), 1e9));
        expect("A shop sale cannot complete when payment is insufficient", InsufficientPaymentException.class,
                () -> sys.getShop().processSale(java.util.Collections.singletonMap("FD02", 2), 1));

        // 15. points and standings (F01 was 2-1 to Kingston Lions)
        System.out.println();
        check("Winner gets 3 points", tm.findTeam("T01").getPoints() == 3);
        check("Loser gets 0 points", tm.findTeam("T02").getPoints() == 0);
        league.recordResult("X3", 1, 1);
        check("A draw gives each team 1 point", tm.findTeam("T02").getPoints() == 1 && tm.findTeam("T01").getPoints() == 4);

        // 16. save and reload round trip
        File dir = new File(System.getProperty("java.io.tmpdir"), "kpfl_demo_" + System.nanoTime());
        String before = league.standingsReport() + league.fixturesReport() + sys.getTicketOffice().attendanceReport()
                + sys.getShop().inventoryReport() + tm.teamReport();
        SystemStorage.saveAll(sys, dir.getPath());
        LeagueSystem reloaded = SystemStorage.loadAll(dir.getPath());
        String after = reloaded.getLeague().standingsReport() + reloaded.getLeague().fixturesReport()
                + reloaded.getTicketOffice().attendanceReport() + reloaded.getShop().inventoryReport()
                + reloaded.getTeamManager().teamReport();
        check("Save then reload gives identical standings, fixtures, tickets, stock and squads",
                before.equals(after));

        System.out.println("\n" + (failures == 0 ? "ALL RULES DEMONSTRATED." : failures + " CHECK(S) FAILED."));
        System.exit(failures == 0 ? 0 : 1);
    }
}
