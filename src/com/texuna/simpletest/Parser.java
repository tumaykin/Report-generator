/**
 * Created by kirill on 13.08.2016.
 */
package com.texuna.simpletest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.stream.*;

public class Parser {
    private static Integer pageWidth;
    private static Integer pageHeight;
    private static ArrayList<String> columnNames = new ArrayList<String>();
    private static ArrayList<Integer> columnSizes = new ArrayList<Integer>();
    private static Integer actualPageWidth = 1;
    private static ArrayList<String> columnsInfo = new ArrayList();



    public static void parseXML(String fileName) {
        try {
            XMLStreamReader xmlr = XMLInputFactory.newInstance().createXMLStreamReader(fileName, new FileInputStream(fileName));

            while (xmlr.hasNext()) {
                xmlr.next();
                if (xmlr.isStartElement() && xmlr.getLocalName().equals("page")) {
                    while(xmlr.hasNext()){
                        xmlr.next();
                        if(xmlr.isStartElement() && xmlr.getLocalName().equals("width")) {
                            xmlr.next();
                            pageWidth = Integer.valueOf(xmlr.getText());
                        }
                        else if(xmlr.isStartElement() && xmlr.getLocalName().equals("height")){
                            xmlr.next();
                            pageHeight = Integer.valueOf(xmlr.getText());
                        }
                        else if(xmlr.isEndElement() && xmlr.getLocalName().equals("page")) break;
                    }
                } else if(xmlr.isStartElement() && xmlr.getLocalName().equals("columns")) {
                    while(xmlr.hasNext()){
                       xmlr.next();
                        if(xmlr.hasText() && xmlr.getText().trim().length() > 0){
                        columnsInfo.add(xmlr.getText());
                        }
                        else if(xmlr.isEndElement() && xmlr.getLocalName().equals("columns")) break;
                    }
                }
            }
        } catch (FileNotFoundException | XMLStreamException ex) {
            ex.printStackTrace();
        }

        for(int i = 0; i < columnsInfo.size(); i+= 2){
            String name = columnsInfo.get(i);
            Integer size = Integer.valueOf(columnsInfo.get(i+1));
            //columnsNameToValue.put(key, value);
            columnNames.add(name);
            columnSizes.add(size);
            actualPageWidth += size + 3;
        }
    }

    public static List<String> getColumnNames() {
        return Collections.unmodifiableList(columnNames);
    }

    public static List<Integer> getColumnSizes() {
        return Collections.unmodifiableList(columnSizes);
    }

    public static Integer getPageWidth() {
        return pageWidth;
    }

    public static Integer getPageHeight() {
        return pageHeight;
    }

    public static Integer getActualPageWidth() {
        return actualPageWidth;
    }
}




