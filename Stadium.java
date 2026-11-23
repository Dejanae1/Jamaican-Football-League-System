import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Stadium{

    // Attributes
    private String stadiumID;
    private String stadiumName;
    private String location;
    private int seatingCapacity;
    private FieldCondition fieldCondition;
    private double ticketPrice;

    // Relationship with Fixtures
    private ArrayList<Fixtures> fixturesList = new ArrayList<>();

    // Field Conditions Enum
    public enum FieldCondition 
    {
        GOOD, FAIR, POOR
    }

    // Default Constructor
    public Stadium()
    {
        this.stadiumID = "";
        this.stadiumName = "";
        this.location = "";
        this.seatingCapacity = 0;
        this.fieldCondition = Stadium.FieldCondition.GOOD;
        this.ticketPrice = 0.0;
    }

    // Parameterized Constructor
    public Stadium (String stadiumID, String stadiumName,
                   String location, int seatingCapacity,
                   FieldCondition fieldCondition, double ticketPrice)
    {
        this.stadiumID = stadiumID;
        this.stadiumName = stadiumName;
        this.location = location;
        this.seatingCapacity = seatingCapacity;
        this.fieldCondition = fieldCondition;
        this.ticketPrice = ticketPrice;
    }
    
    // Mutator Methods
    public void setStadiumID(String stadiumID)
    {
        this.stadiumID = stadiumID;
    }
    public void setStadiumName(String stadiumName)
    {
        this.stadiumName = stadiumName;
    }
    public void setlocation(String location)
    {
        this.location = location;
    }
    public void setseatingCapacity(int seatingCapacity)
    {
        this.seatingCapacity = seatingCapacity;
    }
    public void setfieldCondition(FieldCondition fieldCondition)
    {
        this.fieldCondition = fieldCondition;
    }
    public void setticketPrice(double ticketPrice)
    {
        this.ticketPrice = ticketPrice;
    }
    
    // Accessor Methods
    public String getstadiumID1()
    {
        return stadiumID;
    }
    public String getstadiumName()
    {
        return stadiumName;
    }
    public String getlocation()
    {
        return location;
    }
    public int getseatingCapacity()
    {
        return seatingCapacity;
    }
    public  FieldCondition getfieldCondition()
    {
        return fieldCondition;
    }
    public double getticketPrice()
    {
        return ticketPrice;
    }
    public ArrayList<Fixtures> getFixturesList() {
            return fixturesList;
    }

    // Validate Stadium
    public boolean validStadium()
    {
        if (stadiumID == null || stadiumID.isEmpty()) 
        {
            return false;
        }

        if (stadiumName == null || stadiumName.isEmpty()) 
        {
            return false;
        }

        if (location == null || location.isEmpty()) 
        {
            return false;
        }

        if (seatingCapacity <= 0 || ticketPrice < 0) 
        {
            return false;
        }

        if (fieldCondition == null) 
        {
            return false;
        }

        return true;
    }

    // Update Stadium

    public void updateStadium(String stadiumName, String location,
                              int seatingCapacity,
                              FieldCondition fieldCondition,
                              double ticketPrice) {

        if (stadiumName != null && !stadiumName.isEmpty()
                && location != null && !location.isEmpty()
                && seatingCapacity > 0
                && fieldCondition != null
                && ticketPrice >= 0) {

            this.stadiumName = stadiumName;
            this.location = location;
            this.seatingCapacity = seatingCapacity;
            this.fieldCondition = fieldCondition;
            this.ticketPrice = ticketPrice;

        } else {
            System.out.println("Invalid stadium information.");
        }
    }


    // Display Stadium

    public void displayStadium() {

        System.out.println("========== STADIUM DETAILS ==========");

        System.out.println("Stadium ID: " + stadiumID);
        System.out.println("Stadium Name: " + stadiumName);
        System.out.println("Location: " + location);
        System.out.println("Seating Capacity: " + seatingCapacity);
        System.out.println("Field Condition: " + fieldCondition);
        System.out.println("Ticket Price: " + ticketPrice);
    }


    // Check Stadium Availability

    public boolean checkAvailability(LocalDate date, LocalTime time) {

        if (date == null || time == null) {
            return false;
        }

        for (Fixtures fixture : fixturesList) {

            if (fixture.getMatchDate().equals(date)
                    && fixture.getKickOffTime().equals(time)) {

                return false;
            }
        }

        return true;
    }


    // Add Fixture to Stadium

    public void addFixture(Fixtures fixture) {

        if (fixture != null && !fixturesList.contains(fixture)) {
            fixturesList.add(fixture);
        }
    }
}
