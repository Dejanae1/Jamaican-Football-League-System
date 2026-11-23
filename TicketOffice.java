
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Sells tickets and enforces stadium seating capacity. */
public class TicketOffice {

    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final Map<String, Fixtures> matches = new HashMap<>();
    private final List<Ticket> tickets = new ArrayList<>();

    /** Registers an existing league fixture. */
    public void registerFixture(Fixtures fixture)
            throws InvalidDataException {

        if (fixture == null || !fixture.validateFixture()) {
            throw new InvalidDataException("Invalid fixture");
        }

        String id = fixture.getFixtureID();

        if (matches.containsKey(id)) {
            throw new InvalidDataException(
                    "Fixture already registered: " + id);
        }

        matches.put(id, fixture);
    }

    /** Counts tickets sold for a particular fixture. */
    public int ticketsSold(String fixtureId) {
        int count = 0;

        for (Ticket t : tickets) {
            if (t.getFixtureId().equals(fixtureId)) {
                count++;
            }
        }

        return count;
    }

    /** Returns the remaining seating capacity. */
    public int seatsRemaining(String fixtureId)
            throws InvalidDataException {

        Fixtures fixture = matches.get(fixtureId);

        if (fixture == null) {
            throw new InvalidDataException(
                    "Unknown fixture: " + fixtureId);
        }

        return fixture.getStadium().getCapacity()
                - ticketsSold(fixtureId);
    }

    /** Creates a readable match name. */
    private String getMatchName(Fixtures fixture) {
        return fixture.getHomeTeam().getTeamName()
                + " vs "
                + fixture.getAwayTeam().getTeamName();
    }

    /**
     * Sells tickets.
     * No tickets are issued unless capacity and payment checks pass.
     */
    public Receipt sellTickets(
            String fixtureId,
            String spectatorId,
            TicketCategory category,
            int qty,
            double basePrice,
            double payment) throws LeagueException {

        if (fixtureId == null || fixtureId.trim().isEmpty()) {
            throw new InvalidDataException("Fixture ID is required");
        }

        if (spectatorId == null || spectatorId.trim().isEmpty()) {
            throw new InvalidDataException("Spectator ID is required");
        }

        if (category == null) {
            throw new InvalidDataException("Ticket category is required");
        }

        if (qty <= 0) {
            throw new InvalidDataException(
                    "Ticket quantity must be greater than zero");
        }

        if (!Double.isFinite(basePrice) || basePrice < 0) {
            throw new InvalidDataException("Invalid ticket price");
        }

        int remaining = seatsRemaining(fixtureId);

        if (qty > remaining) {
            throw new CapacityExceededException(
                    "Only " + remaining + " seat(s) left for fixture "
                    + fixtureId + ", requested " + qty);
        }

        Fixtures fixture = matches.get(fixtureId);

        double unitPrice = category.priceFor(basePrice);

        List<Receipt.Line> lines = new ArrayList<>();

        lines.add(new Receipt.Line(
                category + " ticket (" + fixtureId + ")",
                qty,
                unitPrice));

        // Receipt validates payment before tickets are issued.
        Receipt receipt = new Receipt(
                nextReceiptNo(),
                lines,
                payment);

        String purchasedAt =
                LocalDateTime.now().format(TIME_FORMAT);

        // Generate and issue individual tickets.
        for (int i = 0; i < qty; i++) {

            String ticketId = nextTicketId();

            Ticket ticket = new Ticket(
                    ticketId,
                    fixtureId,
                    getMatchName(fixture),
                    fixture.getMatchDate().toString() + " "
                            + fixture.getKickOffTime().toString(),
                    fixture.getStadium().getStadiumName(),
                    spectatorId.trim(),
                    category,
                    unitPrice,
                    receipt.getReceiptNo(),
                    purchasedAt);

            tickets.add(ticket);
        }

        return receipt;
    }

    /** Generates a ticket ID that does not already exist. */
    private String nextTicketId() {

        int number = 1;

        while (true) {
            String id = String.format("TK%05d", number);

            boolean exists = false;

            for (Ticket t : tickets) {
                if (t.getTicketId().equals(id)) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                return id;
            }

            number++;
        }
    }

    /** Generates a receipt number that does not already exist. */
    private String nextReceiptNo() {

        int number = 1;

        while (true) {
            String receiptNo = String.format("T%04d", number);

            boolean exists = false;

            for (Ticket t : tickets) {
                if (t.getReceiptNo().equals(receiptNo)) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                return receiptNo;
            }

            number++;
        }
    }

    /** Returns a copy of all tickets. */
    public List<Ticket> getAllTickets() {
        return new ArrayList<>(tickets);
    }

    /** Returns tickets belonging to a particular receipt. */
    public List<Ticket> getTicketsForReceipt(String receiptNo) {

        List<Ticket> result = new ArrayList<>();

        for (Ticket t : tickets) {
            if (t.getReceiptNo().equals(receiptNo)) {
                result.add(t);
            }
        }

        return result;
    }

    /** Restores tickets from saved data, avoiding duplicate IDs. */
    public void restoreTicket(Ticket t)
            throws InvalidDataException {

        if (t == null) {
            throw new InvalidDataException("Ticket cannot be null");
        }

        Fixtures fixture = matches.get(t.getFixtureId());

        if (fixture == null) {
            throw new InvalidDataException(
                    "Unknown fixture: " + t.getFixtureId());
        }

        for (Ticket existing : tickets) {
            if (existing.getTicketId().equals(t.getTicketId())) {
                return;
            }
        }

        if (ticketsSold(t.getFixtureId())
                >= fixture.getStadium().getCapacity()) {

            throw new InvalidDataException(
                    "Restoring ticket would exceed stadium capacity");
        }

        tickets.add(t);
    }

    /** Report: attendance and revenue per match. */
    public String attendanceReport() {

        StringBuilder sb =
                new StringBuilder("--- TICKET SALES REPORT ---\n");

        for (Fixtures fixture : matches.values()) {

            double revenue = 0;

            for (Ticket t : tickets) {
                if (t.getFixtureId().equals(
                        fixture.getFixtureID())) {

                    revenue += t.getPrice();
                }
            }

            int sold = ticketsSold(fixture.getFixtureID());
            int capacity = fixture.getStadium().getCapacity();

            double percentage =
                    100.0 * sold / capacity;

            sb.append(String.format(
                    "%-8s %-28s sold %d/%d (%.0f%% full) revenue $%.2f%n",
                    fixture.getFixtureID(),
                    getMatchName(fixture),
                    sold,
                    capacity,
                    percentage,
                    Money.round(revenue)));
        }

        return sb.toString();
    }

    /** Report: every ticket sold. */
    public String ticketListReport() {

        StringBuilder sb =
                new StringBuilder("--- TICKETS SOLD ---\n");

        if (tickets.isEmpty()) {
            sb.append("No tickets sold yet.\n");
        }

        for (Ticket t : tickets) {
            sb.append(t).append("\n");
        }

        return sb.toString();
    }
}


/** Represents one individual match ticket. */
class Ticket {

    private final String ticketId;
    private final String fixtureId;
    private final String matchName;
    private final String matchDateTime;
    private final String stadiumName;
    private final String spectatorId;
    private final TicketCategory category;
    private final double price;
    private final String receiptNo;
    private final String purchasedAt;

    public Ticket(
            String ticketId,
            String fixtureId,
            String matchName,
            String matchDateTime,
            String stadiumName,
            String spectatorId,
            TicketCategory category,
            double price,
            String receiptNo,
            String purchasedAt) throws InvalidDataException {

        FileManager.checkField(ticketId, "Ticket ID");
        FileManager.checkField(fixtureId, "Fixture ID");
        FileManager.checkField(matchName, "Match name");
        FileManager.checkField(matchDateTime, "Match date and time");
        FileManager.checkField(stadiumName, "Stadium name");
        FileManager.checkField(spectatorId, "Spectator ID");
        FileManager.checkField(receiptNo, "Receipt number");
        FileManager.checkField(purchasedAt, "Purchase date");

        if (category == null) {
            throw new InvalidDataException(
                    "Ticket category is required");
        }

        if (!Double.isFinite(price) || price < 0) {
            throw new InvalidDataException("Invalid ticket price");
        }

        this.ticketId = ticketId.trim();
        this.fixtureId = fixtureId.trim();
        this.matchName = matchName.trim();
        this.matchDateTime = matchDateTime.trim();
        this.stadiumName = stadiumName.trim();
        this.spectatorId = spectatorId.trim();
        this.category = category;
        this.price = Money.round(price);
        this.receiptNo = receiptNo.trim();
        this.purchasedAt = purchasedAt.trim();
    }

    public String getTicketId() { return ticketId; }
    public String getFixtureId() { return fixtureId; }
    public String getMatchName() { return matchName; }
    public String getMatchDateTime() { return matchDateTime; }
    public String getStadiumName() { return stadiumName; }
    public String getSpectatorId() { return spectatorId; }
    public TicketCategory getCategory() { return category; }
    public double getPrice() { return price; }
    public String getReceiptNo() { return receiptNo; }
    public String getPurchasedAt() { return purchasedAt; }

    /** Converts ticket information into a file record. */
    public String toFileLine() {

        String sep = FileManager.SEPARATOR;

        return ticketId + sep
                + fixtureId + sep
                + matchName + sep
                + matchDateTime + sep
                + stadiumName + sep
                + spectatorId + sep
                + category + sep
                + price + sep
                + receiptNo + sep
                + purchasedAt;
    }

    @Override
    public String toString() {

        return String.format(
                "%s | %s %s | %s | %s | spectator %s | %s | $%.2f | receipt %s | %s",
                ticketId,
                fixtureId,
                matchName,
                matchDateTime,
                stadiumName,
                spectatorId,
                category,
                price,
                receiptNo,
                purchasedAt);
    }
}


/** Ticket categories and their price multipliers. */
enum TicketCategory {

    GENERAL(1.0),
    VIP(2.5),
    STUDENT(0.5);

    private final double multiplier;

    TicketCategory(double multiplier) {
        this.multiplier = multiplier;
    }

    public double priceFor(double basePrice) {
        return Money.round(basePrice * multiplier);
    }
}
