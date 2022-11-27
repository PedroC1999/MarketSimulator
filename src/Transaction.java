import java.time.LocalDate;

/**
 * The Transaction object, each transaction represents a buy or sale on the Market.
 * Stores data that can be important tp advanced traders, as can allow to calculate risk/rewards on individual
 * trades as opposed to in general.
 */
public class Transaction {
    private final String ticker;
    private final double amount;
    private final Market market;
    private final LocalDate date;

    private final DailyData transactionDailyData;

    /**
     * Instantiates a new Transaction.
     * Ticker, amount and Market are needed to allow data to be accessible by other classes.
     *
     * @param ticker Company ticker.
     * @param amount Amount of stock being bought/sold.
     * @param market Market being traded in.
     */
    public Transaction(String ticker, double amount, Market market) {
        this.ticker = ticker;
        this.amount = amount;
        this.market = market;
        this.date = market.getCurrentDate();
        this.transactionDailyData = market.findLatestDailyData(ticker);
    }

    /**
     * Gets amount of stock involved in transaction.
     *
     * @return Amount transacted.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Gets company involved in transaction.
     *
     * @return Company stock was bought/sold from.
     */
    public Company getCompany() {
        return market.findCompany(ticker);
    }

    /**
     * Get DailyData for the date of the transaction.
     *
     * @return DailyData of when transaction took place.
     */
    public DailyData getDailyData() {
        return transactionDailyData;
    }

    /**
     * Get transaction date.
     *
     * @return Date of when transaction took place
     */
    public LocalDate getDate() {
        return this.date;
    }
}
