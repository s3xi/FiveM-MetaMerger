package fivem.metamerger.gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import javax.swing.text.StyledDocument;

public class OutputLogPanel extends JPanel {

    private JTextPane outputLog;

    public OutputLogPanel(GUI gui) {
        setLayout(new BorderLayout());

        outputLog = new JTextPane();
        outputLog.setEditable(false);
        DefaultCaret caret = (DefaultCaret) outputLog.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        JScrollPane outputScroll = new JScrollPane(outputLog, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        outputScroll.setAutoscrolls(true);
        outputScroll.setBorder(BorderFactory.createEmptyBorder());
        add(outputScroll);
    }

    public void log(String message) {
        try {
            StyledDocument doc = outputLog.getStyledDocument();
            doc.insertString(doc.getLength(), message, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
