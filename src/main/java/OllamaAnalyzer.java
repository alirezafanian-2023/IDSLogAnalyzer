

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class OllamaAnalyzer {

    private static final String OLLAMA_API_URL = "http://176.101.48.153:20002/api/generate";

    public static void sendToOllama(JSONObject jsonData, JTextPane outputArea, JLabel loadingLabel) {

        outputArea.setText("<html><b style='color:blue; font-size:16px;'>Sending to IDS Analyzer ...</b></html>");
        loadingLabel.setVisible(true);

        new Thread(() -> {
            try {
                JSONObject requestJson = new JSONObject();
                requestJson.put("model", "hf.co/dattaraj/security-attacks-MITRE");
                requestJson.put("prompt", jsonData.toString());
                requestJson.put("stream", false);

                String response = OllamaHttpClient.sendHttpRequest(OLLAMA_API_URL, requestJson.toString());
                JSONObject jsonResponse = new JSONObject(response);


                String timestamp = jsonData.optString("timestamp", "N/A");
                String sourceIp = jsonData.optString("source_ip", "N/A");
                String destinationIp = jsonData.optString("destination_ip", "N/A");

                SwingUtilities.invokeLater(() -> {
                    loadingLabel.setVisible(false);
                    outputArea.setText(
                            "<html>" +
                                    "<h2 style='color:red; font-size:16px;'>Alert Analysis:</h2>" +
                                    "<b style='color:blue; font-size:14px;'>Security Analysis:</b> " +
                                    "<span style='font-size:14px;'>" + jsonResponse.getString("response") + "</span><br>" +

                                    "<b style='color:blue; font-size:14px;'>Processing Status:</b> " +
                                    "<span style='font-size:14px;'>" + jsonResponse.getString("done_reason") + "</span><br>" +

                                    "<b style='color:blue; font-size:14px;'>Tokens Processed:</b> " +
                                    "<span style='font-size:14px;'>" + jsonResponse.getInt("prompt_eval_count") + "</span><br>" +

                                    "<b style='color:blue; font-size:14px;'>Total Processing Time (ms):</b> " +
                                    "<span style='font-size:14px;'>" + (jsonResponse.optLong("total_duration", 0) / 1_000_000) + " ms</span><br><br>" +

                                    "<b style='color:green; font-size:14px;'>Timestamp:</b> " +
                                    "<span style='font-size:14px;'>" + timestamp + "</span><br>" +

                                    "<b style='color:green; font-size:14px;'>Source IP:</b> " +
                                    "<span style='font-size:14px;'>" + sourceIp + "</span><br>" +

                                    "<b style='color:green; font-size:14px;'>Destination IP:</b> " +
                                    "<span style='font-size:14px;'>" + destinationIp + "</span>" +

                                    "</html>"                    );
                });
            } catch (IOException e) {
                SwingUtilities.invokeLater(() -> {
                    loadingLabel.setVisible(false);
                    outputArea.setText("<html><b style='color:red;'>Error sending request: " + e.getMessage() + "</b></html>");
                });
            }
        }).start();
    }
}
