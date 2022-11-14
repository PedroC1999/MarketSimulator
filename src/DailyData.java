import java.time.LocalDate;

public class DailyData {
    private final String ticker;
    private final LocalDate date;
    private final double volume;
    private final double startValue;
    private final double endValue;
    private final double highestValue;
    private final double lowestValue;

    public LocalDate getDate() {
        return date;
    }

    public double getVolume() {
        return volume;
    }

    public double getStartValue() {
        return startValue;
    }

    public double getEndValue() {
        return endValue;
    }

    public double getHighestValue() {
        return highestValue;
    }

    public double getLowestValue() {
        return lowestValue;
    }

    public DailyData(String ticker, LocalDate date, double volume, double startValue, double endValue, double highestValue, double lowestValue) {
        this.ticker = ticker;
        this.date = date;
        this.volume = volume;
        this.startValue = startValue;
        this.endValue = endValue;
        this.highestValue = highestValue;
        this.lowestValue = lowestValue;
    }

    @Override
    public String toString() {
        return ticker + "{" +
                "date='" + date + '\'' +
                ", volume=" + volume +
                ", startValue=" + startValue +
                ", endValue=" + endValue +
                ", highestValue=" + highestValue +
                ", lowestValue=" + lowestValue +
                '}';
    }
}
