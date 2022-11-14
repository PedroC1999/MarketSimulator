import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.javatuples.Pair;

import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Company {
    private final String ticker;
    private final Hashtable<LocalDate, DailyData> dailyData;

    private DailyData highestHigh;
    private DailyData lowestLow;
    private DailyData highestClose;
    private DailyData lowestClose;

    public Company(String historyCSV, String ticker) {
        this.ticker = ticker;
        List<String[]> unprocessedData = readCSV(historyCSV);
        assert unprocessedData != null;
        this.dailyData = createDailyData(unprocessedData);
    }

    public ArrayList<DailyData> getDailyDataBetweenDates(LocalDate startDate, LocalDate endDate){
        ArrayList<DailyData> list = new ArrayList<>();
        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            list.add(this.getDailyData(date));
        }
        return list;
    }

    public Hashtable<LocalDate, DailyData> createDailyData(List<String[]> data) {
        Hashtable<LocalDate, DailyData> dataHashtable = new Hashtable<>();
        for (String[] row : data) {
            char[] currentDate = row[0].toCharArray();
            int currentYear = Integer.parseInt(new String(currentDate, 6, 4 ));
            int currentMonth = Integer.parseInt(new String(currentDate, 3, 2 ));
            int currentDay = Integer.parseInt(new String(currentDate, 0, 2 ));
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

    public DailyData getDailyData(LocalDate date) {
        return dailyData.get(date);
    }

    @Override
    public String toString() {
        return "Ticker: " + this.ticker
                + "\nHighest Value: " + highestHigh.getHighestValue() + " (" + highestHigh.getDate()
                + ")\nLowest Value: " + lowestLow.getLowestValue() + " (" + lowestLow.getDate()
                + ")\nHighest Closing Value: " + highestClose.getEndValue() + " (" + highestClose.getDate()
                + ")\nLowest Closing Value: " + lowestClose.getEndValue() + " (" + lowestClose.getDate() + ")";

    }
}
