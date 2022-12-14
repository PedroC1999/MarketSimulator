import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class DailyDataTest {

    @Test
    void testGetDate() {
        LocalDate date = LocalDate.of(2022, 12, 11);
        DailyData dailyData = new DailyData("TEST", date, 0.0, 0.0, 0.0, 0.0, 0.0);
        assertEquals(date, dailyData.getDate());
    }

    @Test
    void testGetVolume() {
        double volume = 10.5;
        DailyData dailyData = new DailyData("TEST", LocalDate.of(2022, 12, 11), volume, 0.0, 0.0, 0.0, 0.0);
        assertEquals(volume, dailyData.getVolume());
    }

    @Test
    void testGetStartValue() {
        double startValue = 100.0;
        DailyData dailyData = new DailyData("TEST", LocalDate.of(2022, 12, 11), 0.0, startValue, 0.0, 0.0, 0.0);
        assertEquals(startValue, dailyData.getStartValue());
    }

    @Test
    void testGetEndValue() {
        double endValue = 100.0;
        DailyData dailyData = new DailyData("TEST", LocalDate.of(2022, 12, 11), 0.0, 0.0, endValue, 0.0, 0.0);
        assertEquals(endValue, dailyData.getEndValue());
    }

    @Test
    void testGetHighestValue() {
        double highestValue = 100.0;
        DailyData dailyData = new DailyData("TEST", LocalDate.of(2022, 12, 11), 0.0, 0.0, 0.0, highestValue, 0.0);
        assertEquals(highestValue, dailyData.getHighestValue());
    }

    @Test
    void testGetLowestValue() {
        double lowestValue = 100.0;
        DailyData dailyData = new DailyData("TEST", LocalDate.of(2022, 12, 11), 0.0, 0.0, 0.0, 0.0, lowestValue);
        assertEquals(lowestValue, dailyData.getLowestValue());
    }

    public void testToString() {
        String ticker = "TEST";
        LocalDate date = LocalDate.of(2022, 12, 11);
        double volume = 100.0;
        double startValue = 10.0;
        double endValue = 11.0;
        double highestValue = 12.0;
        double lowestValue = 9.0;
        DailyData dailyData = new DailyData(ticker, date, volume, startValue, endValue, highestValue, lowestValue);

        String expected = ticker + "{" +
                "date='" + date + '\'' +
                ", volume=" + volume +
                ", startValue=" + startValue +
                ", endValue=" + endValue +
                ", highestValue=" + highestValue +
                ", lowestValue=" + lowestValue +
                '}';
        String actual = dailyData.toString();

        assertEquals(expected, actual);
    }
}