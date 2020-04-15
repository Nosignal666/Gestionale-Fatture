package gui;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import strutturaDati.Fattura;

public class TabellaFatturaModel extends AbstractTableModel{
	
	ArrayList<Fattura> fatture;
	private final String[] nomeCampi= {"Codice Univoco","NomeAzienda","Partita Iva","Numero Progressivo","Anno","Data Emissione","Data Scadenza","Importo","note","utente","Stato"};
	private Field[] campi=Fattura.class.getDeclaredFields();
	
	
	public TabellaFatturaModel(ArrayList<Fattura> fatture) {
		this.fatture=fatture;
	}
	
	public Fattura getFattura(int i) {
		return fatture.get(i);
	}

	@Override
	public Class<?> getColumnClass(int arg0) {
		if(arg0==5 | arg0==6) return String.class;
		else return campi[arg0].getClass();
	}

	@Override
	public int getColumnCount() {
		return 11;
	}

	@Override
	public String getColumnName(int arg0) {
		return nomeCampi[arg0];
	}

	@Override
	public int getRowCount(){
		return fatture.size();
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		campi[arg1].setAccessible(true);
		Object o=null;
		try {
			 if(arg1==5 | arg1==6) {
				 LocalDate date=(LocalDate)campi[arg1].get(fatture.get(arg0));
				 o=date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			 }
			 else o=campi[arg1].get(fatture.get(arg0));
		}catch(IllegalArgumentException | IllegalAccessException e) {e.printStackTrace();}
		return o;
	}

	@Override
	public boolean isCellEditable(int arg0, int arg1) {
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValueAt(Object arg0, int arg1, int arg2) {
		try {
			campi[arg2].set(fatture.get(arg1), arg0);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

}

