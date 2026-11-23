/** Jerseys, flags, scarves and similar items. */
public class Merchandise extends Product {
    public static final String CATEGORY = "MERCH";

    private final String size;
    private final String colour;

    public Merchandise(String id, String name, double price, int stock, String size, String colour)
            throws InvalidDataException {
        super(id, name, price, stock);
        if (size == null || colour == null) {
            throw new InvalidDataException("Size and colour are required");
        }
        this.size = size.trim();
        this.colour = colour.trim();
    }

    public String getSize() { return size; }
    public String getColour() { return colour; }

    @Override
    public String getCategory() { return CATEGORY; }

    @Override
    public String getDetails() { return "Size " + size + ", " + colour; }

    @Override
    public String toFileLine() {
        return baseFields() + FileManager.SEPARATOR + size + FileManager.SEPARATOR + colour;
    }
}
