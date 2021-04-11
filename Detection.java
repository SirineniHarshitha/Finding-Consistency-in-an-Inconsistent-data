package com;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JFrame;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.jfree.ui.RefineryUtilities;
public class Detection extends JFrame implements ActionListener{
	MyTableModel dtm;
	JScrollPane jsp;
	JTable table;
	JPanel p1;
	JLabel l1,l2,l3,l4;
	JComboBox c1,c2,c3;
	JTextField tf1;
	JButton b1,b2,b3;
	Font f1;
	ArrayList<String> columns = new ArrayList<String>(); 
	int lhs,rhs;
	LinkedHashMap<String,String> group = new LinkedHashMap<String,String>();
	String dc_name;
	ArrayList<Integer> dc_list = new ArrayList<Integer>();
	String opr;
	ArrayList<Sort> output = new ArrayList<Sort>();
	static long mysql_time,onescan_time;
public Detection(){
	setTitle("Inconsistency Detection Screen");
	f1 = new Font("Courier New",Font.BOLD,14);
	p1 = new JPanel();
	p1.setLayout(null);
	l1 = new JLabel("Table Name");
	l1.setFont(f1);
	l1.setBounds(100,10,150,30);
	p1.add(l1);
	c1 = new JComboBox();
	c1.setFont(f1);
	c1.setBounds(250,10,150,30);
	p1.add(c1);
	c1.addActionListener(this);

	l2 = new JLabel("Column Name");
	l2.setFont(f1);
	l2.setBounds(100,60,150,30);
	p1.add(l2);
	c2 = new JComboBox();
	c2.setFont(f1);
	c2.setBounds(250,60,150,30);
	p1.add(c2);
	c2.addActionListener(this);

	l3 = new JLabel("Denial Constraint Value");
	l3.setFont(f1);
	l3.setBounds(50,110,200,30);
	p1.add(l3);
	tf1 = new JTextField();
	tf1.setFont(f1);
	tf1.setBounds(250,110,100,30);
	p1.add(tf1);

	l4 = new JLabel("Denial Constraint Operators");
	l4.setFont(f1);
	l4.setBounds(10,160,240,30);
	p1.add(l4);
	c3 = new JComboBox();
	c3.setFont(f1);
	c3.setBounds(250,160,150,30);
	p1.add(c3);
	c3.addItem("=");
	c3.addItem("!=");
	c3.addItem("<");
	c3.addItem(">");
	c3.addItem("<=");
	c3.addItem(">=");

	b1 = new JButton("Execute Query");
	b1.setFont(f1);
	p1.add(b1);
	b1.setBounds(80,210,180,30);
	b1.addActionListener(this);

	b2 = new JButton("Inconsistency Detection");
	b2.setFont(f1);
	p1.add(b2);
	b2.setBounds(270,210,220,30);
	b2.addActionListener(this);

	b3 = new JButton("Query Execution Graph");
	b3.setFont(f1);
	p1.add(b3);
	b3.setBounds(530,210,220,30);
	b3.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			Chart chart1 = new Chart("Query Execution & Inconsistency Detection Graph");
			chart1.pack();
			RefineryUtilities.centerFrameOnScreen(chart1);
			chart1.setVisible(true);
		}
	});

	dtm = new MyTableModel(){
		public boolean isCellEditable(){
			return false;
		}
	};
	table = new JTable(dtm);
	table.getTableHeader().setFont(new Font("Courier New",Font.BOLD,15));
	table.setFont(new Font("Courier New",Font.BOLD,14));
	table.setRowHeight(30);
	jsp = new JScrollPane(table);
	getContentPane().add(jsp,BorderLayout.SOUTH);
	getContentPane().add(p1,BorderLayout.CENTER);
	//jsp.setPreferredSize(new Dimension(800,600));
}
public void addTable() {
	String arr[] = DBCon.getTables();
	c1.removeActionListener(this);
	c1.removeAllItems();
	for(String str : arr){
		c1.addItem(str);
	}
	c1.addActionListener(this);
}
public void addColumns(){
	String table = c1.getSelectedItem().toString().trim();
	String arr[] = DBCon.getColumns(table);
	c2.removeActionListener(this);
	c2.removeAllItems();
	for(String str : arr){
		c2.addItem(str);
	}
	c2.addActionListener(this);
}

public void actionPerformed(ActionEvent ae) {
	if(ae.getSource() == c1) {
		addColumns();
	}
	if(ae.getSource() == c2) {
		String column = c2.getSelectedItem().toString().trim();
		if(!columns.contains(column))
			columns.add(column);
	}
	if(ae.getSource() == b1) {
		long start = System.currentTimeMillis();
		clearTable();
		String table = c1.getSelectedItem().toString().trim();
		opr = c3.getSelectedItem().toString().trim();
		String value = tf1.getText().trim();
		StringBuilder query = new StringBuilder();
		query.append("select t1.*,t2.* from "+table+" t1, "+table+" t2 where ");
		for(int i=0;i<columns.size()-1;i++){
			String name = columns.get(i);
			query.append("t1."+name+" = t2."+name+" AND ");
		}
		String name = columns.get(columns.size()-1);
		dc_name = name;
		query.append("t1."+name+" "+opr+" "+value);
		System.out.println(query);
		System.out.println(columns);
		DBCon.executeQuery(query.toString().trim(),dtm);
		columns.clear();
		long end = System.currentTimeMillis();
		mysql_time = end - start;
	}
	if(ae.getSource() == b2) {
		long start = System.currentTimeMillis();
		inconsistencyDetection();
		long end = System.currentTimeMillis();
		onescan_time = end - start;
		System.out.println(mysql_time+" "+onescan_time);
	}
}
public void inconsistencyDetection(){
	dc_list.clear();
	group.clear();
	lhs = 0;
	rhs = 0;
	boolean flag = false;
	for(int i=0;i<dtm.getColumnCount();i++){
		String name = dtm.getColumnName(i);
		if(name.equals(dc_name)) {
			dc_list.add(i);
		}
		if(group.containsKey(name) && !flag){
			flag = true;
			rhs = i;
		} else {
			group.put(name,name);
		}
	}
	mergeOutput();
}
public void mergeOutput(){
	output.clear();
	for(int i=0;i<dtm.getRowCount();i++){
		StringBuilder sb = new StringBuilder();
		String value = "none";
		for(int j=0;j<dtm.getColumnCount();j++){
			value = dtm.getValueAt(i,j).toString().trim();
			sb.append(value+",");
		}
		sb.deleteCharAt(sb.length()-1);
		Sort s = new Sort();
		s.setRecord(sb.toString());
		s.setValue(value);
		output.add(s);
	}
	sort();
}
public void clearRows(){
	for(int i=table.getRowCount()-1;i>=0;i--){
		dtm.removeRow(i);
	}
}
public void sort(){
	clearRows();
	java.util.Collections.sort(output,new Sort());
	for(int i=0;i<output.size();i++){
		Sort s = output.get(i);
		matchLHS_RHS_Subset(s.getRecord());
	}
}
public void matchLHS_RHS_Subset(String merge_output){
	String arr[] = merge_output.split(",");
	String lhs_subset = arr[dc_list.get(0)];
	String rhs_subset = arr[dc_list.get(1)];
	if(opr.equals("=")){
		double d1 = Double.parseDouble(lhs_subset);
		double d2 = Double.parseDouble(rhs_subset);
		double input = Double.parseDouble(tf1.getText().trim());
		if(d1 == input && d2 == input) {
			dtm.addRow(arr);
		}
	}
	if(opr.equals("<")){
		double d1 = Double.parseDouble(lhs_subset);
		double d2 = Double.parseDouble(rhs_subset);
		double input = Double.parseDouble(tf1.getText().trim());
		if(d1 < input && d2 < input) {
			dtm.addRow(arr);
		}
	}

	if(opr.equals(">")){
		double d1 = Double.parseDouble(lhs_subset);
		double d2 = Double.parseDouble(rhs_subset);
		double input = Double.parseDouble(tf1.getText().trim());
		if(d1 > input && d2 > input) {
			dtm.addRow(arr);
		}
	}

	if(opr.equals("<=")){
		double d1 = Double.parseDouble(lhs_subset);
		double d2 = Double.parseDouble(rhs_subset);
		double input = Double.parseDouble(tf1.getText().trim());
		if(d1 <= input && d2 <= input) {
			dtm.addRow(arr);
		}
	}
	if(opr.equals(">=")){
		double d1 = Double.parseDouble(lhs_subset);
		double d2 = Double.parseDouble(rhs_subset);
		double input = Double.parseDouble(tf1.getText().trim());
		if(d1 >= input && d2 >= input) {
			dtm.addRow(arr);
		}
	}

	if(opr.equals("!=")){
		double d1 = Double.parseDouble(lhs_subset);
		double d2 = Double.parseDouble(rhs_subset);
		double input = Double.parseDouble(tf1.getText().trim());
		if(d1 != input && d2 != input) {
			dtm.addRow(arr);
		}
	}
}

public void clearTable(){
	for(int i=table.getRowCount()-1;i>=0;i--){
		dtm.removeRow(i);
	}
	for(int i=table.getColumnCount()-1;i>=0;i--){
		dtm.removeColumn(i);
	}
}
}
class MyTableModel extends DefaultTableModel {
    public void removeColumn(int column){
		columnIdentifiers.remove(column);
		for(Object row: dataVector){
			((java.util.Vector) row).remove(column);
		}
		fireTableStructureChanged();
    }	
}