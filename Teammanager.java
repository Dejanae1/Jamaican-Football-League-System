import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;


public class TeamManager
{
    private ArrayList<FootballTeam> teams = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Coach> coaches = new ArrayList<>();
    private ArrayList<MatchOfficial> officials = new ArrayList<>();

   
    public void registerTeam(FootballTeam team) throws DuplicateRegistrationException
    {
        for (FootballTeam t : teams)
        {
            if (t.getTeamID().equals(team.getTeamID()))
            {
                throw new DuplicateRegistrationException("Team ID " + team.getTeamID() + " already exists.");
            }
            if (t.getTeamName().equalsIgnoreCase(team.getTeamName()))
            {
                throw new DuplicateRegistrationException("Team name " + team.getTeamName() + " already exists.");
            }
        }
        teams.add(team);
    }

    public void registerPlayer(Player player) throws DuplicateRegistrationException
    {
        checkPersonIDUnused(player.getPersonID());
        players.add(player);
    }

    public void registerCoach(Coach coach) throws DuplicateRegistrationException
    {
        checkPersonIDUnused(coach.getPersonID());
        coaches.add(coach);
    }

    public void registerOfficial(MatchOfficial official) throws DuplicateRegistrationException
    {
        checkPersonIDUnused(official.getPersonID());
        officials.add(official);
    }

    /
    private void checkPersonIDUnused(String id) throws DuplicateRegistrationException
    {
        if (findPerson(id) != null)
        {
            throw new DuplicateRegistrationException("Person ID " + id + " is already registered.");
        }
    }

    
    public void assignPlayerToTeam(String playerID, String teamID)
            throws InvalidInputException, DuplicateRegistrationException
    {
        Player player = findPlayer(playerID);
        FootballTeam team = findTeam(teamID);
        if (player == null)
        {
            throw new InvalidInputException("No player with ID " + playerID + ".");
        }
        if (team == null)
        {
            throw new InvalidInputException("No team with ID " + teamID + ".");
        }
        team.addPlayer(player);
    }

    public void assignCoachToTeam(String coachID, String teamID)
            throws InvalidInputException, DuplicateRegistrationException
    {
        Coach coach = findCoach(coachID);
        FootballTeam team = findTeam(teamID);
        if (coach == null)
        {
            throw new InvalidInputException("No coach with ID " + coachID + ".");
        }
        if (team == null)
        {
            throw new InvalidInputException("No team with ID " + teamID + ".");
        }
        team.assignCoach(coach);
    }

    
    public FootballTeam findTeam(String teamID)
    {
        for (FootballTeam t : teams)
        {
            if (t.getTeamID().equals(teamID))
            {
                return t;
            }
        }
        return null;
    }
    public Player findPlayer(String id)
    {
        for (Player p : players)
        {
            if (p.getPersonID().equals(id))
            {
                return p;
            }
        }
        return null;
    }
    public Coach findCoach(String id)
    {
        for (Coach c : coaches)
        {
            if (c.getPersonID().equals(id))
            {
                return c;
            }
        }
        return null;
    }
    public MatchOfficial findOfficial(String id)
    {
        for (MatchOfficial o : officials)
        {
            if (o.getPersonID().equals(id))
            {
                return o;
            }
        }
        return null;
    }
    public Person findPerson(String id)
    {
        Person found = findPlayer(id);
        if (found == null)
        {
            found = findCoach(id);
        }
        if (found == null)
        {
            found = findOfficial(id);
        }
        return found;
    }

    
    public ArrayList<Player> searchPlayersByName(String text)
    {
        ArrayList<Player> matches = new ArrayList<>();
        for (Player p : players)
        {
            if (p.getFullName().toLowerCase().contains(text.toLowerCase()))
            {
                matches.add(p);
            }
        }
        return matches;
    }

    
    public ArrayList<FootballTeam> getTeams()
    {
        return new ArrayList<>(teams);
    }
    public ArrayList<Player> getPlayers()
    {
        return new ArrayList<>(players);
    }
    public ArrayList<Coach> getCoaches()
    {
        return new ArrayList<>(coaches);
    }
    public ArrayList<MatchOfficial> getOfficials()
    {
        return new ArrayList<>(officials);
    }

    
    public void printTeamReport()
    {
        System.out.println("=== TEAM REPORT ===");
        for (FootballTeam t : teams)
        {
            System.out.println(t);
            for (Player p : t.getPlayers())
            {
                System.out.println("    " + p);
            }
        }
    }

    
    public void saveToFile(String fileName) throws IOException
    {
        try (PrintWriter out = new PrintWriter(new FileWriter(fileName)))
        {
            for (Coach c : coaches)
            {
                out.println(c.toCsv());
            }
            for (MatchOfficial o : officials)
            {
                out.println(o.toCsv());
            }
            for (Player p : players)
            {
                out.println(p.toCsv());
            }
            for (FootballTeam t : teams)
            {
                out.println(t.toCsv());
            }
        }
    }

    public void loadFromFile(String fileName)
            throws IOException, InvalidInputException, DuplicateRegistrationException
    {
        teams.clear();
        players.clear();
        coaches.clear();
        officials.clear();

        ArrayList<String[]> teamLines = new ArrayList<>();
        ArrayList<String[]> playerLines = new ArrayList<>();

        try (BufferedReader in = new BufferedReader(new FileReader(fileName)))
        {
            String line;
            while ((line = in.readLine()) != null)
            {
                if (line.trim().isEmpty())
                {
                    continue;
                }
                String[] f = line.split(",");
                try
                {
                    switch (f[0])
                    {
                        case "COACH":
                            registerCoach(new Coach(f[1], f[2], f[3], LocalDate.parse(f[4]),
                                    f[5], Integer.parseInt(f[6])));
                            break;
                        case "OFFICIAL":
                            MatchOfficial o = new MatchOfficial(f[1], f[2], f[3], LocalDate.parse(f[4]),
                                    MatchOfficial.OfficialType.valueOf(f[5]));
                            o.setMatchesOfficiated(Integer.parseInt(f[6]));
                            registerOfficial(o);
                            break;
                        case "PLAYER":
                            Player p = new Player(f[1], f[2], f[3], LocalDate.parse(f[4]),
                                    Integer.parseInt(f[5]), Player.Position.valueOf(f[6]));
                            p.setGoalsScored(Integer.parseInt(f[7]));
                            registerPlayer(p);
                            playerLines.add(f);
                            break;
                        case "TEAM":
                            registerTeam(new FootballTeam(f[1], f[2], f[3]));
                            teamLines.add(f);
                            break;
                        default:
                            throw new InvalidInputException("Unknown record type: " + f[0]);
                    }
                }
                catch (RuntimeException e)   // bad number, bad date, missing column...
                {
                    throw new InvalidInputException("Bad line in " + fileName + ": " + line);
                }
            }
        }

        
        for (String[] f : teamLines)
        {
            if (!f[4].equals("NONE"))
            {
                assignCoachToTeam(f[4], f[1]);
            }
        }
        for (String[] f : playerLines)
        {
            if (!f[8].equals("NONE"))
            {
                assignPlayerToTeam(f[1], f[8]);
            }
        }
    }


    public static void main(String[] args)
    {
        TeamManager manager = new TeamManager();
        try
        {
            manager.registerTeam(new FootballTeam("T01", "Kingston Lions", "Kingston"));
            manager.registerTeam(new FootballTeam("T02", "Half Way Tree FC", "St. Andrew"));
            manager.registerCoach(new Coach("C01", "Marcus", "Brown", LocalDate.of(1980, 5, 14), "Level B", 12));
            manager.registerPlayer(new Player("P01", "Andre", "Clarke", LocalDate.of(2001, 3, 2), 9, Player.Position.FORWARD));
            manager.registerPlayer(new Player("P02", "Devon", "Reid", LocalDate.of(1999, 8, 21), 1, Player.Position.GOALKEEPER));
            manager.registerPlayer(new Player("P03", "Kemar", "Lewis", LocalDate.of(2000, 1, 30), 9, Player.Position.MIDFIELDER));
            manager.registerOfficial(new MatchOfficial("O01", "Paul", "Grant", LocalDate.of(1985, 11, 3), MatchOfficial.OfficialType.REFEREE));

            manager.assignCoachToTeam("C01", "T01");
            manager.assignPlayerToTeam("P01", "T01");
            manager.assignPlayerToTeam("P02", "T01");
        }
        catch (Exception e)
        {
            System.out.println("Setup error: " + e.getMessage());
        }

      
        try { manager.assignPlayerToTeam("P03", "T01"); }                       // jersey 9 already used
        catch (Exception e) { System.out.println("Rejected: " + e.getMessage()); }

        try { manager.registerPlayer(new Player("P01", "Copy", "Cat", LocalDate.of(2000, 1, 1), 5, Player.Position.DEFENDER)); } 
        catch (Exception e) { System.out.println("Rejected: " + e.getMessage()); }

        try { manager.assignPlayerToTeam("P01", "T02"); }                       // already on a team
        catch (Exception e) { System.out.println("Rejected: " + e.getMessage()); }

        try { new Player("P09", "Bad", "Number", LocalDate.of(2000, 1, 1), 150, Player.Position.FORWARD); } 
        catch (Exception e) { System.out.println("Rejected: " + e.getMessage()); }

        try { new Coach("C09", "", "Blank", LocalDate.of(1980, 1, 1), "Level A", 5); }  // blank name
        catch (Exception e) { System.out.println("Rejected: " + e.getMessage()); }

        System.out.println();
        manager.printTeamReport();

        
        try
        {
            manager.saveToFile("kpfl_teams.csv");
            TeamManager reloaded = new TeamManager();
            reloaded.loadFromFile("kpfl_teams.csv");
            System.out.println();
            System.out.println("After reload:");
            reloaded.printTeamReport();
        }
        catch (Exception e)
        {
            System.out.println("File error: " + e.getMessage());
        }
    }
}