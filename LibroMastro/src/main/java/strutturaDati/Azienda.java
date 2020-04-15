package strutturaDati;
import java.util.ArrayList;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;


public class Azienda {
	private String nomeAzienda;
	private String partitaIva;
	ArrayList<Fattura> fatture;
	private String user="non registrato";
	
	public Azienda() {
		
	}
	public Azienda(String nomeAzienda, String partitaIva) throws MalformedDataException {
		super();
		if(!partitaIva.matches("[0-9]{11}")) throw new MalformedDataException("Partita Iva non valida"); 
		if(nomeAzienda.equals("")) throw new MalformedDataException("Nome azienda non valido");
		this.nomeAzienda = nomeAzienda.toLowerCase().trim();
		this.partitaIva = partitaIva;

	}
	public Money getImportoRimanente() {
		Money importoRimanente=Money.of(CurrencyUnit.EUR, 0);
		for(Fattura fattura:fatture) {
			importoRimanente=importoRimanente.plus(fattura.getImportoRimanente());
		}
		return importoRimanente;
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
	public ArrayList<Fattura> getFatture() {
		return fatture;
	}
	public void setFatture(ArrayList<Fattura> fatture) {
		this.fatture = fatture;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	

	
	
}

