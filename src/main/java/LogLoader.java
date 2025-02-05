
import org.json.JSONArray;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LogLoader {

    public static JSONArray loadLogs(String filePath) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            return new JSONArray(content);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading JSON file: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return new JSONArray();
        }
    }
}
