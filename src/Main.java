import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileReader;
import java.time.LocalDate;
import java.util.*;

public class Main {


    public static void main(String[] args) throws InterruptedException, InsufficientFundsException, InsufficientResourcesException {
        //testCompanies();
        //testMarket();
        //testController();
        testTraders();
    }

    public static void testCompanies(){
        ArrayList<Company> data = new ArrayList<>();
        data.add(new Company("data/AAPL.csv", "AAPL"));
        data.add(new Company("data/AMD.csv", "AMD"));
        data.add(new Company("data/AMZN.csv", "AMZN"));
        data.add(new Company("data/META.csv", "META"));
        data.add(new Company("data/NFLX.csv", "NFLX"));
        data.add(new Company("data/QCOM.csv", "QCOM"));
        data.add(new Company("data/SBUX.csv", "SBUX"));
        data.add(new Company("data/TSLA.csv", "TSLA"));

        LocalDate localDate = LocalDate.of(2020, 11, 24);
        //Market market = new Market(data, localDate., 5000);

        for (Company i:data) {
            //System.out.println(i.toString());
            System.out.println(i.getDailyData(localDate));
        }
    }

    public static void testMarket(){
        Hashtable<String, Company> data = new Hashtable<>();
        data.put("AAPL", new Company("data/AAPL.csv", "AAPL"));
        data.put("AMD", new Company("data/AMD.csv", "AMD"));
        data.put("AMZN", new Company("data/AMZN.csv", "AMZN"));
        data.put("META", new Company("data/META.csv", "META"));
        data.put("NFLX", new Company("data/NFLX.csv", "NFLX"));
        data.put("QCOM", new Company("data/QCOM.csv", "QCOM"));
        data.put("SBUX", new Company("data/SBUX.csv", "SBUX"));
        data.put("TSLA", new Company("data/TSLA.csv", "TSLA"));

        LocalDate localDate = LocalDate.of(2020, 11, 24);
        Market market = new Market(data, localDate);
        System.out.println(market);

        market.nextDay();
        market.nextDay();
        market.nextDay();
        System.out.println(market);

    }

    public static void testController() throws InterruptedException {
        Hashtable<String, Company> data = new Hashtable<>();
        data.put("AAPL", new Company("data/AAPL.csv", "AAPL"));
        data.put("AMD", new Company("data/AMD.csv", "AMD"));
        data.put("AMZN", new Company("data/AMZN.csv", "AMZN"));
        data.put("META", new Company("data/META.csv", "META"));
        data.put("NFLX", new Company("data/NFLX.csv", "NFLX"));
        data.put("QCOM", new Company("data/QCOM.csv", "QCOM"));
        data.put("SBUX", new Company("data/SBUX.csv", "SBUX"));
        data.put("TSLA", new Company("data/TSLA.csv", "TSLA"));

        LocalDate localDate = LocalDate.of(2020, 11, 26);
        Market market = new Market(data, localDate);

        Controller controller = new Controller(market, 1000);
        market.toggleActive();
        controller.run();
        Thread.sleep(10000);


    }

    public static void testTraders() throws InsufficientFundsException, InsufficientResourcesException {
        Hashtable<String, Company> data = new Hashtable<>();
        data.put("AAPL", new Company("data/AAPL.csv", "AAPL"));
        data.put("AMD", new Company("data/AMD.csv", "AMD"));
        data.put("AMZN", new Company("data/AMZN.csv", "AMZN"));
        data.put("META", new Company("data/META.csv", "META"));
        data.put("NFLX", new Company("data/NFLX.csv", "NFLX"));
        data.put("QCOM", new Company("data/QCOM.csv", "QCOM"));
        data.put("SBUX", new Company("data/SBUX.csv", "SBUX"));
        data.put("TSLA", new Company("data/TSLA.csv", "TSLA"));
        data.put("KLAC", new Company("data/KLAC.csv", "KLAC"));

        LocalDate localDate = LocalDate.of(2020, 11, 23);
        Market market = new Market(data, localDate);

        market.toggleActive();

        Trader a1 = new Trader(market, 5000);
        a1.buy(market, "KLAC", 10);
        a1.buy(market, "QCOM", 10);
        System.out.println(a1);
        market.addDays(365);
        System.out.println(a1);
        a1.sell(market, "KLAC", 9);
        System.out.println(a1);


    }
}