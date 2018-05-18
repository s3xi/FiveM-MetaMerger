package fivem.metamerger.gui;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import fivem.metamerger.MetaFile;
import fivem.metamerger.MetaFileType;
import fivem.metamerger.merger.Merger;

public class InputPanel extends JPanel {

    private HashMap<MetaFileType, java.util.List<MetaFile>> metaFiles = new HashMap<>();
    private JFileChooser fileChooserMeta;

    public InputPanel(GUI gui) {
        fileChooserMeta = new JFileChooser();
        fileChooserMeta.setMultiSelectionEnabled(true);
        setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton buttonAddFile = new JButton("Add .meta File...");
        buttonAddFile.setToolTipText("<html><p style=\"padding-bottom:2px\">Add a <b>.meta</b> file to prepare for merging.</p></html>");
        buttonAddFile.setBorderPainted(false);
        buttonAddFile.setFocusPainted(false);
        buttonAddFile.addActionListener(e -> {
            fileChooserMeta.setFileFilter(new FileNameExtensionFilter("Meta Files", "meta"));
            int returnVal = fileChooserMeta.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                for (File file : fileChooserMeta.getSelectedFiles()) {
                    MetaFile metaFile = new MetaFile(file);
                    if (metaFile.getMetaFileType() != null) { //Is valid FiveM meta file.
                        List<MetaFile> metaFiles = new ArrayList<>();
                        if (this.metaFiles.containsKey(metaFile.getMetaFileType())) {
                            metaFiles = this.metaFiles.get(metaFile.getMetaFileType());
                        }
                        if (metaFiles.stream().anyMatch(metaFile1 -> metaFile.getFile().equals(metaFile1.getFile()))) {
                            gui.log(GUI.LogLevel.ERROR, "File already added");
                        } else {
                            metaFiles.add(metaFile);
                            this.metaFiles.put(metaFile.getMetaFileType(), metaFiles);
                            gui.log(GUI.LogLevel.INFO, "Added file '" + file.getParent() + File.separator + file.getName() + "'. File detected as '" + metaFile.getMetaFileType().name() + "'");
                        }
                    } else {
                        gui.log(GUI.LogLevel.INFO, "File '" + file.getParent() + File.separator + file.getName() + "' is not a valid FiveM .meta file");
                    }
                }
            }
        });
        add(buttonAddFile);

        JButton buttonMerge = new JButton("Merge");
        buttonMerge.setToolTipText("<html><p style=\"padding-bottom:2px\">Combines all added <b>.meta</b> files into a<br>single <b>.meta</b> file of each type.</p></html>");
        buttonMerge.setBorderPainted(false);
        buttonMerge.setFocusPainted(false);
        buttonMerge.addActionListener(e -> {
            if (metaFiles.isEmpty()) {
                gui.log(GUI.LogLevel.ERROR, "No files have been added for merging");
            }
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose Destination Folder");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = fileChooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                gui.log(GUI.LogLevel.INFO, "Preparing to merge files...");
                File destinationFolder = fileChooser.getSelectedFile();
                metaFiles.entrySet().stream().forEach(entry -> {
                    gui.log(GUI.LogLevel.INFO, "Merging all '" + entry.getKey().name() + "' files...");
                    Merger merger = entry.getKey().getMerger(entry.getValue());
                    Document document = merger.merge();
                    try {
                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
                        Transformer transformer = transformerFactory.newTransformer();
                        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                        DOMSource source = new DOMSource(document);
                        StreamResult result = new StreamResult(new File(destinationFolder + File.separator + entry.getKey().getFileName()));
                        transformer.transform(source, result);
                    } catch (Exception ex) {
                        gui.log(GUI.LogLevel.ERROR, "Error: Unable to merge '" + entry.getKey().name() + "' files");
                        ex.printStackTrace();
                    }
                    gui.log(GUI.LogLevel.INFO, "Successfully merged all '" + entry.getKey().name() + "' files");
                });
            }
        });
        add(buttonMerge);
    }
}
