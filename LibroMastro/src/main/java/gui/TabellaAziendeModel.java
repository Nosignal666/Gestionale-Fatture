package gui;

import java.util.ArrayList;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import strutturaDati.Azienda;

public class TabellaAziendeModel extends AbstractTableModel{
	
	public TabellaAziendeModel(ArrayList<Azienda> aziende) {
		super();
		this.aziende = aziende;
	}

	private ArrayList<Azienda> aziende;

	

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public String getColumnName(int columnIndex) {
		if(columnIndex==0) return "Nome Azienda";
		else return "Partita Iva";
	}

	@Override
	public int getRowCount() {
		return aziende.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Azienda azienda=aziende.get(rowIndex);
		if(columnIndex==0) return azienda.getNomeAzienda();
		else return azienda.getPartitaIva();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		
	}
	

}
