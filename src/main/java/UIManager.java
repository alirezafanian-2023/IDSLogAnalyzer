
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class UIManager {
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JList<String> logList;
    private JButton analyzeButton;
    private JTextPane outputArea;
    private JLabel loadingLabel;

    private JSONArray logsArray;

    public UIManager() {

        logsArray = LogLoader.loadLogs("./logs.json");

        createLeftPanel();
        createRightPanel();
    }

    private void createLeftPanel() {
        leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("Available Logs:"), BorderLayout.NORTH);

        Vector<String> titles = getLogTitles();
        logList = new JList<>(titles);
        logList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScrollPane = new JScrollPane(logList);
        listScrollPane.setPreferredSize(new Dimension(450, 400));
        leftPanel.add(listScrollPane, BorderLayout.CENTER);

        analyzeButton = new JButton("Analyze Selected Log");
        analyzeButton.setEnabled(false);
        leftPanel.add(analyzeButton, BorderLayout.SOUTH);

        logList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                analyzeButton.setEnabled(!logList.isSelectionEmpty());
            }
        });

        analyzeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = logList.getSelectedIndex();
                if (selectedIndex != -1) {
                    JSONObject selectedLog = logsArray.getJSONObject(selectedIndex);
                    OllamaAnalyzer.sendToOllama(selectedLog, outputArea, loadingLabel);
                }
            }
        });
    }

    private void createRightPanel() {
        rightPanel = new JPanel(new BorderLayout());
        outputArea = new JTextPane();
        outputArea.setContentType("text/html");
        outputArea.setEditable(false);
        JScrollPane outputScrollPane = new JScrollPane(outputArea);

        loadingLabel = new JLabel(new ImageIcon("./loading.gif"));
        loadingLabel.setVisible(false);

        rightPanel.add(outputScrollPane, BorderLayout.CENTER);
        rightPanel.add(loadingLabel, BorderLayout.SOUTH);
    }

    private Vector<String> getLogTitles() {
        Vector<String> titles = new Vector<>();
        for (int i = 0; i < logsArray.length(); i++) {
            JSONObject log = logsArray.getJSONObject(i);
            JSONObject alert = log.getJSONObject("alert");
            String eventType = log.optString("event_type", "N/A");
            String signature = alert.optString("signature", "N/A");
            titles.add(i + ": " + eventType + " - " + signature);
        }
        return titles;
    }

    public JPanel getLeftPanel() {
        return leftPanel;
    }

    public JPanel getRightPanel() {
        return rightPanel;
    }
}
