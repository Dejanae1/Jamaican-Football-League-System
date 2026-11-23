import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Sells tickets and enforces the "cannot exceed seating capacity" rule. */
public class TicketOffice {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final Map<String, MatchInfo> matches = new HashMap<>();   // fixtureId -> match details
    private final List<Ticket> tickets = new ArrayList<>();           // every ticket sold

    /** Called when a fixture is created, with the match details from the league. */
    public void registerFixture(MatchInfo match) {
        matches.put(match.getFixtureId(), match);
    }

    public int ticketsSold(String fixtureId) {
        int count = 0;
        for (Ticket t : tickets) {
            if (t.getFixtureId().equals(fixtureId)) {
                count++;
            }
        }
        return count;
    }

    public int seatsRemaining(String fixtureId) throws InvalidDataException {
        MatchInfo match = matches.get(fixtureId);
        if (match == null) {
            throw new InvalidDataException("Unknown fixture: " + fixtureId);
        }
        return match.getCapacity() - ticketsSold(fixtureId);
    }

    /**
     * Sells qty tickets. Nothing is issued unless capacity and payment both pass.
     * basePrice is the stadium's ticket price (GCT included).
     */
    public Receipt sellTickets(String fixtureId, String spectatorId, TicketCategory category,
                               int qty, double basePrice, double payment) throws LeagueException {
        if (qty <= 0) {
            throw new InvalidDataException("Ticket quantity must be greater than zero");
        }
        if (basePrice < 0) {
            throw new InvalidDataException("Ticket price cannot be negative");
        }
        FileManager.checkField(spectatorId, "Spectator id");
        int remaining = seatsRemaining(fixtureId);
        if (qty > remaining) {
            throw new CapacityExceededException("Only " + remaining + " seat(s) left for fixture "
                + fixtureId + ", requested " + qty);
        }

        MatchInfo match = matches.get(fixtureId);
        double unitPrice = category.priceFor(basePrice);
        List<Receipt.Line> lines = new ArrayList<>();
        lines.add(new Receipt.Line(category + " ticket (" + fixtureId + ")", qty, unitPrice));
        Receipt receipt = new Receipt(nextReceiptNo(), lines, payment);

        String purchasedAt = LocalDateTime.now().format(TIME_FORMAT);
        for (int i = 0; i < qty; i++) {
            String ticketId = String.format("TK%05d", tickets.size() + 1);
            tickets.add(new Ticket(ticketId, fixtureId, match.getMatchName(), match.getMatchDateTime(),
                match.getStadiumName(), spectatorId.trim(), category, unitPrice,
                receipt.getReceiptNo(), purchasedAt));
        }
        return receipt;
    }

    /** Counts the receipts already on file, so numbers stay unique after a reload. */
    private String nextReceiptNo() {
        List<String> seen = new ArrayList<>();
        for (Ticket t : tickets) {
            if (!seen.contains(t.getReceiptNo())) {
                seen.add(t.getReceiptNo());
            }
        }
        return String.format("T%04d", seen.size() + 1);
    }

    public List<Ticket> getAllTickets() {
        return new ArrayList<>(tickets);
    }

    public List<Ticket> getTicketsForReceipt(String receiptNo) {
        List<Ticket> result = new ArrayList<>();
        for (Ticket t : tickets) {
            if (t.getReceiptNo().equals(receiptNo)) {
                result.add(t);
            }
        }
        return result;
    }

    /** Used when loading from file. A ticket that is already in the list is skipped. */
    public void restoreTicket(Ticket t) {
        for (Ticket existing : tickets) {
            if (existing.getTicketId().equals(t.getTicketId())) {
                return;
            }
        }
        tickets.add(t);
    }

    /** Report: attendance and revenue per match. */
    public String attendanceReport() {
        StringBuilder sb = new StringBuilder("--- TICKET SALES REPORT ---\n");
        for (MatchInfo m : matches.values()) {
            double revenue = 0;
            for (Ticket t : tickets) {
                if (t.getFixtureId().equals(m.getFixtureId())) {
                    revenue = revenue + t.getPrice();
                }
            }
            int sold = ticketsSold(m.getFixtureId());
            sb.append(String.format("%-4s %-28s sold %d/%d (%.0f%% full)  revenue $%.2f%n",
                m.getFixtureId(), m.getMatchName(), sold, m.getCapacity(),
                100.0 * sold / m.getCapacity(), Money.round(revenue)));
        }
        return sb.toString();
    }

    /** Report: every ticket sold, with its code and match details. */
    public String ticketListReport() {
        StringBuilder sb = new StringBuilder("--- TICKETS SOLD ---\n");
        if (tickets.isEmpty()) {
            sb.append("No tickets sold yet.\n");
        }
        for (Ticket t : tickets) {
            sb.append(t).append("\n");
        }
        return sb.toString();
    }
}

/**
 * The match details a ticket needs. Member C's Fixture/Stadium supply these values
 * when the fixture is created (teams, date and time, stadium name, seating capacity).
 */
class MatchInfo {
    private final String fixtureId;
    private final String matchName;       // e.g. "Tivoli vs Harbour View"
    private final String matchDateTime;   // e.g. "2026-12-05 15:00"
    private final String stadiumName;
    private final int capacity;

    public MatchInfo(String fixtureId, String matchName, String matchDateTime,
                     String stadiumName, int capacity) throws InvalidDataException {
        FileManager.checkField(fixtureId, "Fixture id");
        FileManager.checkField(matchName, "Match name");
        FileManager.checkField(matchDateTime, "Match date and time");
        FileManager.checkField(stadiumName, "Stadium name");
        if (capacity <= 0) {
            throw new InvalidDataException("Seating capacity must be greater than zero");
        }
        this.fixtureId = fixtureId.trim();
        this.matchName = matchName.trim();
        this.matchDateTime = matchDateTime.trim();
        this.stadiumName = stadiumName.trim();
        this.capacity = capacity;
    }

    public String getFixtureId() { return fixtureId; }
    public String getMatchName() { return matchName; }
    public String getMatchDateTime() { return matchDateTime; }
    public String getStadiumName() { return stadiumName; }
    public int getCapacity() { return capacity; }

    @Override
    public String toString() {
        return fixtureId + ": " + matchName + ", " + matchDateTime + ", " + stadiumName;
    }
}

/** A single match ticket. The ticketId is the ticket's code. */
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

    public Ticket(String ticketId, String fixtureId, String matchName, String matchDateTime,
                  String stadiumName, String spectatorId, TicketCategory category,
                  double price, String receiptNo, String purchasedAt) throws InvalidDataException {
        if (price < 0) {
            throw new InvalidDataException("Ticket price cannot be negative");
        }
        this.ticketId = ticketId;
        this.fixtureId = fixtureId;
        this.matchName = matchName;
        this.matchDateTime = matchDateTime;
        this.stadiumName = stadiumName;
        this.spectatorId = spectatorId;
        this.category = category;
        this.price = price;
        this.receiptNo = receiptNo;
        this.purchasedAt = purchasedAt;
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

    /** One line for the tickets file (same field order as the constructor). */
    public String toFileLine() {
        String sep = FileManager.SEPARATOR;
        return ticketId + sep + fixtureId + sep + matchName + sep + matchDateTime + sep + stadiumName
            + sep + spectatorId + sep + category + sep + price + sep + receiptNo + sep + purchasedAt;
    }

    @Override
    public String toString() {
        return String.format("%s | %s %s | %s | %s | spectator %s | %s | $%.2f | receipt %s | %s",
            ticketId, fixtureId, matchName, matchDateTime, stadiumName,
            spectatorId, category, price, receiptNo, purchasedAt);
    }
}

/** Ticket categories. Price = stadium base ticket price x multiplier. */
enum TicketCategory {
    GENERAL(1.0), VIP(2.5), STUDENT(0.5);

    private final double multiplier;
    TicketCategory(double multiplier) { this.multiplier = multiplier; }

    public double priceFor(double basePrice) { return Money.round(basePrice * multiplier); }
}
