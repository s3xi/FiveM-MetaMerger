package fivem.metamerger;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class MetaFile {

    private File file;
    private String nodeId;
    private Document document;

    public MetaFile(File file) {
        this.file = file;
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(file);
            document.getDocumentElement().normalize();
            nodeId = document.getDocumentElement().getNodeName();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public File getFile() {
        return file;
    }

    public String getNodeId() {
        return nodeId;
    }

    public MetaFileType getMetaFileType() {
        return MetaFileType.getFileType(this);
    }

    public Document getDocument() {
        return document;
    }
}
