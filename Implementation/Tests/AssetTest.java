import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Hashtable;

public class AssetTest {
    private Asset asset;
    private Market market;

    // This method is run before each test to create a new Asset object and a new Market object
    @Before
    public void setUp() {
        asset = new Asset();
        Company testCompany = new Company("ABC");
        Hashtable<String, Company> testHashtable = new Hashtable<String, Company>();
        testHashtable.put("ABC", testCompany);
        market = new Market(testHashtable, LocalDate.of(2020, 1, 1));

    }

    // Test the getQuantityOwned method
    @Test
    public void testGetQuantityOwned() {
        // Initially, the quantity owned should be 0
        assertEquals(0, asset.getQuantityOwned(), 0.001);
    }

    // Test the buy method
    @Test
    public void testBuy() {
        // After buying 10 stocks, the quantity owned should be 10
        asset.buy(market, "ABC", 10);
        assertEquals(10, asset.getQuantityOwned(), 0.001);
    }

    // Test the sell method
    @Test
    public void testSell() {
        asset.buy(market, "ABC", 10);
        // After selling 10 stocks, the quantity owned should be 0
        asset.sell(market, "ABC", 10);
        assertEquals(0, asset.getQuantityOwned(), 0.001);
    }

    // Test the toJson method
    @Test
    public void testToJson() {
        // The toJson method should return a JSON string representation of the Asset object
        String expected = "{\"transactions\":[],\"quantityOwned\":0.0}";
        String actual = asset.toJson();
        assertEquals(expected, actual);
    }
}
