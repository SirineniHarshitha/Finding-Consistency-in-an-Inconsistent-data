package com;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.UIManager;
import java.awt.Cursor;
import javax.swing.JFileChooser;
import java.io.File;
import java.util.ArrayList;
import java.io.FileWriter;
import org.jfree.ui.RefineryUtilities;
public class MainScreen extends JFrame{
	String name;
	JPanel p1,p2,p3,p4;
	JLabel l1;
	Font f1,f2;
	JButton b1,b2,b3,b4,b5,b6;
	JScrollPane jsp;
	JTextArea area;
	JFileChooser chooser;
	File file;
	
public MainScreen(){
	super("One-pass Inconsistency");
	setLayout(new BorderLayout());
	f1 = new Font("Courier New",Font.BOLD,16);
	f2 = new Font("Courier New",Font.BOLD,13);
	p1 = new JPanel();
	p1.setLayout(new BorderLayout());
	p1.setBackground(Color.white);
	
	chooser = new JFileChooser(new File("dataset"));

	p2 = new JPanel();
	l1 = new JLabel("<HTML><BODY><CENTER>One-pass Inconsistency Detection Algorithms for Big Data</CENTER></BODY></HTML>");
	l1.setFont(f1);
	p2.add(l1);
	p2.setBackground(Color.white);

	p3 = new JPanel();
	p3.setPreferredSize(new Dimension(200,80));
	p3.setBackground(Color.white);
	
	b1 = new JButton("Upload Dataset Tables");
	b1.setFont(f2);
	p3.add(b1);
	b1.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			int option = chooser.showOpenDialog(MainScreen.this);
			if(option == JFileChooser.APPROVE_OPTION){
				file = chooser.getSelectedFile();
				area.setText("");
				String dataset = ReadDataset.readDataset(file.getPath(),area);
				String arr[] = dataset.split("\n");
				try{
					DBCon.createTable(arr);
				}catch(Exception e){
					e.printStackTrace();
				}
				JOptionPane.showMessageDialog(MainScreen.this,"Sample table created");
			}
		}
	});

	b4 = new JButton("Inconsistency Detection");
	b4.setFont(f2);
	p3.add(b4);
	b4.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			Detection d = new Detection();
			d.setVisible(true);
			d.setExtendedState(JFrame.MAXIMIZED_BOTH);
			d.addTable();
		}
	});

	b6 = new JButton("Exit");
	b6.setFont(f2);
	p3.add(b6);
	b6.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			System.exit(0);
		}
	});

	p1.add(p2,BorderLayout.NORTH);
	p1.add(p3,BorderLayout.CENTER);
	
	p4 = new JPanel();
	p4.setLayout(new BorderLayout());
	p4.setBackground(Color.white);
	area = new JTextArea();
	area.setEditable(false);
	area.setFont(f2);
	jsp = new JScrollPane(area);
	p4.add(jsp,BorderLayout.CENTER);
	

	add(p1,BorderLayout.NORTH);
	add(p4,BorderLayout.CENTER);
}

public static void main(String a[])throws Exception{
	 UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	 MainScreen ps = new MainScreen();
	 ps.setVisible(true);
	 ps.setExtendedState(JFrame.MAXIMIZED_BOTH);
}
}