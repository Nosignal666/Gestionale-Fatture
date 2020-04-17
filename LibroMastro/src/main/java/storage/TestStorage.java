package storage;


import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import strutturaDati.Azienda;
import strutturaDati.Fattura;
import strutturaDati.PagamentoParziale;
import strutturaDati.TipoPagamento;


public class TestStorage {

	public static void main(String[] args) {
		
		/*
		LocalDate date=LocalDate.now();
		DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String text=date.format(formatter);
		LocalDate date1=LocalDate.parse(text, formatter);
		System.out.println(date1);
		*/
		CurrencyUnit eur=CurrencyUnit.EUR;
		try {
			
			Fattura fattura1 = new Fattura("12345678901","Lavanderia Planet","9999",LocalDate.of(2020,3,26),
					LocalDate.of(2020,4,1),Money.of(eur, 666.97),"fattura iniziale");
			fattura1.setPdfFile(new File("C:\\Users\\Gamma.Academy\\Desktop\\EC-lavanderia planet.pdf"));
			Fattura fattura1mod = new Fattura("12345678901","Lavanderia Planet","0123",LocalDate.of(2020,3,26),
					LocalDate.of(2020,4,1),Money.of(eur, 999),"fattura iniziale");
			Fattura fattura2=new Fattura("12345678901","Lavanderia Planet","0124", LocalDate.of(2020,3,27),
					LocalDate.of(2020,4,2),Money.of(eur, 0.03), "seconda fattura");
			Fattura fattura3 = new Fattura("12345678901","Lavanderia Planet","0125",LocalDate.of(2020,3,26),
					LocalDate.of(2020,4,1),Money.of(eur, 1000),"fattura iniziale");
			Fattura fattura4 = new Fattura("12345678901","Lavanderia Planet","0126",LocalDate.of(2020,3,26),
					LocalDate.of(2020,4,1),Money.of(eur, 1000),"fattura iniziale");
		
		    PagamentoParziale pp1=new PagamentoParziale("0123",Money.of(eur,0.97), LocalDate.now(), TipoPagamento.Bonifico,"12345678901");
			PagamentoParziale pp2=new PagamentoParziale("0124",Money.of(eur,0.02), LocalDate.now(), TipoPagamento.Bonifico,"12345678901");
			PagamentoParziale pp3=new PagamentoParziale("0123",Money.of(eur,666),  LocalDate.now(), TipoPagamento.Bonifico,"12345678901");
			PagamentoParziale pp4=new PagamentoParziale("0124",Money.of(eur,0.01), LocalDate.now(), TipoPagamento.Bonifico,"12345678901");    
			                                                                         
			DataBaseStorageManager dbsm=new DataBaseStorageManager();
			dbsm.initStorage("pasquale_moretti", "gidomico", "jdbc:postgresql://localhost:5432/Fatture");
			//dbsm.scriviEstrattoConto("lavanderia planet", "12345678901", LocalDate.parse("1999-01-01"), LocalDate.parse("3000-01-01"));
			
			//dbsm.inserisciAzienda(new Azienda("Lavanderia Planet","12345678901"));
			dbsm.inserisciFattura(fattura1);
			//dbsm.inserisciFattura(fattura2);
			//dbsm.inserisciFattura(fattura3);
			//dbsm.inserisciPagamentoParziale(pp1);
			//dbsm.inserisciPagamentoParziale(pp2);
			//dbsm.inserisciPagamentoParziale(pp3);
			//dbsm.inserisciPagamentoParziale(pp4);
			//dbsm.inserisciFattura(fattura4);
			////dbsm.updateFattura(fattura1mod, fattura1);
			
			

			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}

}
