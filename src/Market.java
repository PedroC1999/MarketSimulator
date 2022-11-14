import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class Market{
    private Hashtable<String, Company> companies;
    private final LocalDate startDate;
    private LocalDate currentDate;
    private boolean active = false;

    public Market(Hashtable<String, Company> companies, LocalDate startDate) {
        this.companies = companies;
        this.startDate = startDate;
        this.currentDate = LocalDate.of(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth());
    }

    public void start(){
        active = true;
    }

    public void stop(){
        active = false;
    }

    public void reset(){
        this.currentDate = LocalDate.of(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth());
    }

    public Hashtable<String, Company> getCompanies() {
        return companies;
    }

    public Company findCompany(String ticker){
        return companies.getOrDefault(ticker, null);
    }

    public DailyData findLatestDailyData(String ticker){
        return findCompany(ticker).getDailyData(this.currentDate);
    }

    public void nextDay(){
        this.currentDate =  this.currentDate.plusDays(1);
    }

    public void addDays(int days){
        this.currentDate =  this.currentDate.plusDays(days);
    }

    public boolean isActive() {
        return active;
    }

    public void toggleActive(){
        this.active = !this.active;
    }

    @Override
    public String toString() {

        StringBuilder out = new StringBuilder(new String("Date: " + this.currentDate.toString() + "\n"));

        Enumeration<String> e = this.companies.keys();

        if (this.currentDate.getDayOfWeek().equals(DayOfWeek.SATURDAY) || this.currentDate.getDayOfWeek() == DayOfWeek.SUNDAY ){
            out.append("    ").append("Marked Closed (Weekend)");
        } else {
            while (e.hasMoreElements()) {
                String key = e.nextElement();
                out.append("    ").append(this.companies.get(key).getDailyData(this.currentDate)).append("\n");
            }
        }
        return  out.toString();
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }
}
