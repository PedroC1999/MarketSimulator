import java.io.File;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.*;

public class Main {
    protected static boolean companiesLoaded = false;
    protected static boolean marketReady = false;
    protected static Market market;
    protected static Trader t = null;

    /**
     * Main launch method, previously used for integration testing and remnants still visible, but commented out.
     * Now simply launches main UI.
     *
     * @param args
     * @throws InterruptedException
     * @throws InsufficientFundsException
     * @throws InsufficientResourcesException
     */
    public static void main(String[] args) throws InterruptedException, InsufficientFundsException, InsufficientResourcesException {
        //testCompanies();
        //testMarket();
        //testController();
        //testTraders();
        UI(args);
    }

    /**
     * Main UI.
     *
     * @param args
     */
    public static void UI(String[] args) {
        System.out.println("Market Simulator");
        System.out.println("Ready: " + marketReady);
        if (market == null) {
            System.out.println("Active: false");
        } else {
            System.out.println("Active: " + market.isActive());
        }
        if (t == null) {
            System.out.println("Trader: false");
        } else {
            System.out.println("Trader: true");
        }
        boolean running = true;
        while (running) {
            System.out.println("Please select an option:");
            System.out.println(" 1 - Help");
            if (!marketReady) System.out.println(" 2 - Setup Market");
            if (marketReady) System.out.println(" 3 - Enter Market");
            System.out.println(" Q - Exit");
            System.out.print("Option: ");
            Scanner scan = new Scanner(System.in);
            if (scan.hasNextInt()) {
                switch (scan.nextInt()) {
                    case 1 -> {
                        helpUI();
                    }
                    case 2 -> {
                        if (!marketReady) setupMarket();
                    }
                    case 3 -> {
                        if (marketReady) {
                            createTrader();
                        }
                    }
                    default -> System.out.println("Invalid option, please try again\n");
                }
            } else {
                if (scan.next().equals("Q")) {
                    System.out.println("Exiting");
                    System.out.println("Status : Exited");
                    System.exit(0);
                } else {
                    System.out.println("Invalid option, please try again");
                }
            }
        }
    }

    /**
     * Allows user to interact with the  Market.
     */
    public static void enterMarket() {
        boolean validChoice = false;
        while (!validChoice) {
            System.out.println("Date: " + market.getCurrentDate());
            System.out.println("Active: " + market.isActive());
            System.out.println("Please select an option:");
            System.out.println(" 1 - Output Current Company Prices");
            System.out.println(" 2 - Output Portfolio");
            System.out.println(" 3 - Get Company Data");
            System.out.println(" 4 - Advance Market");
            System.out.println(" 5 - Buy/Sell");
            System.out.println(" 6 - Toggle Market");
            System.out.println(" Q - Exit");
            System.out.print("Option: ");
            Scanner scan = new Scanner(System.in);
            if (scan.hasNextInt()) {
                switch (scan.nextInt()) {
                    case 1 -> {
                        System.out.println("Date: " + market.getCurrentDate());
                        for (String key : market.getCompanies().keySet()) {
                            Company currentCompany = market.getCompanies().get(key);
                            System.out.println(key + ": " + currentCompany.getDailyData(market.getCurrentDate()).getEndValue());
                        }
                    }
                    case 2 -> {
                        System.out.println("Date: " + market.getCurrentDate());
                        System.out.println(t);
                    }
                    case 3 -> {
                        getCompanyNameMenu();
                    }
                    case 4 -> {
                        advanceMarketMenu();
                    }
                    case 5 -> {
                        tradeMenu();
                    }
                    case 6 -> {
                        market.toggleActive();
                    }
                }
            } else {
                if (scan.next().equals("Q")) {
                    System.out.println("Exiting");
                    System.out.println("Status : Exited");
                    System.exit(0);
                } else {
                    System.out.println("Invalid option, please try again");
                }
            }
        }
    }

    /**
     * Menu for buy or selling stocks
     */
    public static void tradeMenu() {
        boolean validChoice = false;
        while (!validChoice) {
            System.out.println("Funds: " + t.getCurrentFunds());
            System.out.println("Please select an option:");
            System.out.println(" 1 - Buy");
            System.out.println(" 2 - Sell");
            System.out.println(" 3 - Sell All");
            System.out.println(" 4 - Market Menu");
            System.out.println(" Q - Exit");
            System.out.print("Option: ");
            Scanner scan = new Scanner(System.in);
            if (scan.hasNextInt()) {
                switch (scan.nextInt()) {
                    case 1 -> {
                        enterCompanyName(true);
                    }
                    case 2 -> {
                        enterCompanyName(false);
                    }
                    case 3 -> {
                        t.sellAll();
                    }
                    case 4 -> {
                        validChoice = true;
                    }
                    default -> System.out.println("Invalid option, please try again\n");
                }
            } else {
                if (scan.next().equals("Q")) {
                    System.out.println("Exiting");
                    System.out.println("Status : Exited");
                    System.exit(0);
                } else {
                    System.out.println("Invalid option, please try again");
                }
            }
        }
    }

    /**
     * Facilitates data entry for company name.
     *
     * @param isBuy whether the transaction is a buy or sell.
     */
    public static void enterCompanyName(boolean isBuy){
        boolean validChoice = false;
        while (!validChoice) {
            System.out.println("Please enter Company Ticker");
            System.out.print("Ticker: ");
            Scanner scan = new Scanner(System.in);
            if (scan.hasNext()) {
                enterStockAmount(isBuy, scan.next());
                validChoice = true;
            } else {
                if (scan.next().equals("Q")) {
                    System.out.println("Exiting");
                    System.out.println("Status : Exited");
                    System.exit(0);
                } else {
                    System.out.println("Invalid option, please try again");
                }
            }
        }
    }

    /**
     * Facilitates data entry for stock amount being bought.
     *
     * @param isBuy whether the transaction is a buy or sell.
     */
    public static void enterStockAmount(boolean isBuy, String ticker){
        boolean validChoice = false;
        while (!validChoice) {
            System.out.println("Please enter amount of stock to buy/sell");
            System.out.print("Amount: ");
            Scanner scan = new Scanner(System.in);
            if (scan.hasNextInt()) {
                if (isBuy){
                    t.buy(ticker, scan.nextInt());
                } else {
                    t.sell(ticker, scan.nextInt());
                }
                validChoice = true;
            } else {
                if (scan.next().equals("Q")) {
                    System.out.println("Exiting");
                    System.out.println("Status : Exited");
                    System.exit(0);
                } else {
                    System.out.println("Invalid option, please try again");
                }
            }
        }
    }

    /**
     * Menu that allows market time to be advanced.
     */
    public static void advanceMarketMenu() {
        boolean validChoice = false;
        while (!validChoice) {
            System.out.println("How many days should the market be advanced by?");
            System.out.print("Days: ");
            Scanner scan = new Scanner(System.in);
            if (scan.hasNextInt()) {
                market.addDays(scan.nextInt());
                validChoice = true;
            } else {
                if (scan.next().equals("Q")) {
                    System.out.println("Exiting");
                    System.out.println("Status : Exited");
                    System.exit(0);
                } else {
                    System.out.println("Invalid option, please try again");
                }
            }
        }
    }

    /**
     * Finds and outputs company data
     */
    public static void getCompanyNameMenu() {
        boolean validChoice = false;
        while (!validChoice) {
            System.out.println("Please enter Company Ticker");
            System.out.print("Ticker: ");
            Scanner scan = new Scanner(System.in);
            if (scan.hasNext()) {
                Company currentCompany = market.getCompanies().get(scan.next());
                System.out.println(currentCompany);
                System.out.println(currentCompany.getDailyData(market.getCurrentDate()));
                validChoice = true;
            } else {
                if (scan.next().equals("Q")) {
                    System.out.println("Exiting");
                    System.out.println("Status : Exited");
                    System.exit(0);
                } else {
                    System.out.println("Invalid option, please try again");
                }
            }
        }
    }


    /**
     * Creates a trader using user input
     */
    public static void createTrader() {
        boolean validChoice = false;
        while (!validChoice) {
            System.out.println("Please enter Trader starting funds:");
            Scanner scan = new Scanner(System.in);
            if (scan.hasNextInt()) {
                t = new Trader(market, scan.nextInt());
                enterMarket();
            } else {
                if (scan.next().equals("Q")) {
                    System.out.println("Exiting");
                    System.out.println("Status : Exited");
                    System.exit(0);
                } else {
                    System.out.println("Invalid option, please try again");
                }
            }
        }
    }

    /**
     * Goes through the inout steps required to initialise a market object.
     */
    public static void setupMarket() {
        Hashtable<String, Company> loadedData = new Hashtable<>();
        boolean validChoice = false;
        while (!validChoice) {
            System.out.println("Load company data from location: ");
            System.out.println(" 1 - Default ~/data folder");
            System.out.println(" 2 - Other folder");
            System.out.println(" Q - Exit");
            System.out.print("Option: ");
            Scanner scan = new Scanner(System.in);
            if (scan.hasNextInt()) {
                switch (scan.nextInt()) {
                    case 1 -> {
                        System.out.println(" 1 - Loading from default data folder");
                        loadedData = loadData("Implementation/data/");
                        System.out.println("Status : Company Data Loaded");
                        validChoice = true;
                    }
                    case 2 -> {
                        System.out.println(" 2 - Please input the absolute folder path for data loading:");
                        scan = new Scanner(System.in);
                        loadedData = loadData(scan.next());
                        validChoice = true;
                    }
                }
            } else {
                if (scan.next().equals("Q")) {
                    System.out.println("Exiting");
                    System.out.println("Status : Exited");
                    System.exit(0);
                } else {
                    System.out.println("Invalid option, please try again");
                }
            }
        }
        StringBuilder tickerNames = new StringBuilder();
        tickerNames.append("Following Companies have been loaded: ");
        Iterator<String> e = loadedData.keys().asIterator();
        while (e.hasNext()) {
            String currentKey = e.next();
            tickerNames.append(currentKey);
            if (e.hasNext()) {
                tickerNames.append(", ");
            }
        }
        System.out.println(tickerNames);
        LocalDate currentLocalDate = LocalDate.now();
        validChoice = false;
        while (!validChoice) {
            System.out.println("Please enter Market start date in DD-MM-YYYY format");
            System.out.print("Date: ");
            Scanner scan = new Scanner(System.in);
            if (scan.hasNext()) {
                String currentDateInput = scan.next();
                char[] currentDate = currentDateInput.toCharArray();
                try {
                    int currentYear = Integer.parseInt(new String(currentDate, 6, 4));
                    int currentMonth = Integer.parseInt(new String(currentDate, 3, 2));
                    int currentDay = Integer.parseInt(new String(currentDate, 0, 2));
                    currentLocalDate = LocalDate.of(currentYear, currentMonth, currentDay);
                    validChoice = true;
                } catch (Exception j) {
                    j.printStackTrace();
                    System.out.println("Invalid option, please try again\n");
                }
            }
        }
        validChoice = false;
        while (!validChoice) {
            System.out.println("Create Market using provided data?");
            System.out.println(" 1 - Yes");
            System.out.println(" Q - Exit");
            System.out.print("Option: ");
            Scanner scan = new Scanner(System.in);
            if (scan.hasNextInt()) {
                if (scan.nextInt() == 1) {
                    market = new Market(loadedData, currentLocalDate);
                    validChoice = true;
                    marketReady = true;
                }
            } else {
                if (scan.next().equals("Q")) {
                    System.out.println("Exiting");
                    System.out.println("Status : Exited");
                    System.exit(0);
                } else {
                    System.out.println("Invalid option, please try again");
                }
            }
        }
        System.out.println(market);
        market.toggleActive();
    }

    /**
     * Method that aids in loading all data in a folder.
     *
     * @param folderPath Folder path to load data from.
     * @return Completed Hashtable of Companies, Indexed by Company name.
     */
    public static Hashtable<String, Company> loadData(String folderPath) {
        ArrayList<String> tickerNames = new ArrayList<>();
        File[] files = new File(folderPath).listFiles();
        Hashtable<String, Company> loadedData = new Hashtable<>();
        assert files != null;
        for (File file : files) {
            if (file.isFile()) {
                String currentPath = folderPath + file.getName();
                String currentTicker = file.getName().replace(".csv", "");
                loadedData.put(currentTicker, new Company(currentPath, currentTicker));
                tickerNames.add(currentTicker);
            }
        }
        return loadedData;
    }

    public static void helpUI() {
        System.out.println(" - ");
    }


    public static void testCompanies() {
        ArrayList<Company> data = new ArrayList<>();
        data.add(new Company("Implementation/data/AAPL.csv", "AAPL"));
        data.add(new Company("Implementation/data/AMD.csv", "AMD"));
        data.add(new Company("Implementation/data/AMZN.csv", "AMZN"));
        data.add(new Company("Implementation/data/META.csv", "META"));
        data.add(new Company("Implementation/data/NFLX.csv", "NFLX"));
        data.add(new Company("Implementation/data/QCOM.csv", "QCOM"));
        data.add(new Company("Implementation/data/SBUX.csv", "SBUX"));
        data.add(new Company("Implementation/data/TSLA.csv", "TSLA"));

        LocalDate localDate = LocalDate.of(2020, 11, 24);
        //Market market = new Market(data, localDate., 5000);

        for (Company i : data) {
            //System.out.println(i.toString());
            System.out.println(i.getDailyData(localDate));
        }
    }

    public static void testMarket() {
        Hashtable<String, Company> data = new Hashtable<>();
        data.put("AAPL", new Company("Implementation/data/AAPL.csv", "AAPL"));
        data.put("AMD", new Company("Implementation/data/AMD.csv", "AMD"));
        data.put("AMZN", new Company("Implementation/data/AMZN.csv", "AMZN"));
        data.put("META", new Company("Implementation/data/META.csv", "META"));
        data.put("NFLX", new Company("Implementation/data/NFLX.csv", "NFLX"));
        data.put("QCOM", new Company("Implementation/data/QCOM.csv", "QCOM"));
        data.put("SBUX", new Company("Implementation/data/SBUX.csv", "SBUX"));
        data.put("TSLA", new Company("Implementation/data/TSLA.csv", "TSLA"));

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
        data.put("AAPL", new Company("Implementation/data/AAPL.csv", "AAPL"));
        data.put("AMD", new Company("Implementation/data/AMD.csv", "AMD"));
        data.put("AMZN", new Company("Implementation/data/AMZN.csv", "AMZN"));
        data.put("META", new Company("Implementation/data/META.csv", "META"));
        data.put("NFLX", new Company("Implementation/data/NFLX.csv", "NFLX"));
        data.put("QCOM", new Company("Implementation/data/QCOM.csv", "QCOM"));
        data.put("SBUX", new Company("Implementation/data/SBUX.csv", "SBUX"));
        data.put("TSLA", new Company("Implementation/data/TSLA.csv", "TSLA"));
        data.put("KLAC", new Company("Implementation/data/KLAC.csv", "KLAC"));

        LocalDate localDate = LocalDate.of(2020, 11, 23);
        Market market = new Market(data, localDate);

        market.toggleActive();

        Trader a1 = new Trader(market, 5000);
        a1.buy("KLAC", 10);
        a1.buy("QCOM", 10);
        System.out.println(a1);
        a1.buy("KLAC", 3);
        System.out.println(a1);
        market.addDays(365);
        System.out.println(a1);
        a1.sell("KLAC", 10);
        System.out.println(a1);
        a1.sellAll();
        System.out.println(a1);
    }
}