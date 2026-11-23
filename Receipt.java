import java.util.ArrayList;
import java.util.List;

/** Receipt for a ticket sale or a shop sale. All prices include GCT. */
public class Receipt {

    /** One row on the receipt. */
    public static class Line {
        private final String description;
        private final int quantity;
        private final double unitPrice;

        public Line(String description, int quantity, double unitPrice) {
            this.description = description;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
        }

        public String getDescription() { return description; }
        public int getQuantity() { return quantity; }
        public double getUnitPrice() { return unitPrice; }

        public double getLineTotal() {
            return Money.round(unitPrice * quantity);
        }
    }

    private final String receiptNo;
    private final List<Line> lines;
    private final double total;      // what the customer pays (GCT included)
    private final double tax;        // GCT part of the total
    private final double subtotal;   // total minus GCT
    private final double payment;
    private final double change;

    public Receipt(String receiptNo, List<Line> lines, double payment)
            throws InsufficientPaymentException {
        double sum = 0;
        for (Line l : lines) {
            sum = sum + l.getLineTotal();
        }
        double roundedTotal = Money.round(sum);
        if (Money.round(payment) < roundedTotal) {
            throw new InsufficientPaymentException(String.format(
                "Payment of $%.2f is less than the total of $%.2f", payment, roundedTotal));
        }

        this.receiptNo = receiptNo;
        this.lines = new ArrayList<>(lines);
        this.total = roundedTotal;
        this.tax = Money.gctIncluded(roundedTotal);
        this.subtotal = Money.round(roundedTotal - tax);
        this.payment = Money.round(payment);
        this.change = Money.round(this.payment - roundedTotal);
    }

    public String getReceiptNo() { return receiptNo; }
    public List<Line> getLines() { return lines; }
    public double getSubtotal() { return subtotal; }
    public double getTax() { return tax; }
    public double getTotal() { return total; }
    public double getPayment() { return payment; }
    public double getChange() { return change; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== KPFL RECEIPT ").append(receiptNo).append(" ===\n");
        for (Line l : lines) {
            sb.append(String.format("%-22s x%-3d @ %9.2f = %10.2f%n",
                l.getDescription(), l.getQuantity(), l.getUnitPrice(), l.getLineTotal()));
        }
        sb.append(String.format("Subtotal:      %10.2f%n", subtotal));
        sb.append(String.format("GCT (%.0f%%):     %10.2f%n", Money.GCT_RATE * 100, tax));
        sb.append(String.format("TOTAL:         %10.2f%n", total));
        sb.append(String.format("Paid:          %10.2f%n", payment));
        sb.append(String.format("Change:        %10.2f%n", change));
        return sb.toString();
    }
}
