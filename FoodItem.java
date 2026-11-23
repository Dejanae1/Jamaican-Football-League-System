/** Food and beverages sold at the stadium shop. */
public class FoodItem extends Product {
    public static final String CATEGORY = "FOOD";

    private final boolean beverage;

    public FoodItem(String id, String name, double price, int stock, boolean beverage)
            throws InvalidDataException {
        super(id, name, price, stock);
        this.beverage = beverage;
    }

    public boolean isBeverage() { return beverage; }

    @Override
    public String getCategory() { return CATEGORY; }

    @Override
    public String getDetails() {
        if (beverage) {
            return "Beverage";
        }
        return "Food";
    }

    @Override
    public String toFileLine() {
        return baseFields() + FileManager.SEPARATOR + beverage;
    }
}
