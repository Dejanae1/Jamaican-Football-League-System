/** Abstract superclass for everything the stadium shop sells. */
public abstract class Product {
    private final String id;
    private String name;
    private double price;   // unit price, GCT included
    private int stock;

    protected Product(String id, String name, double price, int stock) throws InvalidDataException {
        if (id == null || id.trim().isEmpty()) {
            throw new InvalidDataException("Product id is required");
        }
        this.id = id.trim();
        setName(name);
        setPrice(price);
        setStock(stock);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }

    public void setName(String name) throws InvalidDataException {
        FileManager.checkField(name, "Product name");
        this.name = name.trim();
    }

    public void setPrice(double price) throws InvalidDataException {
        if (price < 0) {
            throw new InvalidDataException("Price cannot be negative");
        }
        this.price = price;
    }

    public void setStock(int stock) throws InvalidDataException {
        if (stock < 0) {
            throw new InvalidDataException("Stock cannot be negative");
        }
        this.stock = stock;
    }

    public void addStock(int qty) throws InvalidDataException {
        if (qty <= 0) {
            throw new InvalidDataException("Restock quantity must be greater than zero");
        }
        stock = stock + qty;
    }

    /** The shop checks stock before calling this, so it only subtracts. */
    public void reduceStock(int qty) {
        stock = stock - qty;
    }

    /** Subclasses say what kind of product they are, e.g. "FOOD". */
    public abstract String getCategory();

    /** Subclass-specific details shown in listings. */
    public abstract String getDetails();

    /** One line for the data file. */
    public abstract String toFileLine();

    /** The fields every product has, joined for the data file. */
    protected String baseFields() {
        String sep = FileManager.SEPARATOR;
        return getCategory() + sep + id + sep + name + sep + price + sep + stock;
    }

    @Override
    public String toString() {
        return String.format("%-6s %-22s $%8.2f  stock:%-4d [%s] %s",
            id, name, price, stock, getCategory(), getDetails());
    }
}
