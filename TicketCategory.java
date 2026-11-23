/** Ticket categories. Price = stadium base ticket price x multiplier. */
public enum TicketCategory {
    GENERAL(1.0), VIP(2.5), STUDENT(0.5);

    private final double multiplier;
    TicketCategory(double multiplier) { this.multiplier = multiplier; }

    public double priceFor(double basePrice) { return Money.round(basePrice * multiplier); }
}
