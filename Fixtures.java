import java.time.LocalDate;
import java.time.LocalTime;

public class Fixtures {
     private String fixtureID;
     private LocalDate matchDate;
     private LocalTime kickOffTime;
     private String status;

     //Relationships with other classes
     private FootballTeam homeTeam;
     private FootballTeam awayTeam;
     private Stadium stadium;
     private MatchOfficial matchOfficial;
     private MatchResult matchResult;

     //Default Constructor
     public Fixtures() 
     {
         this.fixtureID = "";
         this.matchDate = null;
         this.kickOffTime = null;
         this.status = "Scheduled"; // Default status is "Scheduled"

         this.homeTeam = null;
         this.awayTeam = null;
         this.stadium = null;
         this.matchOfficial = null;
         this.matchResult = null;
     }

     // Parameterized Constructor
    public Fixtures(String fixtureID, LocalDate matchDate,
                    LocalTime kickOffTime,
                    FootballTeam homeTeam,
                    FootballTeam awayTeam,
                    Stadium stadium,
                    MatchOfficial matchOfficial) {

        this.fixtureID = fixtureID;
        this.matchDate = matchDate;
        this.kickOffTime = kickOffTime;
        this.status = "Scheduled";

        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.stadium = stadium;
        this.matchOfficial = matchOfficial;
        this.matchResult = null;
    }

     // Mutator Methods (Setters)
     public void setFixtureID(String fixtureID) 
     {
          this.fixtureID = fixtureID;
     }

     public void setMatchDate(LocalDate matchDate) 
     {
          this.matchDate = matchDate;
     }

     public void setKickOffTime(LocalTime kickOffTime) 
     {
          this.kickOffTime = kickOffTime;
     }

     public void setStatus(String status) 
     {
          this.status = status;
     }

     public void setHomeTeam(FootballTeam homeTeam) 
     {
          this.homeTeam = homeTeam;
     }

     public void setAwayTeam(FootballTeam awayTeam) 
     {
     this.awayTeam = awayTeam;
     }

     public void setStadium(Stadium stadium) 
     {
          this.stadium = stadium;
     }

     public void setMatchOfficial(MatchOfficial matchOfficial) 
     {
          this.matchOfficial = matchOfficial;
     }


     // Accessor Methods (Getters)
     public String getFixtureID() 
     {
          return fixtureID;
     }

     public LocalDate getMatchDate() 
     {
          return matchDate;
     }

     public LocalTime getKickOffTime() 
     {
          return kickOffTime;
     }

     public String getStatus() 
     {
          return status;
     }

     public FootballTeam getHomeTeam() 
     {
          return homeTeam;
     }

     public FootballTeam getAwayTeam() 
     {
          return awayTeam;
     }

     public Stadium getStadium() 
     {
          return stadium;
     }

     public MatchOfficial getMatchOfficial() 
     {
          return matchOfficial;
     }

     public MatchResult getMatchResult() 
     {
          return matchResult;
     }
     
     // Public Attributes
     public boolean validateFixture()
     {
          if (fixtureID == null || fixtureID.isEmpty()) 
          {
               return false;
          }

          if (matchDate == null || kickOffTime == null) 
          {
               return false;
          }

          if (homeTeam == null || awayTeam == null) 
          {
               return false;
          }

          if (homeTeam == awayTeam) 
          {
               return false;
          }

          if (stadium == null || matchOfficial == null) 
          {
               return false;
          }

          return true;
     }

    public void updateFixture(LocalDate newDate, LocalTime newTime)
{
    if (newDate != null && newTime != null) {

        this.matchDate = newDate;
        this.kickOffTime = newTime;

        System.out.println("Fixture updated successfully.");

    } else {

        System.out.println(
            "Invalid date or time. Fixture not updated."
        );
    }
}

     public void displayFixture()
     {
          System.out.println("========== Fixture Details ==========\n");
          System.out.println("Fixture ID: " + fixtureID);
          System.out.println("Match Date: " + matchDate);
          System.out.println("Kick-Off Time: " + kickOffTime);
          System.out.println("Status: " + status);

          System.out.println("Home Team Assigned: " + (homeTeam != null));
          System.out.println("Away Team Assigned: " + (awayTeam != null));
          System.out.println("Stadium Assigned: " + (stadium != null));
          System.out.println("Match Official Assigned: " + (matchOfficial != null));
          
          if (matchResult != null) 
          {
               System.out.println("Match Result: Recorded");
          } else {
               System.out.println("Match Result: Not Yet Recorded");
          }
     }

     public void changeStatus(String newStatus) 
     {
          if (newStatus == null || newStatus.isEmpty()) 
          {
               System.out.println("Invalid fixture status.");
               return;
          }

          if (newStatus.equalsIgnoreCase("Completed") && matchResult == null) 
          {
               System.out.println("Cannot complete fixture without a valid result.");
               return;
          }

          this.status = newStatus;
     }

     // Record Match Result

     public void recordResult(int homeScore, int awayScore) {

          MatchResult result = new MatchResult(homeScore, awayScore);

          if (result.validateScore()) 
          {

               this.matchResult = result;
               this.status = "Completed";

               System.out.println("Match result recorded successfully.");

          } else {
               System.out.println("Invalid match scores.");
          }
     }

}
