import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Random;
import java.util.Set;

public class RequirementsTesting {
    @Test
    public static void main(String[] args) {
        int numTransactions = 0;
        int numDailyData = 0;
        long startTime = System.nanoTime();
        // Create a new Random object
        Random random = new Random();
        Set<String> combinations = new HashSet<>();
        while (combinations.size() < 500) {
            // Generate a random character between 'A' and 'Z'
            char c = (char) (random.nextInt(26) + 'A');

            int n = random.nextInt(1000);

            // Convert the integer to a string and concatenate it with the character
            String combination = c + Integer.toString(n);

            combinations.add(combination);
        }

        Hashtable<String, Company> companies = new Hashtable<>();

        for (String combination : combinations) {
            LocalDate date = LocalDate.now().minusDays(10000);
            Hashtable<LocalDate, DailyData> currentCompanyData = new Hashtable<>();
            Company currentCompany = new Company(combination);
            for(int i = 0; i < 10000; i++){
                date = date.plusDays(1);
                DailyData current = new DailyData(combination, date, 50, 50, 50, 50, 50);
                currentCompany.addDailyData(current);
                numDailyData++;
            }
            companies.put(combination, currentCompany);
        }
        Market currentMarket = new Market(companies, LocalDate.now());
        System.out.println(currentMarket);
        String[] companyTickers = combinations.toArray(new String[combinations.size()]);
        Set<Trader> traders = new HashSet();
        while (traders.size() < 1000) {
            Trader currentTrader = new Trader(currentMarket, 1000000);
            for(int i = 0; i < 10000; i++){
                // generate a random number
                int rndmNumber = random.nextInt(combinations.size());

                currentTrader.buy(companyTickers[rndmNumber], 1);
                numTransactions++;
            }
            System.out.println(currentTrader);
            traders.add(currentTrader);
        }

        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        System.out.println("Elapsed time: " + elapsedTime/ 1000000000.0 + " seconds");

        startTime = System.nanoTime();
        traders.forEach(trader -> trader.sellAll());
        endTime = System.nanoTime();
        elapsedTime = endTime - startTime;
        System.out.println("Elapsed time: " + elapsedTime/ 1000000000.0 + " seconds");

        System.out.println("Number of Transactions:" + numTransactions);
        System.out.println("Number of DailyDatas:" + numDailyData);
    }
}
