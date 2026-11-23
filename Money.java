/** Money helpers. All KPFL prices already INCLUDE 15% GCT (see brief, section 2). */
public final class Money {
    public static final double GCT_RATE = 0.15;
    private Money() { }

    public static double round(double v) { return Math.round(v * 100.0) / 100.0; }

    /** GCT contained inside a tax-inclusive total. */
    public static double gctIncluded(double total) {
        return round(total - total / (1 + GCT_RATE));
    }
}
