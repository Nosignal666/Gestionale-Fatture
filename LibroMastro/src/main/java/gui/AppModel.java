package gui;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.joda.money.Money;

import storage.DataBaseStorageManager;
import strutturaDati.Azienda;
import strutturaDati.Fattura;
import strutturaDati.MalformedDataException;
import strutturaDati.PagamentoParziale;

public class AppModel {
	String filtroNomeAziendaPiece=null,filtroPartitaIvaHead=null;
	private DataBaseStorageManager dbsm;
	private ArrayList<Observer> ObserverGroup;
	ArrayList<Fattura> fatture;
	ArrayList<Azienda> aziende;
	
	public AppModel() {
		ObserverGroup=new ArrayList<Observer>();
		fatture=new ArrayList<Fattura>();
		aziende=new ArrayList<Azienda>();
		dbsm=new DataBaseStorageManager();
	}
	
	public void initStorage(String user,String password,String url) throws ClassNotFoundException, SQLException {
		try {
			dbsm.initStorage(user, password, url);
		} catch (ClassNotFoundException | SQLException e) {
			throw e;
		}
	}
	
	public void aggiungiObserver(Observer obs) {
		ObserverGroup.add(obs);
	}
	
	public void notificaObserverGroup() {
		for(Observer obs:ObserverGroup) {
			obs.notificaCambiamento();
		}
	}
	
	public void fetch(String nomeAziendaPiece,String partitaIvaHead) {
		try {
			fatture.clear();
			aziende.clear();
			filtroNomeAziendaPiece=null;
			filtroPartitaIvaHead=null;
			fatture.addAll(dbsm.leggiFatture(nomeAziendaPiece,partitaIvaHead));
			aziende.addAll(dbsm.leggiAziende(nomeAziendaPiece,partitaIvaHead));
			filtroNomeAziendaPiece=nomeAziendaPiece;
			filtroPartitaIvaHead=partitaIvaHead;
			notificaObserverGroup();
		} catch (SQLException | MalformedDataException e) {
			e.printStackTrace();
		}
	}
	

	
	public void refetch() {
		if(filtroNomeAziendaPiece==null | filtroPartitaIvaHead==null) return;
		fetch(filtroNomeAziendaPiece,filtroPartitaIvaHead);
	}
	
	public Fattura getFattura(String codiceUnivoco,String partitaIva) {
		return (Fattura)fatture.stream().filter(x -> x.getCodiceUnivoco().equals(codiceUnivoco)  & x.getPartitaIva().equals(partitaIva))
				.findFirst().orElse(null);
	}
	
	public ArrayList<Azienda> getAziende(){
		return aziende;
	}
	
	public ArrayList<Fattura> getFatture(){
		return fatture;
	}
	
	public void inserisciPagamentoParziale(PagamentoParziale pp) throws SQLException, MalformedDataException {
		dbsm.inserisciPagamentoParziale(pp);
	}
	public void inserisciFattura(Fattura fattura) throws SQLException, FileNotFoundException {
		dbsm.inserisciFattura(fattura);
	}
	public void inserisciAzienda(Azienda azienda) throws SQLException, MalformedDataException {
		dbsm.inserisciAzienda(azienda);
	}
	public Boolean checkifAlreadyExist(LocalDate dataPagamento,Money importo) throws SQLException {
		return dbsm.checkIfAlreadyExistPP(dataPagamento, importo);
	}
	public void scriviEstrattoConto(String nomeAzienda,String partitaIva,LocalDate dataInizio,LocalDate dataFine,File dest) throws FileNotFoundException, SQLException, MalformedDataException {
		dbsm.scriviEstrattoConto(nomeAzienda, partitaIva, dataInizio, dataFine,dest);
	}
	public ArrayList<Azienda> leggiAziende(String partitaIvaHead,String nomeAziendaPiece) throws SQLException, MalformedDataException{
		return dbsm.leggiAziende(partitaIvaHead, nomeAziendaPiece);
	}
	public byte[] getPdfFileContent(String codiceUnivoco,String partitaIva) throws SQLException {
		return dbsm.getPdfFileContent(codiceUnivoco, partitaIva);
	}
	public void modificaFattura(Fattura fatturaOriginale,Fattura fatturaModificata) throws Exception {
		dbsm.modificaFattura(fatturaOriginale,fatturaModificata);
	}
	public void modificaPagamentoParziale(PagamentoParziale ppOriginale,PagamentoParziale ppModificato) throws SQLException {
		dbsm.modificaPagamentoParziale(ppOriginale, ppModificato);
	}

}
