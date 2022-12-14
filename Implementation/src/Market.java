import com.google.gson.Gson;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * The Market object. Responsible for storing all companies and enabling actions related to finding company data
 * Allows for market to be stopped/started for Traders, as well as allows for time manipulation of the Market.
 */
public class Market {
    private final Hashtable<String, Company> companies;
    private final LocalDate startDate;
    private LocalDate currentDate;
    private boolean active = false;

    private ArrayList<Trader> controlledTraders = new ArrayList<>();

    /**
     * Instantiates a new Market. Trading is disabled by default and must be started using start() or toggleActive().
     *
     * @param companies Hashtable of all companies part of the market.
     * @param startDate Date from which trading will begin.
     */
    public Market(Hashtable<String, Company> companies, LocalDate startDate) {
        this.companies = companies;
        this.startDate = startDate;
        this.currentDate = LocalDate.of(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth());
    }

    /**
     * Start the Market and allow trading.
     */
    public void start() {
        active = true;
    }

    /**
     * Gets current list of traders being controlled by the market.
     *
     * @return Populated list of traders.
     */
    public ArrayList<Trader> getControlledTraders(){
        return this.controlledTraders;
    }

    /**
     * Adds a trader to the list of controlledTraders, which allow for trading to be automated using each trader's
     * run() method.
     *
     * @param trader Trader to be controlled.
     */
    public void addTrader(Trader trader){
        if (!controlledTraders.contains(trader)){
            controlledTraders.add(trader);
        }
    }

    /**
     * Stop the Market and stop trading.
     */
    public void stop() {
        active = false;
    }

    /**
     * Reset Market back to initial startDate.
     */
    public void reset() {
        this.currentDate = LocalDate.of(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth());
    }

    /**
     * Gets all companies.
     *
     * @return Hashtable indexed by company ticker.
     */
    public Hashtable<String, Company> getCompanies() {
        return companies;
    }

    /**
     * Find company.
     *
     * @param ticker Company ticker.
     * @return Company object corresponding with ticker.
     */
    public Company findCompany(String ticker) {
        return companies.getOrDefault(ticker, null);
    }

    /**
     * Find latest DailyData for a company.
     *
     * @param ticker Company ticker.
     * @return Today's DailyData.
     */
    public DailyData findLatestDailyData(String ticker) {
        return findCompany(ticker).getDailyData(this.currentDate);
    }

    /**
     * Advance Market by a day.
     */
    public void nextDay() {
        this.currentDate = this.currentDate.plusDays(1);
    }

    /**
     * Advance Market by a set amount of days.
     *
     * @param days Days to advance Market.
     */
    public void addDays(int days) {
        this.currentDate = this.currentDate.plusDays(days);
    }

    /**
     * Whether the Market is currently active
     *
     * @return True if Market is active.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Toggle Market state between active/inactive.
     */
    public void toggleActive() {
        this.active = !this.active;
    }

    /**
     * Gets current Market date.
     *
     * @return Current date.
     */
    public LocalDate getCurrentDate() {
        return currentDate;
    }

    /**
     * Advanced toString() override that outputs information in a human-readable format.
     *
     * @return String containing all companies current stock price, and/or market status.
     */
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder("Date: " + this.currentDate.toString() + "\n");
        Enumeration<String> e = this.companies.keys();
        if (this.currentDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                || this.currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            out.append("    ").append("Marked Closed (Weekend)");
        } else {
            while (e.hasMoreElements()) {
                String key = e.nextElement();
                out.append("    ").append(this.companies.get(key).getDailyData(this.currentDate)).append("\n");
            }
        }
        return out.toString();
    }

    /**
     * Converts this Market object to a JSON string.
     *
     * @return the JSON string representation of this Market object.
     */
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
