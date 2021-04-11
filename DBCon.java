package com;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.sql.Statement;
import java.sql.ResultSetMetaData;
public class DBCon{
    private static Connection con;
public static Connection getCon()throws Exception {
    Class.forName("com.mysql.jdbc.Driver");
    con = DriverManager.getConnection("jdbc:mysql://localhost/onescan","root","root");
    return con;
}
public static void createTable(String arr[]){
	try{
		String table = arr[0];
		String temp[] = table.split("\\s+");
		table  = temp[2].trim().toLowerCase();
		Connection con = getCon();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("show tables");
		boolean flag = false;
		while(rs.next()){
			String name = rs.getString(1).trim().toLowerCase();
			if(name.equals(table)) {
				flag = true;
				break;
			}
		}
		if(!flag){
			for(int i=0;i<arr.length;i++){
				stmt = con.createStatement();
				stmt.execute(arr[i]);
			}
		}
	}catch(Exception e){
		e.printStackTrace();
	}
}
public static String[] getTables(){
	StringBuilder sb = new StringBuilder();
	try{
		con = getCon();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("show tables");
		while(rs.next()){
			sb.append(rs.getString(1)+" ");
		}
		rs.close();stmt.close();
	}catch(Exception e){
		e.printStackTrace();
	}
	return sb.toString().trim().split("\\s+");
}
public static String[] getColumns(String table){
	StringBuilder sb = new StringBuilder();
	try{
		con = getCon();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("show columns from "+table);
		while(rs.next()){
			sb.append(rs.getString(1)+" ");
		}
		rs.close();stmt.close();
	}catch(Exception e){
		e.printStackTrace();
	}
	return sb.toString().trim().split("\\s+");
}
public static void executeQuery(String query,MyTableModel dtm){
	try{
		con = getCon();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();
		for (int i=1;i<=count;i++){
			dtm.addColumn(rsmd.getColumnName(i));
		}
		while(rs.next()){
			Object row[] = new Object[count];
			for (int i=1;i<=count;i++){
				row[i-1] = rs.getString(i);
			}
			dtm.addRow(row);
		}
		rs.close();stmt.close();
	}catch(Exception e){
		e.printStackTrace();
	}
}
}
