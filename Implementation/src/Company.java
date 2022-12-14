import com.google.gson.Gson;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * The Company object. Represents a company that is able to be traded on the Market.
 * Once data is imported/processed and data structure is created, mainly used to facilitate data access across objects.
 */
public class Company {
    private final String ticker;
    private final Hashtable<LocalDate, DailyData> dailyData;
    private DailyData highestHigh;
    private DailyData lowestLow;
    private DailyData highestClose;
    private DailyData lowestClose;

    /**
     * Instantiates a new Company. Requires a .csv file in format as per NASDAQ/YahooFinance (and potentially others).
     * First line is column titles, further layout is as shown below:
     * <p>
     * Date,Close/Last,Volume,Open,High,Low
     *
     * @param historyCSV Path to .csv file in specified format
     * @param ticker     Company ticker
     */
    public Company(String historyCSV, String ticker) {
        this.ticker = ticker;
        List<String[]> unprocessedData = readCSV(historyCSV);
        assert unprocessedData != null;
        this.dailyData = createDailyData(unprocessedData);
    }

    /**
     * Instantiates a new Company. No .csv file is required as DailyData expected to be added afterwards
     *
     * @param ticker     Company ticker
     */
    public Company(String ticker) {
        this.ticker = ticker;
        this.dailyData = new Hashtable<LocalDate, DailyData>();
    }

    /**
     * Initial step in reading the CSV file, this opens the file and dumps its contents into a list of String arrays.
     * Expected format is as per NASDAQ/YahooFinance (and potentially others). First line is column titles.
     * Further layout is as shown below:
     * <p>
     * Date,Close/Last,Volume,Open,High,Low
     *
     * @param file File data is being read from.
     * @return Complete String[] of all data available in the file.
     */
    private static List<String[]> readCSV(String file) {
        try {
            // Create an object of file reader
            // class with CSV file as a parameter.
            FileReader filereader = new FileReader(file);

            // create csvReader object and skip first Line
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withSkipLines(1)
                    .build();
            List<String[]> dataCSV = csvReader.readAll();
            return dataCSV;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Finds all DailyData objects within the startDate and endDate, and returns them as an ArrayList
     *
     * @param startDate Starting date of search.
     * @param endDate   Ending date of search.
     * @return ArrayList containing all DailyData between the dates.
     */
    public ArrayList<DailyData> getDailyDataBetweenDates(LocalDate startDate, LocalDate endDate) {
        ArrayList<DailyData> list = new ArrayList<>();
        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            list.add(this.getDailyData(date));
        }
        return list;
    }

    /**
     * Finds all DailyData objects within the startDate and endDate, and returns them as a Json String.
     *
     * @param startDate Starting date of search.
     * @param endDate   Ending date of search.
     * @return Json String containing an ArrayList with all DailyData between the dates.
     */
    public String getDailyDataBetweenDatesJson(LocalDate startDate, LocalDate endDate) {
        ArrayList<DailyData> list = new ArrayList<>();
        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            list.add(this.getDailyData(date));
        }
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    /**
     * Creates DailyData data structure, reads the String array list line by line.
     * Extracts data to create the objects and stores in a Hashtable for easy/efficient lookups.
     *
     * @param data List of String arrays, within the expected format defined in readCSV().
     * @return Completed Hashtable of dailyData objects, indexed by LocalDate.
     */
    public Hashtable<LocalDate, DailyData> createDailyData(List<String[]> data) {
        Hashtable<LocalDate, DailyData> dataHashtable = new Hashtable<>();
        for (String[] row : data) {
            char[] currentDate = row[0].toCharArray();
            int currentYear = Integer.parseInt(new String(currentDate, 6, 4));
            int currentMonth = Integer.parseInt(new String(currentDate, 3, 2));
            int currentDay = Integer.parseInt(new String(currentDate, 0, 2));
            LocalDate currentLocalDate = LocalDate.of(currentYear, currentDay, currentMonth);
            double currentVolume = Double.parseDouble(row[2]);
            double currentStartValue = Double.parseDouble(row[3].replace("$", ""));
            double currentEndValue = Double.parseDouble(row[1].replace("$", ""));
            double currentHighestValue = Double.parseDouble(row[4].replace("$", ""));
            double currentLowestValue = Double.parseDouble(row[5].replace("$", ""));
            DailyData currentDailyData = new DailyData(this.ticker, currentLocalDate, currentVolume, currentStartValue, currentEndValue, currentHighestValue, currentLowestValue);
            dataHashtable.put(currentLocalDate, currentDailyData);
            AnalyseDailyData(currentDailyData);
        }
        return dataHashtable;
    }

    /**
     * Analyse the dailyData and computes overall Highs/Lows.
     * Modifies the dailyData directly, so no returns are expected.
     *
     * @param data dailyData object to be processed.
     */
    public void AnalyseDailyData(DailyData data) {
        if (this.highestHigh == null) {
            this.highestHigh = data;
        } else {
            if (data.getHighestValue() > this.highestHigh.getHighestValue()) {
                this.highestHigh = data;
            }
        }

        if (this.highestClose == null) {
            this.highestClose = data;
        } else {
            if (data.getEndValue() > this.highestClose.getEndValue()) {
                this.highestClose = data;
            }
        }

        if (this.lowestLow == null) {
            this.lowestLow = data;
        } else {
            if (data.getLowestValue() < this.lowestLow.getLowestValue()) {
                this.lowestLow = data;
            }
        }

        if (this.lowestClose == null) {
            this.lowestClose = data;
        } else {
            if (data.getEndValue() < this.lowestClose.getEndValue()) {
                this.lowestClose = data;
            }
        }
    }

    /**
     * Finds and returns dailyData object for the date provided.
     *
     * @param date Date being asked for.
     * @return dailyData object for that date.
     */
    public DailyData getDailyData(LocalDate date) {
        return dailyData.get(date);
    }

    /**
     * Basic toString() override that outputs this companies highest/lowest ever values and closing values.
     *
     * @return String containing highest/lowest ever values and closing values.
     */
    @Override
    public String toString() {
        return "Ticker: " + this.ticker
                + "\nHighest Value: " + highestHigh.getHighestValue() + " (" + highestHigh.getDate()
                + ")\nLowest Value: " + lowestLow.getLowestValue() + " (" + lowestLow.getDate()
                + ")\nHighest Closing Value: " + highestClose.getEndValue() + " (" + highestClose.getDate()
                + ")\nLowest Closing Value: " + lowestClose.getEndValue() + " (" + lowestClose.getDate() + ")";
    }

    /**
     * Converts this Company object to a JSON string.
     *
     * @return the JSON string representation of this Company object.
     */
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public void addDailyData(DailyData dailyData) {
        this.dailyData.put(dailyData.getDate(), dailyData);
    }
}
