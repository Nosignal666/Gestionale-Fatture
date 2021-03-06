package strutturaDati;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

public class Fattura {
	
	private String codiceUnivoco;
	private String nomeAzienda;
	private String partitaIva;
	private int numeroProgressivo=-1;
	private int anno;
	private LocalDate dataEmissione;
	private LocalDate dataScadenza;
	private Money importo;
	private String note;
	private String stato="nessuno";
	private String user="non registrato";
	private File pdfFile=null;
	private ArrayList<PagamentoParziale> pagamentiParziali;
	
	public Fattura(String partitaIva,String nomeAzienda,
			 String codiceUnivoco,LocalDate dataEmissione, LocalDate dataScadenza, Money importo,
			 String note) throws MalformedDataException{
		super();
		if(!partitaIva.matches("[0-9]{11}")) throw new MalformedDataException("Partita Iva non valida");
		if(!codiceUnivoco.matches("^[0-9]*$")) throw new MalformedDataException("Partita Iva non valida");
		if(nomeAzienda.equals("")) throw new MalformedDataException("Nome Azienda non valido");
		this.codiceUnivoco=codiceUnivoco;
		this.anno = dataEmissione.getYear();
		this.dataEmissione = dataEmissione;
		this.dataScadenza = dataScadenza;
		this.note = note;
		this.partitaIva=partitaIva;
		this.nomeAzienda=nomeAzienda.toLowerCase().trim();
		this.importo=importo;
	}
	
	
	
	



	public File getPdfFile() {
		return pdfFile;
	}







	public void setPdfFile(File pdfFile) {
		this.pdfFile = pdfFile;
	}







	public String getNrFattura() {
		return numeroProgressivo+"\\"
				+anno;
	}
	
	public Money getImportoRimanente() {
		Money importoRimanente=importo;
		for(PagamentoParziale pp:pagamentiParziali) {
			importoRimanente=importoRimanente.minus(pp.getImporto());
		}
		return importoRimanente;
	}


	public String getCodiceUnivoco() {
		return codiceUnivoco;
	}


	public void setCodiceUnivoco(String codiceUnivoco) {
		this.codiceUnivoco = codiceUnivoco;
	}


	public String getNomeAzienda() {
		return nomeAzienda;
	}


	public void setNomeAzienda(String nomeAzienda) {
		this.nomeAzienda = nomeAzienda;
	}


	public String getPartitaIva() {
		return partitaIva;
	}


	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}


	public int getNumeroProgressivo() {
		return numeroProgressivo;
	}


	public void setNumeroProgressivo(int numeroProgressivo) {
		this.numeroProgressivo = numeroProgressivo;
	}


	public int getAnno() {
		return anno;
	}


	public void setAnno(int anno) {
		this.anno = anno;
	}


	public LocalDate getDataEmissione() {
		return dataEmissione;
	}


	public void setDataEmissione(LocalDate dataEmissione) {
		this.dataEmissione = dataEmissione;
	}


	public LocalDate getDataScadenza() {
		return dataScadenza;
	}


	public void setDataScadenza(LocalDate dataScadenza) {
		this.dataScadenza = dataScadenza;
	}


	public Money getImporto() {
		return importo;
	}


	public void setImporto(Money importo) {
		this.importo = importo;
	}


	public String getNote() {
		return note;
	}


	public void setNote(String note) {
		this.note = note;
	}


	public String getUser() {
		return user;
	}


	public void setUser(String user) {
		this.user = user;
	}
	

	public String getStato() {
		return stato;
	}


	public void setStato(String stato) {
		this.stato = stato;
	}


	public ArrayList<PagamentoParziale> getPagamentiParziali() {
		return pagamentiParziali;
	}
	
	public PagamentoParziale getPagamentoParziale(int indicePagamento) {
		return pagamentiParziali.stream().filter(x -> x.getIndicepagamento()==indicePagamento).findFirst().orElse(null);
	}


	public void setPagamentiParziali(ArrayList<PagamentoParziale> pagamentiParziali) {
		if(pagamentiParziali.size()==0) return;
		this.pagamentiParziali = pagamentiParziali;
		if(this.getImportoRimanente().isZero()) stato="completato";
		else stato="parziale";
	}
	
	
	
	
	


	

}
