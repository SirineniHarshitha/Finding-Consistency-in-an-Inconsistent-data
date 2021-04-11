package com;
import java.util.Comparator;
public class Sort implements Comparator<Sort>{
	String record;
	String value;
public void setRecord(String record){
	this.record = record;
}
public String getRecord(){
	return record;
}
public void setValue(String value){
	this.value = value;
}
public String getValue(){
	return value;
}

public int compare(Sort p1,Sort p2){
	return p1.getValue().compareTo(p2.getValue());  
}
}