public class MatchResult {
    // Attributes
    private int homeScore;
    private int awayScore;


    // Default Constructor
    public MatchResult() 
    {
        this.homeScore = 0;
        this.awayScore = 0;
    }


    // Parameterized Constructor
    public MatchResult(int homeScore, int awayScore) 
    {
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }


    // Mutator Methods (Setters)

    public void setHomeScore(int homeScore) 
    {
        this.homeScore = homeScore;
    }

    public void setAwayScore(int awayScore) 
    {
        this.awayScore = awayScore;
    }


    // Accessor Methods (Getters)

    public int getHomeScore() 
    {
        return homeScore;
    }

    public int getAwayScore() 
    {
        return awayScore;
    }


    // Validate Score

    public boolean validateScore() 
    {

        if (homeScore < 0 || awayScore < 0) {
            return false;
        }

        return true;
    }


    // Get Match Winner

    public String getWinner() 
    {

        if (!validateScore()) {
            return "Invalid Score";
        }

        if (homeScore > awayScore) {
            return "Home Team Wins";

        } else if (awayScore > homeScore) {
            return "Away Team Wins";

        } else {
            return "Match Drawn";
        }
    }
}
