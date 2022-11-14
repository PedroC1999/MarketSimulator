import java.sql.Array;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;

public class Trader {
    private double startFunds;
    private double currentFunds;
    private Market market;
    private Hashtable<String, ArrayList<Transaction>> portfolio = new Hashtable<>();

    public Trader(Market market, double startFunds){
        this.startFunds = startFunds;
        this.currentFunds = startFunds;
        this.market = market;
    }

    public double stocksOwned(String ticker){
        if (!portfolio.containsKey(ticker)){
            return 0;
        } else {
            double stockTotal = 0;

            ArrayList<Transaction> currentCompany = this.portfolio.get(ticker);
            for (Transaction trans:currentCompany) {
                stockTotal = stockTotal + trans.getAmount();
            }
            return stockTotal;
        }
    }

    public ArrayList<Transaction> getTransactions(String company){
        return portfolio.get(company);
    }

    public void buy(Market market, String ticker, double amount) throws InsufficientFundsException {
        double costOfSale = market.findLatestDailyData(ticker).getEndValue() * amount;
        /*
            if sufficent funds, create transaction
            if Portfolio includes previously bought company stocks, add to linked list, else, init linked list
         */
        if (costOfSale <= currentFunds){
            Transaction latestTransaction = new Transaction(ticker, amount, market);
            this.currentFunds = this.currentFunds - costOfSale;
            if(portfolio.containsKey(ticker)){
                portfolio.get(ticker).add(latestTransaction);
            } else {
                ArrayList<Transaction> currentTickerTransactions = new ArrayList<>();
                currentTickerTransactions.add(latestTransaction);
                portfolio.put(ticker, currentTickerTransactions);
            }
        } else {
            throw new InsufficientFundsException("Trader requires " + costOfSale + " to complete transaction, but only has " + currentFunds);
        }
    }

    public void sell(Market market, String ticker, double amount) throws InsufficientResourcesException {
        double costOfSale = market.findLatestDailyData(ticker).getEndValue() * amount;
        if (portfolio.containsKey(ticker)){
            //check if enough stocks in resource
            if (stocksOwned(ticker) != 0){
                Transaction latestTransaction = new Transaction(ticker, amount*-1, market);
                this.currentFunds = this.currentFunds + costOfSale;
                if(portfolio.containsKey(ticker)){
                    portfolio.get(ticker).add(latestTransaction);
                } else {
                    ArrayList<Transaction> currentTickerTransactions = new ArrayList<>();
                    currentTickerTransactions.add(latestTransaction);
                    portfolio.put(ticker, currentTickerTransactions);
                }
                if (stocksOwned(ticker) == 0){
                    this.portfolio.remove(ticker);
                }
            }
        } else {
            throw new InsufficientResourcesException("Agent does not hold any " + ticker + " stocks to sell");
        }
    }

    @Override
    public String toString() {
        if(portfolio.isEmpty()){
            return "Empty portfolio";
        } else {
            StringBuilder out = new StringBuilder("Current Funds: ").append(this.currentFunds);
            out.append("\n").append("Initial Funds: ").append(this.startFunds);
            Enumeration<String> e = this.portfolio.keys();
            double assetValue = 0;
            while(e.hasMoreElements()) {
                String key = e.nextElement();

                double stockTotal = 0;

                ArrayList<Transaction> currentCompany = this.portfolio.get(key);
                for (Transaction trans:currentCompany) {
                    stockTotal = stockTotal + trans.getAmount();
                }
                double latestDailyValue = market.findLatestDailyData(key).getEndValue();
                double stockValue = latestDailyValue* stockTotal;
                assetValue = assetValue + stockValue;
                out.append("\n    ").append(key).append(": ").append(stockTotal).append(" @ ").append(latestDailyValue).append( " totalling ").append(stockValue);

            }
            out.append("\n   \n").append("    Stock Worth: ").append(assetValue);
            out.append("\n    ").append("Calculated Gain/Loss: ").append((assetValue+currentFunds)-startFunds);
            return out.toString();
        }
    }
}
