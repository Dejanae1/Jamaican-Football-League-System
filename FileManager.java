import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/** Sequential-access text-file persistence: one record per line, fields joined by SEPARATOR. */
public class FileManager {
    public static final String SEPARATOR = "|";
    private static final String SEPARATOR_REGEX = "\\|";

    // number of fields in each kind of record
    private static final int MERCH_FIELDS = 7;
    private static final int FOOD_FIELDS = 6;
    private static final int TICKET_FIELDS = 10;

    /** Rejects blank text, and text containing the separator (it would break the file format). */
    public static void checkField(String value, String label) throws InvalidDataException {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidDataException(label + " is required");
        }
        if (value.contains(SEPARATOR)) {
            throw new InvalidDataException(label + " cannot contain " + SEPARATOR);
        }
    }

    public static void saveProducts(List<Product> products, String path) throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter(path));
        for (Product p : products) {
            out.println(p.toFileLine());
        }
        out.close();
    }

    public static List<Product> loadProducts(String path) throws IOException, InvalidDataException {
        List<Product> products = new ArrayList<>();
        BufferedReader in = new BufferedReader(new FileReader(path));
        String line = in.readLine();
        int lineNo = 1;
        while (line != null) {
            if (!line.trim().isEmpty()) {
                products.add(parseProduct(line, lineNo));
            }
            line = in.readLine();
            lineNo++;
        }
        in.close();
        return products;
    }

    private static Product parseProduct(String line, int lineNo) throws InvalidDataException {
        String[] f = line.split(SEPARATOR_REGEX, -1);
        try {
            double price = Double.parseDouble(f[3]);
            int stock = Integer.parseInt(f[4]);
            if (f[0].equals(Merchandise.CATEGORY) && f.length == MERCH_FIELDS) {
                return new Merchandise(f[1], f[2], price, stock, f[5], f[6]);
            }
            if (f[0].equals(FoodItem.CATEGORY) && f.length == FOOD_FIELDS) {
                return new FoodItem(f[1], f[2], price, stock, Boolean.parseBoolean(f[5]));
            }
        } catch (NumberFormatException e) {
            // handled below
        }
        throw new InvalidDataException("Corrupt product record on line " + lineNo + ": " + line);
    }

    public static void saveTickets(List<Ticket> tickets, String path) throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter(path));
        for (Ticket t : tickets) {
            out.println(t.toFileLine());
        }
        out.close();
    }

    public static List<Ticket> loadTickets(String path) throws IOException, InvalidDataException {
        List<Ticket> tickets = new ArrayList<>();
        BufferedReader in = new BufferedReader(new FileReader(path));
        String line = in.readLine();
        int lineNo = 1;
        while (line != null) {
            if (!line.trim().isEmpty()) {
                tickets.add(parseTicket(line, lineNo));
            }
            line = in.readLine();
            lineNo++;
        }
        in.close();
        return tickets;
    }

    private static Ticket parseTicket(String line, int lineNo) throws InvalidDataException {
        String[] f = line.split(SEPARATOR_REGEX, -1);
        if (f.length == TICKET_FIELDS) {
            try {
                return new Ticket(f[0], f[1], f[2], f[3], f[4], f[5],
                    TicketCategory.valueOf(f[6]), Double.parseDouble(f[7]), f[8], f[9]);
            } catch (IllegalArgumentException e) {
                // bad category or bad number, handled below
            }
        }
        throw new InvalidDataException("Corrupt ticket record on line " + lineNo + ": " + line);
    }
}
