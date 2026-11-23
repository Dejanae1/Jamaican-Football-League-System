import java.time.LocalDate;
import java.time.Period;

// Abstract parent of Player Coach and MatchOfficial 
public abstract class Person
{
    // Attributes
    private String personID;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;

    // Default Constructor
    public Person()
    {
        this.personID = "";
        this.firstName = "";
        this.lastName = "";
        this.dateOfBirth = null;
    }

    // Parameterized Constructor
    public Person(String personID, String firstName, String lastName, LocalDate dateOfBirth)
            throws InvalidInputException
    {
        setPersonID(personID);
        setFirstName(firstName);
        setLastName(lastName);
        setDateOfBirth(dateOfBirth);
    }

    // Mutator Methods
    public void setPersonID(String personID) throws InvalidInputException
    {
        this.personID = Validation.requireText(personID, "Person ID");
    }
    public void setFirstName(String firstName) throws InvalidInputException
    {
        this.firstName = Validation.requireText(firstName, "First name");
    }
    public void setLastName(String lastName) throws InvalidInputException
    {
        this.lastName = Validation.requireText(lastName, "Last name");
    }
    public void setDateOfBirth(LocalDate dateOfBirth) throws InvalidInputException
    {
        this.dateOfBirth = Validation.requirePastDate(dateOfBirth, "Date of birth");
    }

    // Accessor Methods
    public String getPersonID()
    {
        return personID;
    }
    public String getFirstName()
    {
        return firstName;
    }
    public String getLastName()
    {
        return lastName;
    }
    public LocalDate getDateOfBirth()
    {
        return dateOfBirth;
    }
    public String getFullName()
    {
        return firstName + " " + lastName;
    }
    public int getAge()
    {
        if (dateOfBirth == null)
        {
            return 0;
        }
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    
    public abstract String getRole();
    public abstract String toCsv();

    // Two people are the same if they share an ID
    @Override
    public boolean equals(Object other)
    {
        if (this == other)
        {
            return true;
        }
        if (!(other instanceof Person))
        {
            return false;
        }
        return personID.equals(((Person) other).personID);
    }
    @Override
    public int hashCode()
    {
        return personID.hashCode();
    }

    @Override
    public String toString()
    {
        return getRole() + " [" + personID + "] " + getFullName() + ", age " + getAge();
    }
}