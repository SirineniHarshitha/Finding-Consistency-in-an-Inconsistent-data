package com;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.JTextArea;
public class ReadDataset{

public static String readDataset(String path,JTextArea area) {
	StringBuilder sb = new StringBuilder();
	try{
		BufferedReader br = new BufferedReader(new FileReader(path));
		String line = null;
		while((line = br.readLine()) != null){
			line = line.trim();
			if(line.length() > 0) {
				sb.append(line+System.getProperty("line.separator"));
				area.append(line+"\n");
			}
		}
		br.close();
		sb.deleteCharAt(sb.length()-1);
	}catch(Exception e) {
		e.printStackTrace();
	}
	return sb.toString().trim();
}
}