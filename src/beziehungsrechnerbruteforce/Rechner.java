package beziehungsrechnerbruteforce;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Dies ist die Hauptklasse des Dabendorfer Beziehungsrechners. Dieser berechnet anhand mathematischer Methoden durch sprachliche Eigenschaften der Namen,
 * wie gut zwei Menschen zusammenpassen. Die Ergebnisse sind offiziell und durch den grossen N zur Verfuegung gestellt worden.<br>
 * Diese Hauptklasse kontrolliert die Namenseingabe und leitet bei erfolgreicher Eingabe in die Berechnungsklasse weiter.<br>
 * Sie liest eine Liste an Namen ein und gibt alle moeglichen Beziehungskombinationen geordnet nach Wertigkeit in einer Tabelle aus.
 * 
 * @author Lukas Schramm
 * @version 1.2
 *
 */
public class Rechner {
	
	private JFrame frame1 = new JFrame("Beziehungsliste");
	private JTable tabelle1 = new JTable();
	private GridLayout gridlayout = new GridLayout();
	private BufferedReader br;
	private ArrayList<String> namen = new ArrayList<String>();
	private ArrayList<Beziehung> beziehungen = new ArrayList<Beziehung>();
	
	public Rechner() {
		einlesen("./files/namen.txt");
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setPreferredSize(new Dimension(400,400));
		frame1.setMinimumSize(new Dimension(400,400));
		frame1.setResizable(true);
		frame1.getContentPane().setLayout(gridlayout);

		
		Vector<Object> eintraege = new Vector<Object>();
		for(Beziehung bez:beziehungen) {
			Vector<Object> zeile = new Vector<Object>();
			zeile.add(bez.getName(0));
			zeile.add(bez.getName(1));
			zeile.add(bez.getBeziehungswert());
			eintraege.add(zeile);
		}

		Vector<String> titel = new Vector<String>();
		titel.add("Erster Name");
		titel.add("Zweiter Name");
		titel.add("Beziehungswert");
		tabelle1 = new JTable(eintraege, titel);
		
		tabelle1.getColumn("Erster Name").setPreferredWidth(37);
	    tabelle1.getColumn("Zweiter Name").setPreferredWidth(37);
	    tabelle1.getColumn("Beziehungswert").setPreferredWidth(26);
	    tabelle1.getTableHeader().setBackground(Color.lightGray);
	    tabelle1.setEnabled(false);
	    
	    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	    centerRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
	    for(int x=0;x<tabelle1.getColumnCount();x++) {
	    	tabelle1.getColumnModel().getColumn(x).setCellRenderer(centerRenderer);
	    	tabelle1.getTableHeader().getColumnModel().getColumn(x).setCellRenderer(centerRenderer);
	    }
	    
	    tabelle1.setDefaultRenderer(String.class, centerRenderer);
	    tabelle1.setVisible(true);
	    frame1.getContentPane().add(new JScrollPane(tabelle1));
		
		frame1.pack();
		frame1.setLocationRelativeTo(null);
		frame1.setVisible(true);
	}
	
	/**
	 * Diese Methode liest die Datei mit den Namen ein und warnt den Nutzer bei ungueltigen Namen. Sie liest ausserdem alle enthaltenen Namen in eine Liste ein.
	 * @param dateiname Nimmt den Dateinamen entgegen.
	 */
	private void einlesen(String dateiname) {
		File file = new File(dateiname);
		if(!file.canRead() || !file.isFile()) {
			JOptionPane.showMessageDialog(null, "Die Datei \".files/namen.txt\" ist nicht verfügbar oder kaputt."+System.getProperty("line.separator")+"Bitte überprüfe dies und starte das Programm erneut!", "Datei nicht vorhanden", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		} else {
			try {
	        	br = new BufferedReader(new FileReader(dateiname)); 
	            String zeile = null;
	            int num = 0;
	            boolean warnung = false;
	            while((zeile = br.readLine()) != null) { 
	            	if(num==0) {
	            		if(!zeile.equals("###beziehungsrechner###dabendorf###namensliste###")) {
	            			JOptionPane.showMessageDialog(null, "Das Dateiformat für Namenslisten wurde nicht eingehalten."+System.getProperty("line.separator")+"Die erste Zeile muss lauten:"+System.getProperty("line.separator")+"\"###beziehungsrechner###dabendorf###namensliste###\""+System.getProperty("line.separator")+"Danach werden Namen getrennt durch '#' aufgezählt.", "Datei ungültig", JOptionPane.ERROR_MESSAGE);
	            			System.exit(0);
	            		}
	            	} else {
	            		String[] temp = zeile.split("#");
		            	for(String str:temp) {
		            		if(!str.equals(null) && !str.equals("")) {
		            			String endname = "";
		            			for(char ch:str.toCharArray()) {
		            				String temp2 = String.valueOf(ch);
		            				if(temp2.matches("[a-zA-Z]*")) {
		            					endname += temp2;
		            				} else {
		            					if(!warnung) {
		            						JOptionPane.showMessageDialog(null, "Achtung. Dieses Programm unterstützt nur die 26 Standardbuchstaben von A bis Z."+System.getProperty("line.separator")+"Es wurden andere Zeichen gefunden. Diese sind entsprechend anzupassen."+System.getProperty("line.separator")+"Aus é wird beispielsweise ein e und aus ô wird o.", "Ungültige Zeichen", JOptionPane.ERROR_MESSAGE);
		            						warnung = true;
		            					}
		            				}
		            			}
		            			namen.add(endname);
		            		}
		            	}
	            	}
	            	num++;
	            }
	        } catch(IOException e) { 
	            e.printStackTrace(); 
	        } finally { 
	            if(br != null) {
	            	try { 
	                    br.close(); 
	                } catch (IOException e) { } 
	            }
	            berechnung();
	        }
		}
	}
	
	/**
	 * Diese Methode prueft anhand der Namensliste saemtliche Beziehungskombinationen und uebergibt sie dem Rechner.<br>
	 * Anschliessend sortiert sie die Liste absteigend.
	 */
	public void berechnung() {
		for(int a=0;a<namen.size();a++) {
			for(int b=0;b<a;b++) {
				String[] paar = {namen.get(a),namen.get(b)};
				beziehungen.add(new Beziehung(paar));
			}
		}
		Collections.sort(beziehungen);
		Collections.reverse(beziehungen);
	}
	
	public static void main(String[] args) {
		new Rechner();
	}
}