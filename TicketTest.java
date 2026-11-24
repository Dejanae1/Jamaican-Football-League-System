import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TicketTest {

    private Ticket make(double price) throws InvalidDataException {
        return new Ticket("T001", "F01", "Waterhouse v Portmore", "2026-12-05 15:00",
                "Independence Park", "SP01", null, price, "S0001", "2026-11-23 10:00");
    }

    // TK-01
    @Test
    void create_validPrice_isCreated() throws Exception {
        Ticket t = make(2500);
        assertEquals(2500, t.getPrice(), 0.001);
    }

    // TK-02
    @Test
    void price_zero_accepted() throws Exception {
        assertEquals(0, make(0).getPrice(), 0.001);
    }

    // TK-03
    @Test
    void price_justBelowZero_throws() {
        assertThrows(InvalidDataException.class, () -> make(-0.01));
    }

    // TK-04
    @Test
    void price_negative_throws() {
        assertThrows(InvalidDataException.class, () -> make(-500));
    }

    // TK-05
    @Test
    void getters_returnStoredValues() throws Exception {
        Ticket t = make(2500);
        assertEquals("T001", t.getTicketId());
        assertEquals("F01", t.getFixtureId());
        assertEquals("SP01", t.getSpectatorId());
        assertEquals("S0001", t.getReceiptNo());
    }

    // TK-06
    @Test
    void toString_containsTicketId() throws Exception {
        assertTrue(make(2500).toString().contains("T001"));
    }
}