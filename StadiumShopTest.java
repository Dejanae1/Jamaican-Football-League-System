import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class StadiumShopTest {

    private StadiumShop shop;

    @BeforeEach
    void setUp() {
        shop = new StadiumShop();
    }

    // SH-01
    @Test
    void findById_emptyShop_returnsNull() {
        assertNull(shop.findById("P01"));
    }

    // SH-02
    @Test
    void searchByName_emptyShop_returnsEmptyList() {
        assertTrue(shop.searchByName("shirt").isEmpty());
    }

    // SH-03
    @Test
    void getAllProducts_emptyShop_returnsEmptyList() {
        assertTrue(shop.getAllProducts().isEmpty());
    }

    // SH-04
    @Test
    void processSale_nullCart_throws() {
        assertThrows(InvalidDataException.class, () -> shop.processSale(null, 100.0));
    }

    // SH-05
    @Test
    void processSale_emptyCart_throws() {
        assertThrows(InvalidDataException.class, () -> shop.processSale(new HashMap<>(), 100.0));
    }

    // SH-06
    @Test
    void processSale_unknownProduct_throws() {
        Map<String, Integer> cart = new HashMap<>();
        cart.put("X", 1);
        assertThrows(InvalidDataException.class, () -> shop.processSale(cart, 100.0));
    }

    // SH-07
    @Test
    void restock_unknownProduct_throws() {
        assertThrows(InvalidDataException.class, () -> shop.restock("X", 5));
    }

    // SH-08
    @Test
    void totalSales_noSales_returnsZero() {
        assertEquals(0.0, shop.totalSales(), 0.001);
    }

    // SH-09
    @Test
    void inventoryReport_emptyShop_hasTitleAndZeroSales() {
        String report = shop.inventoryReport();
        assertTrue(report.contains("SHOP INVENTORY REPORT"));
        assertTrue(report.contains("0 transaction(s)"));
    }

    // SH-10
    @Test
    void lowStockReport_emptyShop_hasTitle() {
        assertTrue(shop.lowStockReport().contains("LOW STOCK (5 or fewer)"));
    }

    // SH-11
    @Test
    void lowStockLimit_is5() {
        assertEquals(5, StadiumShop.LOW_STOCK_LIMIT);
    }
}