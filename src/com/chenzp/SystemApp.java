package com.chenzp;

import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;
import java.io.File;
import java.util.Set;
import java.util.Map;

/**
 * Created by chenzhongpu on 3/15/16.
 */

public class SystemApp {

    public SystemApp() {

        initServers();
    }

    public void initServers() {
        try {
            File configFile = new File("serverConfig.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(configFile);

            doc.getDocumentElement().normalize();

            NodeList nodes = doc.getElementsByTagName("server");

            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element)node;
                    String address = element.getElementsByTagName("address").item(0).getTextContent();
                    boolean isLeader = Boolean.valueOf(element.getElementsByTagName("isLeader").item(0).getTextContent()).booleanValue();

                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
