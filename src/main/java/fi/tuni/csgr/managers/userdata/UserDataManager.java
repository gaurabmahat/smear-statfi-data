package fi.tuni.csgr.managers.userdata;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class UserDataManager {

    private static String userDataFileLocation;
    private static final Gson gson = new Gson();

    public UserDataManager(String dirPath) {
        userDataFileLocation = Path.of(dirPath).resolve("gas-master-user-data.json").toString();
    }

    public static class Selection{

        private String fromDate;
        private String toDate;
        private ArrayList<String> gases;
        private ArrayList<String> stations;

        public Selection(LocalDate fromDate, LocalDate toDate, ArrayList<String> gases, ArrayList<String> stations) {
            this.fromDate = fromDate.toString();
            this.toDate = toDate.toString();
            this.gases = gases;
            this.stations = stations;
        }

        public LocalDate getFromDate() {
            return LocalDate.parse(fromDate);
        }

        public LocalDate getToDate() {
            return LocalDate.parse(toDate);
        }

        public ArrayList<String> getGases() {
            return gases;
        }

        public ArrayList<String> getStations() {
            return stations;
        }

        public void setFromDate(String fromDate) {
            this.fromDate = fromDate;
        }

        public void setToDate(String toDate) {
            this.toDate = toDate;
        }

        public void setGases(ArrayList<String> gases) {
            this.gases = gases;
        }

        public void setStations(ArrayList<String> stations) {
            this.stations = stations;
        }
    }

    public void saveSelection(LocalDate fromDate, LocalDate toDate, ArrayList<String> gases, ArrayList<String> stations) throws ErrorWritingUserDataException {
        Selection selection = new Selection(fromDate, toDate, gases, stations);
        writeToFile(gson.toJson(selection));
    }

    public Selection readSelection() throws ErrorReadingUserDataException, FileNotFoundException {
        return readFromFile();
    }

    private static Selection readFromFile() throws FileNotFoundException, ErrorReadingUserDataException {
        File userDataFile = new File(userDataFileLocation);
        if (!userDataFile.exists()){
            throw new FileNotFoundException();
        }
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(userDataFile));
            JsonReader jsonReader = new JsonReader(inputStreamReader);

            return gson.fromJson(jsonReader, Selection.class);
        } catch (FileNotFoundException | JsonIOException | JsonSyntaxException e) {
            throw new ErrorReadingUserDataException();
        }
    }

    private static void writeToFile(String selection) throws ErrorWritingUserDataException {
        File userDataFile = new File(userDataFileLocation);

        if (!userDataFile.exists()){
            try {
                userDataFile.createNewFile();
            } catch (IOException e) {
               throw new ErrorWritingUserDataException();
            }
        }
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(userDataFile)))){
            writer.write(selection);
        } catch (IOException e) {
            throw new ErrorWritingUserDataException();
        }
    }
}
