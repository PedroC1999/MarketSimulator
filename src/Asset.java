import java.util.ArrayList;

/**
 * The Asset object,
 * Used by the Trader class to store information on the transactions carried out by an agent on a company.
 * One asset object per Trader/Company relationship.
 * Allows for more advanced calculations/interactions to be carried out within this class.
 */
public class Asset {
    private double quantityOwned = 0;
    private final ArrayList<Transaction> transactions = new ArrayList<>();

    /**
     * Get quantity of stocks owned.
     *
     * @return Amount of stocks owned by agent within a company/market.
     */
    public double getQuantityOwned(){
        return this.quantityOwned;
    }

    /**
     *  Records a buying transaction for the current trader,
     * Requires the current market, the company ticker and the amount being bought to ensure data is accessible within other class.
     *
     * @param market Market being traded in.
     * @param ticker Company ticker.
     * @param amount Amount of stock being bought.
     */
    public void buy(Market market, String ticker, double amount){
        Transaction latestTransaction = new Transaction(ticker, amount, market);
        this.quantityOwned += amount;
        transactions.add(latestTransaction);
    }

    /**
     * Records a selling transaction for the current trader,
     * Requires the current market, the company ticker and the amount being sold to ensure data is accessible within other class.
     *
     * @param market Market being traded in.
     * @param ticker Company ticker.
     * @param amount Amount of stock being sell.
     */
    public void sell(Market market, String ticker, double amount){
        Transaction latestTransaction = new Transaction(ticker, amount, market);
        this.quantityOwned -= amount;
        transactions.add(latestTransaction);
    }
}
