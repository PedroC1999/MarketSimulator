import java.time.LocalDate;
import com.google.gson.Gson;

/**
 * The DailyData object. Lowest level data storage in the structure.
 * Stores daily data for an individual company for a specific day.
 */
public class DailyData {
    private final String ticker;
    private final LocalDate date;
    private final double volume;
    private final double startValue;
    private final double endValue;
    private final double highestValue;
    private final double lowestValue;

    /**
     * Instantiates a new DailyData object. Requires all data it holds at time of creation as all data is accessible then.
     *
     * @param ticker       Company ticker.
     * @param date         Date of data.
     * @param volume       Volume available.
     * @param startValue   Start value.
     * @param endValue     End value.
     * @param highestValue Highest daily value.
     * @param lowestValue  Lowest daily value.
     */
    public DailyData(String ticker, LocalDate date, double volume, double startValue, double endValue, double highestValue, double lowestValue) {
        this.ticker = ticker;
        this.date = date;
        this.volume = volume;
        this.startValue = startValue;
        this.endValue = endValue;
        this.highestValue = highestValue;
        this.lowestValue = lowestValue;
    }

    /**
     * Gets date of data.
     *
     * @return the date.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Gets volume for day.
     *
     * @return the volume.
     */
    public double getVolume() {
        return volume;
    }

    /**
     * Gets start value for day.
     *
     * @return the start value.
     */
    public double getStartValue() {
        return startValue;
    }

    /**
     * Gets end value for day.
     *
     * @return the end value.
     */
    public double getEndValue() {
        return endValue;
    }

    /**
     * Gets the highest daily value.
     *
     * @return the highest value.
     */
    public double getHighestValue() {
        return highestValue;
    }

    /**
     * Gets the lowest daily value.
     *
     * @return the lowest value.
     */
    public double getLowestValue() {
        return lowestValue;
    }

    /**
     * Basic toString() override that outputs this dailyData's stored information.
     *
     * @return String date, start value, end value, highest value and lowest value.
     */
    @Override
    public String toString() {

        StringBuilder out = new StringBuilder();
        out.append(ticker).append(": ").append(" Date = ").append(date);
        out.append(", Start = ").append(startValue);
        out.append(", End = ").append(endValue);
        out.append(", Highest = ").append(highestValue);
        out.append(", Lowest = ").append(lowestValue);
        out.append(", Volume = ").append(volume);

        return out.toString();
    }

    /**
     * Converts this DailyData object to a JSON string.
     *
     * @return the JSON string representation of this DailyData object.
     */
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
