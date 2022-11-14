import java.time.LocalDate;

public class Transaction {
    private String ticker;
    private double amount;
    private Market market;
    private LocalDate date;

    private DailyData transactionDailyData;

    public Transaction (String ticker, double amount, Market market){
        this.ticker = ticker;
        this.amount = amount;
        this.market = market;
        this.date = market.getCurrentDate();
        this.transactionDailyData = market.findLatestDailyData(ticker);
    }

    public double getAmount() {
        return amount;
    }

    public Company getCompany() {
        return market.findCompany(ticker);
    }

    public DailyData getDailyData(){
        return transactionDailyData;
    }

    public LocalDate getDate(){
        return this.date;
    }
}
