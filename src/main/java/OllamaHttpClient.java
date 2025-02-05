

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class OllamaHttpClient {

    public static String sendHttpRequest(String url, String json) throws IOException {
        URL apiUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = json.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        StringBuilder responseBuilder = new StringBuilder();
        try (InputStream is = connection.getInputStream();
             Scanner scanner = new Scanner(is, "utf-8")) {
            while (scanner.hasNext()) {
                responseBuilder.append(scanner.nextLine()).append("\n");
            }
        }

        return responseBuilder.toString();
    }
}
