package fi.tuni.csgr.managers.userdata;

import com.google.gson.Gson;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class UserDataManager {

    private static Path userDataLocation;
    private static final Gson gson = new Gson();

    public static class SelectionData {
        String fromDate;
        String toDate;
        Map<String, ArrayList<String>> selectionArgs;

        public SelectionData(LocalDate fromDate, LocalDate toDate, Map<String, ArrayList<String>> searchData) {
            this.fromDate = String.valueOf(fromDate);
            this.toDate = String.valueOf(toDate);
            this.selectionArgs = searchData;
        }

        public LocalDate getFromDate() {
            return LocalDate.parse(fromDate);
        }

        public LocalDate getToDate() {
            return LocalDate.parse(toDate);
        }

        public Map<String, ArrayList<String>> getSelectionArgs() {
            return selectionArgs;
        }

        public void setFromDate(String fromDate) {
            this.fromDate = fromDate;
        }

        public void setToDate(String toDate) {
            this.toDate = toDate;
        }

        public void setSelectionArgs(Map<String, ArrayList<String>> selectionArgs) {
            this.selectionArgs = selectionArgs;
        }
    }


    public UserDataManager(String dirPath) {
        userDataLocation = Path.of(dirPath).resolve("user_data");
    }

    public void saveSelection(String source, SelectionData selectionData) throws ErrorWritingUserDataException {
        writeToFile(source, gson.toJson(selectionData));
        System.out.println(gson.toJson(selectionData));
        System.out.println(selectionData.selectionArgs);
    }

    public SelectionData readSelection(String source) throws ErrorReadingUserDataException, FileNotFoundException {
        return readFromFile(source);
    }

    private static SelectionData readFromFile(String source) throws FileNotFoundException, ErrorReadingUserDataException {
        Path userDataFilePath = userDataLocation.resolve(String.format("%s.json", source));
        if (!Files.exists(userDataFilePath)){
            System.out.println("file not found");
            throw new FileNotFoundException();
        }
        try {
            String content = Files.readAllLines(userDataFilePath).get(0);
            return gson.fromJson(content, SelectionData.class);
        } catch (IOException e) {
            System.out.println("reading");
            throw new ErrorReadingUserDataException();
        }
    }

    private static void writeToFile(String source, String selection) throws ErrorWritingUserDataException {

        if (! Files.exists(userDataLocation)){
            try {
                Files.createDirectory(userDataLocation);
            } catch (IOException e) {
                throw new ErrorWritingUserDataException();
            }
        }

        Path userDataFilePath = userDataLocation.resolve(String.format("%s.json", source));

        if (!Files.exists(userDataFilePath)){
            try {
                Files.createFile(userDataFilePath);
            } catch (IOException e) {
                System.out.println("creating failed");
                throw new ErrorWritingUserDataException();
            }
        }
        try {
            Files.write(userDataFilePath, Collections.singleton(selection));
        } catch (IOException e) {
            throw new ErrorWritingUserDataException();
        }
    }
}
