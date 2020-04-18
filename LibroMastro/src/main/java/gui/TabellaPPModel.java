package gui;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import strutturaDati.PagamentoParziale;

public class TabellaPPModel extends AbstractTableModel{
	
	private ArrayList<PagamentoParziale> pagamentiParziali;
	private final String[] nomeCampi= {"Indice pagamento","Data pagamento","Importo","Tipo Pagamento","Ultima modifica"};
	private Field[] campi=PagamentoParziale.class.getDeclaredFields();
	
	public TabellaPPModel() {
		pagamentiParziali=new ArrayList<PagamentoParziale>();
	}
	


	@Override
	public Class<?> getColumnClass(int arg0) {
		if(arg0==0) return String.class;
		else return campi[arg0].getClass();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return nomeCampi.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return nomeCampi[columnIndex];
	}

	@Override
	public int getRowCount() {
		return pagamentiParziali.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object o=null;
		campi[columnIndex].setAccessible(true);
		try {
			 if(columnIndex==1) {
				 LocalDate date=(LocalDate)campi[columnIndex].get(pagamentiParziali.get(rowIndex));
				 o=date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			 }
			 else o=campi[columnIndex].get(pagamentiParziali.get(rowIndex));
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return o;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}


	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		try {
			campi[columnIndex].set(pagamentiParziali.get(rowIndex), aValue);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
	}
	
	public void setPagamentiParziali(ArrayList<PagamentoParziale> pagamentiparziali) {
		this.pagamentiParziali=pagamentiparziali;
	}
	

}
