package fi.tuni.csgr.managers.userdata;

import com.google.gson.Gson;
import fi.tuni.csgr.utils.SelectionData;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

public class UserDataManager {

    private static Path userDataLocation;
    private static final Gson gson = new Gson();

    public UserDataManager(String dirPath) {
        userDataLocation = Path.of(dirPath).resolve("user_data");
    }

    public void saveSelection(String source, SelectionData selectionData) throws ErrorWritingUserDataException {
        writeToFile(source, gson.toJson(selectionData));
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
