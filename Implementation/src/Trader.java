import com.google.gson.Gson;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * The Trader object,
 * Represents a real agent or trader within the active market.
 * Allows the expected actions of buying, selling and querying owned stocks.
 * Many helper methods that make the code more readable and usable.
 */
public class Trader {
    private final double startFunds;
    private final Market market;
    private final Hashtable<String, Asset> portfolio = new Hashtable<>();

    /**
     * Gets the current amount of funds in the account.
     *
     * @return Amount of funds currently Available.
     */
    public double getCurrentFunds() {
        return currentFunds;
    }

    private double currentFunds;

    /**
     * Instantiates a new Trader.
     *
     * @param market     The Market being traded in.
     * @param startFunds Initial funds available for trading.
     */
    public Trader(Market market, double startFunds) {
        this.startFunds = startFunds;
        this.currentFunds = startFunds;
        this.market = market;
    }

    /**
     * Queries the portfolio and returns how many stocks are owned for that company.
     *
     * @param ticker Company ticker.
     * @return Amount of stock currently owned.
     */
    public double stocksOwned(String ticker) {
        if (!portfolio.containsKey(ticker)) {
            return 0;
        } else {
            return portfolio.get(ticker).getQuantityOwned();
        }
    }

    /**
     * Add additional funds available to the trader.
     *
     * @param funds Amount of funding being added.
     */
    public void addFunds(double funds) {
        this.currentFunds += funds;
    }

    /**
     * Remove funds available to the trader.
     *
     * @param funds Amount of funding being removed.
     */
    public void removeFunds(double funds) {
        this.currentFunds -= funds;
    }

    /**
     * Run method, used by the controller classes to trigger a market movement when required.
     * Intentionally meant to be overridden, to allow for advanced users to simulate more complex market behaviours.
     */
    public void Run() {
    }

    /**
     * Performs a buying interaction on the current market,
     * Initially checks if the trader has enough funds to complete the transaction, before subtracting funds and
     * recording the buy using the Asset object format.
     *
     * @param ticker Company ticker.
     * @param amount Amount of stock being bought.
     * @throws InsufficientFundsException Thrown when trader has insufficient funds to complete transaction.
     */
    public void buy(String ticker, double amount) throws InsufficientFundsException {
        double costOfSale = market.findLatestDailyData(ticker).getEndValue() * amount;
        if (costOfSale <= currentFunds) {
            this.currentFunds = this.currentFunds - costOfSale;
            if (portfolio.containsKey(ticker)) {
                portfolio.get(ticker).buy(market, ticker, amount);
            } else {
                portfolio.put(ticker, new Asset());
                portfolio.get(ticker).buy(market, ticker, amount);
            }
        } else {
            throw new InsufficientFundsException("Trader requires " + costOfSale + " to complete transaction, but only has " + currentFunds);
        }
    }

    /**
     * Performs a selling interaction on the current market,
     * Initially checks if the trader has enough stocks to complete the transaction, before adding funds and
     * recording the sale using the Asset object format.
     *
     * @param ticker Company ticker.
     * @param amount Amount of stock being sold.
     * @throws InsufficientResourcesException Thrown when trader has insufficient stocks to complete transaction.
     */
    public void sell(String ticker, double amount) throws InsufficientResourcesException {
        double costOfSale = market.findLatestDailyData(ticker).getEndValue() * amount;
        if (canSell(ticker, amount)) {
            this.currentFunds = this.currentFunds + costOfSale;
            if (portfolio.containsKey(ticker)) {
                portfolio.get(ticker).sell(market, ticker, amount);
            } else {
                portfolio.put(ticker, new Asset());
                portfolio.get(ticker).sell(market, ticker, amount);
            }
            if (this.portfolio.get(ticker).getQuantityOwned() == 0) {
                this.portfolio.remove(ticker);
            }
        } else {
            throw new InsufficientResourcesException("Agent does not hold enough " + ticker + " stocks to sell");
        }
    }

    /**
     * Performs a mass selling interaction on the current market,
     * Iterates through the portfolio and triggers a sell() on each, to ensure all checks are still carried out.
     */
    public void sellAll() {
        Enumeration<String> e = this.portfolio.keys();
        while (e.hasMoreElements()) {
            String key = e.nextElement();
            double amountOwned = this.portfolio.get(key).getQuantityOwned();
            if (canSell(key, amountOwned)) {
                this.sell(key, amountOwned);
            }
        }
    }

    /**
     * Helper method to check if trader owns sufficient stock to sell.
     *
     * @param ticker the ticker
     * @param amount the amount
     * @return the boolean
     */
    public boolean canSell(String ticker, double amount) {
        if (portfolio.containsKey(ticker)) {
            if (stocksOwned(ticker) != 0) {
                return stocksOwned(ticker) >= amount;
            }
        } else {
            return false;
        }
        return false;
    }

    /**
     * Advanced toString() override that outputs all information in a human-readable format.
     *
     * @return String containing current funds, initial funds, current date, as well as current portfolio status
     */
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder("Current Funds: ").append(this.currentFunds);
        out.append("\n").append("Initial Funds: ").append(this.startFunds);
        out.append("\n").append("Current Date: ").append(this.market.getCurrentDate());
        if (!portfolio.isEmpty()) {
            Enumeration<String> e = this.portfolio.keys();
            double assetValue = 0;
            while (e.hasMoreElements()) {
                String key = e.nextElement();
                double stockQuantity = this.portfolio.get(key).getQuantityOwned();
                double latestDailyValue = market.findLatestDailyData(key).getEndValue();
                double stockValue = latestDailyValue * stockQuantity;
                assetValue = assetValue + stockValue;
                out.append("\n    ").append(key).append(": ").append(stockQuantity).append(" @ ").append(latestDailyValue).append(" totalling ").append(stockValue);
            }
            out.append("\n").append("Stock Worth: ").append(assetValue);
            out.append("\n").append("Calculated Gain/Loss: ").append((assetValue + currentFunds) - startFunds);
            out.append("\n");
        } else{
            out.append("\n    ").append("Empty Portfolio");
        }
        return out.toString();
    }
    /**
     * Converts this Trader object to a JSON string.
     *
     * @return the JSON string representation of this Trader object.
     */
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
