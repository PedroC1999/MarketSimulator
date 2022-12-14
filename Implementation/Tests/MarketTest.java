import org.junit.Before;
import org.junit.Test;
import java.time.LocalDate;
import java.util.Hashtable;

import static org.junit.Assert.*;

public class MarketTest {
    private Market market;
    private Hashtable<String, Company> companies;
    private LocalDate startDate;

    @Before
    public void setUp() {
        companies = new Hashtable<>();
        startDate = LocalDate.of(2022, 12, 11);
        market = new Market(companies, startDate);
    }

    @Test
    public void testStart() {
        assertFalse(market.isActive());
        market.start();
        assertTrue(market.isActive());
    }

    @Test
    public void testStop() {
        market.start();
        assertTrue(market.isActive());
        market.stop();
        assertFalse(market.isActive());
    }

    @Test
    public void testReset() {
        market.start();
        market.addDays(5);
        assertEquals(LocalDate.of(2022, 12, 16), market.getCurrentDate());
        market.reset();
        assertEquals(startDate, market.getCurrentDate());
    }

    @Test
    public void testGetCompanies() {
        assertEquals(companies, market.getCompanies());
    }

    @Test
    public void testFindCompany() {
        Company company1 = new Company("TEST1", "Test Company 1");
        Company company2 = new Company("TEST2", "Test Company 2");
        companies.put("TEST1", company1);
        companies.put("TEST2", company2);

        assertEquals(company1, market.findCompany("TEST1"));
        assertEquals(company2, market.findCompany("TEST2"));
        assertNull(market.findCompany("TEST3"));
    }

    @Test
    public void testFindLatestDailyData() {
        // Create a Market object with a few companies, each with several DailyData objects
        Hashtable<String, Company> companies = new Hashtable<>();
        LocalDate currentDate = LocalDate.now();

        Company company1 = new Company("ABC");
        company1.addDailyData(new DailyData("ABC", currentDate.minusDays(2), 10, 20, 30, 40, 50));
        company1.addDailyData(new DailyData("ABC", currentDate.minusDays(1), 20, 30, 40, 50, 60));
        company1.addDailyData(new DailyData("ABC", currentDate, 30, 40, 50, 60, 70));
        Company company2 = new Company("DEF");
        company2.addDailyData(new DailyData("DEF", currentDate.minusDays(3), 15, 25, 35, 45, 55));
        company2.addDailyData(new DailyData("DEF", currentDate.minusDays(2), 25, 35, 45, 55, 65));
        company2.addDailyData(new DailyData("DEF", currentDate.minusDays(1), 35, 45, 55, 65, 75));
        company2.addDailyData(new DailyData("DEF", currentDate, 45, 55, 65, 75, 85));
        companies.put("ABC", company1);
        companies.put("DEF", company2);
        Market market = new Market(companies, currentDate);

        // Test findLatestDailyData for each company
        assertEquals(market.findLatestDailyData("ABC"), company1.getDailyData(currentDate));
        assertEquals(market.findLatestDailyData("DEF"), company2.getDailyData(currentDate));
    }

    @Test
    public void testAddDays() {
        // Create a Market object with a specific start date
        LocalDate startDate = LocalDate.of(2022, 1, 1);
        Market market = new Market(new Hashtable<>(), startDate);

        // Test addDays with different values
        assertEquals(market.getCurrentDate(), startDate);
        market.addDays(5);
        assertEquals(market.getCurrentDate(), startDate.plusDays(5));
        market.addDays(-3);
        assertEquals(market.getCurrentDate(), startDate.plusDays(2));
        market.addDays(0);
        assertEquals(market.getCurrentDate(), startDate.plusDays(2));
    }

    @Test
    public void testIsActive() {
        // Create a Market object
        Market market = new Market(new Hashtable<>(), LocalDate.now());

        // Test isActive before and after calling start and stop
        assertFalse(market.isActive());
        market.start();
        assertTrue(market.isActive());
        market.stop();
        assertFalse(market.isActive());
    }

    @Test
    public void testAddTrader() {
        Market market = new Market(new Hashtable<>(), LocalDate.now());
        Trader trader = new Trader(market, 5000);

        market.addTrader(trader);
        assertTrue(market.getControlledTraders().contains(trader));

        // Adding the same trader again should not result in a duplicate
        market.addTrader(trader);
        assertEquals(1, market.getControlledTraders().size());
    }
}


