package fivem.metamerger.gui;

import java.awt.*;

import javax.swing.*;

public class GUI extends JFrame {

    private OutputLogPanel logPanel;

    public GUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { }

        setTitle("FiveM MetaMerger");
        setSize(500, 300);
        try {
            setIconImage(new ImageIcon(ClassLoader.getSystemClassLoader().getResource("icon.png")).getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        getContentPane().add(new InputPanel(this), BorderLayout.SOUTH);
        logPanel = new OutputLogPanel(this);
        getContentPane().add(logPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Logs a message to the output display.
     * @param logLevel The level of the message to log.
     * @param log The message to display.
     */
    public void log(LogLevel logLevel, String log) {
        if (!logLevel.equals(LogLevel.INFO)) {
            logPanel.log(new StringBuilder("[").append(logLevel.name()).append("] ").append(log).append("\n").toString());
        } else {
            logPanel.log(new StringBuilder(log).append("\n").toString());
        }
    }

    public enum LogLevel {
        INFO,
        ERROR
    }

}