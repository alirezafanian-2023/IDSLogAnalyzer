

import javax.swing.JFrame;
import java.awt.BorderLayout;

public class IDSLogAnalyzer {
    public static void main(String[] args) {

        JFrame frame = new JFrame("IDS Log Analyzer - Multi File Version");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        UIManager uiManager = new UIManager();

        frame.add(uiManager.getLeftPanel(), BorderLayout.WEST);
        frame.add(uiManager.getRightPanel(), BorderLayout.CENTER);

        frame.setVisible(true);
    }
}
