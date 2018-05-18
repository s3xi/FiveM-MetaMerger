package fivem.metamerger.merger;

import java.io.File;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fivem.metamerger.MetaFile;

public class Merger {

    private String[] nodeIds;
    private List<MetaFile> metaFiles;
    private Document document;

    public Merger(String[] nodeIds, List<MetaFile> metaFiles) {
        this.nodeIds = nodeIds;
        this.metaFiles = metaFiles;
        this.document = this.metaFiles.get(0).getDocument();
        this.metaFiles.remove(0);
    }

    public Document merge() {
        for (String nodeId : nodeIds) {
            NodeList parent = document.getElementsByTagName(nodeId);
            for (MetaFile metaFile : metaFiles) {
                NodeList nl = metaFile.getDocument().getElementsByTagName(nodeId);
                for (int i = 0; i < nl.getLength(); i++) {
                    NodeList children = nl.item(i).getChildNodes();
                    for (int j = 0; j < children.getLength(); j++) {
                        Node n = children.item(j);
                        if (n.getNodeType() == Node.ELEMENT_NODE) {
                            Node importNode = document.importNode(n, true);
                            parent.item(0).appendChild(importNode);
                        }
                    }
                }
            }
        }
        return document;
    }
}