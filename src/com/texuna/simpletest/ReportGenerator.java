package com.texuna.simpletest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by SBT-Tumaykin-KK on 18.08.2016.
 */
public class ReportGenerator implements Runnable {
    BufferedReader reader;
    BufferedWriter writer;
    boolean firstpage  = true;
    private int heightLeft = -1;
    private Object[] columnsSizes = Parser.getColumnSizes().toArray();

    ReportGenerator(BufferedReader reader, BufferedWriter writer){
        this.reader = reader;
        this.writer = writer;
    }

    private int getRequeredLines( List<String> currentLine){
        int requierdLines = 0;
        for (int i = 0; i < currentLine.size() ; i++){
            double currentColumnLength = (double)currentLine.get(i).length();
            double defindColumnLength = (Integer)columnsSizes[i];
            double columnLines = currentColumnLength / defindColumnLength;
            int  tmprequiredLines = (int)Math.ceil(columnLines);
            if(tmprequiredLines > requierdLines){
                requierdLines = tmprequiredLines;
            }
        }
        return requierdLines;
    }

    private void writePageSeparator() throws IOException {
        if(!firstpage) writer.write("~\n");
        firstpage = false;
    }

    private void writeLinesSeparator() throws IOException {
        String result = "";
        for (int i = 0; i < Parser.getActualPageWidth(); i++) {
            result += "-";
        }
        result += "\n";
        writer.write(result);
    }

    private void processLine( List<String> lineArray) throws IOException {
        int requiredLines = getRequeredLines(lineArray);
        ArrayList<LinkedList<String>> resultList = new ArrayList<LinkedList<String>>();
        LinkedList<String> splitBySpace;
        LinkedList<String> splitBySize;

        if (requiredLines == 0) return;
        for (int i = 0; i < lineArray.size(); i++) {
            String column = lineArray.get(i);
            Integer columnSize = (Integer) columnsSizes[i];
            splitBySpace = spliteBySpace(column, columnSize);
            splitBySize = splitBySize(splitBySpace, columnSize);
            resultList.add(splitBySize);
            }
        writeDataLine(resultList, requiredLines);
        }

    private LinkedList<String> spliteBySpace(String column, Integer columnSize){
        LinkedList<String> result = new LinkedList<String>();
        String[] splitedBySpace = column.split(" ");
        for (int i = 0; i < splitedBySpace.length; i++) {
            String first = splitedBySpace[i];
            while(i < splitedBySpace.length - 1) {
                String second = splitedBySpace[i + 1];
                if ((first + second).length() + 1 > columnSize) {
                    break;
                }
                first = first + " " + second;
                i++;
            }
            result.add(first);
        }
        return result;
    }

    private LinkedList<String> splitBySize(LinkedList<String> columnList, Integer columnSize){
        LinkedList<String> result = new LinkedList<String>();
        for (String column : columnList){
            while(true){
                Integer columnLength = column.length();
                Integer requiredRange = columnSize > columnLength ? columnLength : columnSize;
                String formatedColumn = column.substring(0, requiredRange);
                column = column.substring(requiredRange);
                result.add(formatedColumn);
                if (column.isEmpty()) break;
            }
        }
        return result;
    }

    private void writeDataLine(ArrayList<LinkedList<String>> resultList, int requiredLines) throws IOException {
        for (int i = 0; i < requiredLines; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < resultList.size(); j++) {
                String column;
                sb.append("| ");
                if (resultList.get(j).isEmpty()) {
                    column = "";
                } else {
                    column = resultList.get(j).removeFirst();
                }
                sb.append(addSpace(column, j));
            }
            sb.append("|\n");
            writer.write(sb.toString());
            heightLeft--;
        }
    }

    private String addSpace(String column, int index){
        String result = column;
        int columnSize = (int) columnsSizes[index];
        for (int i = 0; i <= columnSize - column.length(); i++){
            result += " ";
        }
        return result;
    }

    @Override
    public void run() {
        try {
            while(reader.ready()){
                String[] lineArray = reader.readLine().split("\t");
                ArrayList<String> lineArrayList = new ArrayList<String>(Arrays.asList(lineArray));
                int requiredLines = getRequeredLines(lineArrayList);
                if (heightLeft - requiredLines < 0){
                    heightLeft = Parser.getPageHeight();
                    writePageSeparator();
                    writeLinesSeparator();
                    processLine(Parser.getColumnNames());
                    writeLinesSeparator();
                }
                processLine(lineArrayList);
                writeLinesSeparator();
                writer.flush();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
