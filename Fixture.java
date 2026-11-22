import java.time.LocalDate;
import java.time.LocalTime;

public class Fixture {
     private String fixtureID;
     private LocalDate matchDate;
     private LocalTime kickOffTime;
     private String status;

     // Relationships with other classes
     private Stadium stadium;
     private MatchResult matchResult;

     //Default Construtor
     public Fixture()
     {
          this.fixtureID = "";
          this.matchDate = null;
          this.kickOffTime = null;
          this.status = "Scheduled";
          this.stadium = null;
          this.matchResult = null;
     }

     // Parameterized Constructor
     public Fixture (String fixtureID, LocalDate matchDate, LocalTime kickOffTime, String status, Stadium stadium, MatchResult matchResult)
     {
          this.fixtureID = fixtureID;
          this.matchDate = matchDate;
          this.kickOffTime = kickOffTime;
          this.status = "Scheduled";
          this.stadium = stadium;
          this.matchResult = null;
     }

     // Mutator Methods
     public void setfixtureID(String fixtureID)
     {
          this.fixtureID = fixtureID;
     }
     public void setmatchDate(LocalDate matchDate)
     {
          this.matchDate = matchDate;
     }
     public void setkickOffTime(LocalTime kickOffTime)
     {
          this.kickOffTime = kickOffTime;
     }
     public void setstatus(String status)
     {
          this.status = status;
     }
     

     //Accessor Mutator
     public String getfixtureID()
     {
          return fixtureID;
     }
     public LocalDate getmatchDate()
     {
          return matchDate;
     }
     public LocalTime getkickOffTime()
     {
          return kickOffTime;
     }
     public String getstatus()
     {
          return status;
     }
     public Stadium getStadium()
     {
          return stadium;
     }
     public MatchResult getMatchResult()
     {
          return matchResult;
     }

     public boolean validateFixture()
     {
          if(fixtureID == null || fixtureID.isEmpty())
          {
               return false;
          }

          if(matchDate == null || kickOffTime == null)
          {
               return false;
          }

          return true;
     }

     public void updateFixture(LocalDate matchDate, LocalTime kickOffTime)
     {
          this.matchDate = matchDate;
          this.kickOffTime = kickOffTime;
     }

     public void displayFixture()
     {
          System.out.println("========== FIXTURE DETAILS ==========");
          System.out.println("Fixture ID: " + fixtureID);
          System.out.println("Match Date: " + matchDate);
          System.out.println("Kick Off Time: " + kickOffTime);
          System.out.println("Status: " + status);
          
          if (stadium != null) 
          {
               System.out.println("Stadium: Assigned");
          } else {
               System.out.println("Stadium: Not Assigned");
          }

          if (matchResult != null) 
          {
               System.out.println("Match Result: Recorded");
          } else {
               System.out.println("Match Result: Not Recorded");
          }
     }

     public void changeStatus( String newStatus)
     {
          if(newStatus != null && !newStatus.isEmpty())
          {
               System.out.println("Invalid fixture status");
               return;
          }
          if(newStatus.equalsIgnoreCase("Completed") && matchResult == null)
          {
               System.out.println("Cannot complete fixture without recording a result.");
               return;
          }

          this.status = newStatus;
     }

     public void recordResult(int homeScore, int awayScore)
     {
          MatchResult result = new MatchResult(homeScore, awayScore);

        if (result.validateScore()) {

            this.matchResult = result;
            this.status = "Completed";

            System.out.println("Match result recorded successfully.");

        } else {
            System.out.println("Invalid match scores.");
        }
     }
}
