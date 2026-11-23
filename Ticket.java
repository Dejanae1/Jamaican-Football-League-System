/** A single match ticket. The ticketId is the ticket's code. */
public class Ticket {
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
