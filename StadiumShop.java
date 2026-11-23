import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Manages inventory, sales, GCT and receipts for the stadium shop. */
public class StadiumShop {
    public static final int LOW_STOCK_LIMIT = 5;

    private final Map<String, Product> inventory = new LinkedHashMap<>();   // productId -> product
    private final List<Receipt> salesHistory = new ArrayList<>();
    private int receiptCount = 0;

    public void addProduct(Product p) throws InvalidDataException {
        if (inventory.containsKey(p.getId())) {
            throw new InvalidDataException("Product id already exists: " + p.getId());
        }
        inventory.put(p.getId(), p);
    }

    public Product findById(String id) {
        return inventory.get(id);
    }

    public List<Product> searchByName(String keyword) {
        List<Product> result = new ArrayList<>();
        for (Product p : inventory.values()) {
            if (p.getName().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(inventory.values());
    }

    public void restock(String id, int qty) throws InvalidDataException {
        Product p = inventory.get(id);
        if (p == null) {
            throw new InvalidDataException("Unknown product: " + id);
        }
        p.addStock(qty);
    }

    /**
     * Completes a transaction. cart maps productId -> quantity.
     * Stock is only reduced after every check (quantity, stock, payment) has passed.
     */
    public Receipt processSale(Map<String, Integer> cart, double payment) throws LeagueException {
        if (cart == null || cart.isEmpty()) {
            throw new InvalidDataException("Cart is empty");
        }

        List<Receipt.Line> lines = new ArrayList<>();
        for (String id : cart.keySet()) {
            Product p = inventory.get(id);
            if (p == null) {
                throw new InvalidDataException("Unknown product: " + id);
            }
            int qty = cart.get(id);
            if (qty <= 0) {
                throw new InvalidDataException("Quantity must be greater than zero for " + p.getName());
            }
            if (qty > p.getStock()) {
                throw new InsufficientStockException("Only " + p.getStock() + " of '"
                    + p.getName() + "' in stock, requested " + qty);
            }
            lines.add(new Receipt.Line(p.getName(), qty, p.getPrice()));
        }

        Receipt receipt = new Receipt(String.format("S%04d", receiptCount + 1), lines, payment);

        receiptCount++;
        for (String id : cart.keySet()) {
            inventory.get(id).reduceStock(cart.get(id));
        }
        salesHistory.add(receipt);
        return receipt;
    }

    public double totalSales() {
        double total = 0;
        for (Receipt r : salesHistory) {
            total = total + r.getTotal();
        }
        return Money.round(total);
    }

    /** Report: every product plus a sales summary. */
    public String inventoryReport() {
        StringBuilder sb = new StringBuilder("--- SHOP INVENTORY REPORT ---\n");
        for (Product p : inventory.values()) {
            sb.append(p).append("\n");
        }
        sb.append(String.format("Sales so far: %d transaction(s), $%.2f (GCT included)%n",
            salesHistory.size(), totalSales()));
        return sb.toString();
    }

    /** Report: products at or below the low-stock limit. */
    public String lowStockReport() {
        StringBuilder sb = new StringBuilder("--- LOW STOCK (" + LOW_STOCK_LIMIT + " or fewer) ---\n");
        for (Product p : inventory.values()) {
            if (p.getStock() <= LOW_STOCK_LIMIT) {
                sb.append(p).append("\n");
            }
        }
        return sb.toString();
    }
}
