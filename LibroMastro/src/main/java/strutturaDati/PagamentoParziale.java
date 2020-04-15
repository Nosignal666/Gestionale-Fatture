package strutturaDati;

import java.time.LocalDate;
import org.joda.money.Money;

public class PagamentoParziale {
	
	private LocalDate dataPagamento;
	private Money importo;
	private TipoPagamento tipoPagamento;
	private String user="non registrato";
	private String codiceUnivoco;
	private int indicepagamento=-1;
	private String partitaIva;

	public PagamentoParziale(String codiceUnivoco, Money importo,LocalDate dataPagamento, TipoPagamento tipoPagamento,String partitaIva) {
		super();
		this.partitaIva=partitaIva;
		this.dataPagamento = dataPagamento;
		this.importo = importo;
		this.tipoPagamento=tipoPagamento;
		this.codiceUnivoco=codiceUnivoco;
	}

	public int getIndicepagamento() {
		return indicepagamento;
	}

	public void setIndicepagamento(int indicepagamento) {
		this.indicepagamento = indicepagamento;
	}

	public LocalDate getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(LocalDate dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public Money getImporto() {
		return importo;
	}

	public void setImporto(Money importo) {
		this.importo = importo;
	}

	public TipoPagamento getTipoPagamento() {
		return tipoPagamento;
	}

	public void setTipoPagamento(TipoPagamento tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getCodiceUnivoco() {
		return codiceUnivoco;
	}

	public void setCodiceUnivoco(String codiceUnivoco) {
		this.codiceUnivoco = codiceUnivoco;
	}

	public String getPartitaIva() {
		return partitaIva;
	}

	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}
	
	
	
	
}
