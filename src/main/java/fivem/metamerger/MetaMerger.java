package fivem.metamerger;

import java.util.Properties;

import javax.swing.*;

import fivem.metamerger.gui.GUI;

public class MetaMerger {

    public static void main(String[] args) {
        GUI gui = new GUI();
        String version = "Unknown";
        try {
            Properties properties = new Properties();
            properties.load(ClassLoader.getSystemClassLoader().getResourceAsStream("version.properties"));
            version = properties.getProperty("version");
        } catch (Exception e) {
            e.printStackTrace();
        }
        gui.log(GUI.LogLevel.INFO, "## FiveM MetaMerger v" + version + " ##");
        gui.log(GUI.LogLevel.INFO, "Please add the .meta files you wish to merge...");
    }
}
