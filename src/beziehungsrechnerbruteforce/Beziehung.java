package beziehungsrechnerbruteforce;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Dies ist die Berechnungsklasse des Dabendorfer Beziehungsrechners. Diese fuegt die Namen zusammen und setzt ihre mathematischen Werte solange zusammen,
 * bis eine Zahl zwischen 0 und 100 herauskommt. Selbige wird als Prozentwert, wie gut zwei Menschen zusammenpassen ausgegeben.
 * 
 * @author Lukas Schramm und Miriam Fischer
 * @version 1.2
 *
 */
public class Beziehung implements Comparable<Beziehung> {
    
	/** Diese Liste enthaelt die urspruenglich berechneten Startzahlen und wird bis zum Ende hin modifiziert, bis sie die Endwerte enthaelt.**/
    private ArrayList<Integer> zahlen = new ArrayList<Integer>();
    /** Der Zaehler zaehlt die Vorkommen einzelner Buchstaben.**/
    private int zaehler;
    /** Diese Variable gibt die Laenge der restlichen Zahlenkette aus. Wenn sie kleiner als 3 ist, ist das Endergebnis berechnet.**/
    private int laenge;
    /** Dieser int zaehlt, welche Stelle der Index der letzten verfuegbaren Zahl hat. Der Wert ist invers zur Zaehlvariable i in der Methode berechne().**/
    private int temp;
    /** Dieser String-Array enthaelt die urspruenglichen Namen zur Ausgabe des Ergebnisses.**/
    private String[] namen = new String[2];
    /** Dieser Wert gibt wieder, wie viel die Beziehung wert ist.**/
    private int beziehungswert;
    
    
    public Beziehung(String[] ursprungsname) {
    	ordne(ursprungsname);
    }
    
    /**
     * Diese Methode nimmt die Namen der Spieler entgegen, sortiert sie alphabetisch, fuegt sie zusammen
     * und macht aus ihr die erste Zahlenreihe, die an die Berechnungsmethode weitergegeben wird.
     * Hierbei wird die Liste an Einzelbuchstaben in einer doppelten for-Schleife selbst iteriert und durchgezaehlt, wie oft ein Buchstabe vorkommt.
     * @param ursprungsname Nimmt ein Array der beiden urspruenglichen Namen entgegen.
     */
    public void ordne(String[] ursprungsname) {
    	this.namen = ursprungsname;
        zahlen.clear();

        Arrays.sort(namen);
        String gesamtname = namen[0] + namen[1];
        gesamtname = gesamtname.toLowerCase();

        char[] chars = gesamtname.toCharArray();
        
        temp = chars.length;
        zaehler = 0;

        for(char a:chars) {
        	for(char b:chars) {
        		if(a==b) {
                    zaehler++;
                }
        	}
            zahlen.add(zaehler);
            zaehler = 0;
        }
        
        zaehler = 0;
        berechne();
    }
    
    /**
     * Diese Methode repraesentiert einen Rechenschritt des Beziehungsrechners. Sie ruft sich solange rekursiv auf, bis ein logischer Prozentwert erreicht wird.
     * Es werden jede Runde die erste und die letzte Zahl aufgerufen und zusammen addiert. Bei Ergebnissen groesser als 9 greift eine Zusatzmethode die das Problem doppelter Ziffern loest.
     * Wenn die Endbedingung erreicht ist, wird das Ergebnis ausgeworfen, ansonsten startet eine neue Berechnung.
     */
    private void berechne() {
    	ArrayList<Integer> zahlenStatisch = new ArrayList<Integer>(); /** Diese Methode enthaelt eine Kopie der Liste "zahlen", wird bis Methodenende stets nicht angetastet und dient dem durchiterieren.**/
        ArrayList<Integer> digits = new ArrayList<Integer>(); /** Diese Liste enthaelt bei Ueberschreitung einstelliger Zahlen bei der Berechnung die Ziffern des Schrittes.**/
    	for(int i=0;i<zahlen.size();i++) {
            zahlenStatisch.add(zahlen.get(i));
        }
    	laenge = zahlenStatisch.size();
        temp = laenge-1;
        zahlen.clear();
        for(int i=0;i<(laenge/2);i++) {
            zaehler = zahlenStatisch.get(i) + zahlenStatisch.get(temp);
            if(zaehler > 9) {
                while(zaehler > 0) {
                    digits.add(0, zaehler%10);
                    zaehler/=10;
                }
                zahlen.add(digits.get(0));
                zahlen.add(digits.get(1));
            } else {
                zahlen.add(zaehler);
            }
            temp--;
        }
        /**
         * Bei ungerader Zahlenkettenlaenge wird die mittlere Zahl an die neue Kette hinten dran gehaengt.
         */
        if(laenge %2 != 0) {
            zaehler = zahlenStatisch.get(laenge/2);
            zahlen.add(zaehler);
        }
        if(zahlen.size() > 2 && !(zahlen.size()==3 && zahlen.get(0)==1 && zahlen.get(1)==0 && zahlen.get(2)==0)) {
     	   berechne();
        } else {
        	String gesamt = "";
        	for(int zahl:zahlen) {
        		gesamt += String.valueOf(zahl);
        	}
        	beziehungswert = Integer.valueOf(gesamt);
        }
    }
    
    public String getName(int num) {
		return namen[num];
	}

	public int getBeziehungswert() {
		return beziehungswert;
	}

	@Override
	public int compareTo(Beziehung o) {
		return ((Integer)beziehungswert).compareTo((Integer)o.beziehungswert);
	}
}