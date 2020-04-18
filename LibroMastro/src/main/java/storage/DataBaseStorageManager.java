package storage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.StringJoiner;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import strutturaDati.Azienda;
import strutturaDati.Fattura;
import strutturaDati.MalformedDataException;
import strutturaDati.PagamentoParziale;
import strutturaDati.TipoPagamento;

public class DataBaseStorageManager{
	private String user;
	private String password;
	private String url;

	public void initStorage(String user, String password, String url) throws SQLException, ClassNotFoundException{
		this.user=user;
		this.password=password;
		this.url=url;
		Connection con=null;
		try {
			Class.forName("org.postgresql.Driver");
			con=DriverManager.getConnection(url, user, password);
			con.close();
		}catch(SQLException |ClassNotFoundException  e) {
			throw e;
		}finally {
			if(con!=null) con.close();
		}	
	}
	
	public void inserisciAzienda(Azienda azienda)throws SQLException,MalformedDataException{
		Connection con=DriverManager.getConnection(url, user, password);
		Statement stm=null;
		try {
			stm=con.createStatement();
			String stmString="insert into aziende(nomeAzienda,partitaIVA) values('"+azienda.getNomeAzienda().toLowerCase().trim()+"','"+azienda.getPartitaIva()+"')";
			stm.execute(stmString);
			System.out.println("Inserita nel Database l'azienda "+azienda.getNomeAzienda()+"-"+azienda.getPartitaIva()
			+" operazione effettuata da "+user);
		} catch (SQLException e) {
			throw e;
		}finally {
			stm.close();
			if(con!=null) con.close();
		}	
	}
	
	public void inserisciFattura(Fattura fattura) throws SQLException, FileNotFoundException{
		Connection con=DriverManager.getConnection(url, user, password);
		PreparedStatement ps=null;
		try {
			String stmString="insert into Fatture(partitaiva,nomeAzienda,codiceunivoco,dataEmissione,dataScadenza,importo,note,pdfFile) ";
			StringJoiner sj=new StringJoiner("','","values('","',?)");
			
			sj.add(fattura.getPartitaIva());
			sj.add(fattura.getNomeAzienda());
			sj.add(fattura.getCodiceUnivoco());
			sj.add(fattura.getDataEmissione().toString());
			sj.add(fattura.getDataScadenza().toString());
			sj.add(fattura.getImporto().getAmount().toString());
			sj.add(fattura.getNote());
			stmString+=sj.toString();
			ps=con.prepareStatement(stmString);
			if(fattura.getPdfFile()==null) ps.setNull(1, java.sql.Types.BINARY);
			else ps.setBinaryStream(1, new FileInputStream(fattura.getPdfFile()));
			ps.execute();
			System.out.println("Inserita nel Database la fattura  "+fattura.getCodiceUnivoco()
			+" operazione effettuata da "+user);
		}catch(SQLException | FileNotFoundException e) {
			throw e;
		}finally {
				ps.close();
				if(con!=null) con.close();
		}
	}
	
	public void inserisciPagamentoParziale(PagamentoParziale pp) throws SQLException,MalformedDataException {
		Connection con=DriverManager.getConnection(url, user, password);
		Statement stm=null;
		try {
			String stmString="insert into PagamentiParziali(partitaiva,codiceunivoco,dataPagamento,tipoPagamento,importo) ";
			StringJoiner sj=new StringJoiner("','","values('","')");
			sj.add(pp.getPartitaIva());
			sj.add(pp.getCodiceUnivoco());
			sj.add(pp.getDataPagamento().toString());
			sj.add(pp.getTipoPagamento().toString());
			sj.add(pp.getImporto().getAmount().toString());
			stmString+=sj.toString();
			stm=con.createStatement();
			stm.execute(stmString);
			System.out.println("Inserita nel Database il pagamento parziale di "+pp.getImporto()+",data "+pp.getDataPagamento()
			+" della fattura "+pp.getCodiceUnivoco()+" operazione effettuata da "+user);

		}catch(SQLException e) {
			throw e;
		}finally {
			stm.close();
			if(con!=null) con.close();
		}
		
	}
	
	public ArrayList<PagamentoParziale> leggiPagamentiParziali(String codiceUnivoco)throws SQLException,MalformedDataException{
		Statement stm=null;
		Connection con=DriverManager.getConnection(url, user, password);
		ArrayList<PagamentoParziale> pagamentiParziali=new ArrayList<PagamentoParziale>();
		try {
			String stmString="select distinct on (operazionieffettuate.indiceOggetto) pagamentiparziali.*,operazionieffettuate.utente from pagamentiParziali,operazionieffettuate where codiceunivoco= "+"'"+codiceUnivoco+"'"
					+ " and operazionieffettuate.nomeTabella='pagamentiparziali' and operazionieffettuate.indiceoggetto=pagamentiparziali.indiceoggetto order by "
					+ " operazionieffettuate.indiceOggetto, operazionieffettuate.tstamp desc";
			stm=con.createStatement();
			stm.execute(stmString);
			ResultSet rs=stm.getResultSet();
			while(rs.next()) {
				PagamentoParziale pp=new PagamentoParziale(rs.getString("codiceunivoco"), Money.of(CurrencyUnit.EUR,rs.getDouble("importo")), 
						LocalDate.parse(rs.getString("dataPagamento")),
						TipoPagamento.valueOf(rs.getString("tipoPagamento")),rs.getString("partitaiva"));
				pp.setIndicepagamento(rs.getInt("indiceOggetto"));
				pp.setUser(rs.getString("utente"));
			pagamentiParziali.add(pp);
			}
		}catch(SQLException e) {
			throw e;
		}finally {
			stm.close();
			if(con!=null) con.close();
		}
		System.out.println("Caricati "+pagamentiParziali.size()+" pagamenti parziali");
		return pagamentiParziali;
	}
	

	
	public ArrayList<Fattura> leggiFatture(String nomeAziendaPiece,String partitaIvaHead) throws SQLException , MalformedDataException{
		Connection con=DriverManager.getConnection(url, user, password);
		Statement stm=null;
		ArrayList<Fattura> fatture=new ArrayList<Fattura>();
		try {
			String stmString="select distinct on(fatture.indiceoggetto) fatture.*, operazionieffettuate.utente "
					+ "from fatture,operazionieffettuate where fatture.nomeAzienda like"+"'%"+nomeAziendaPiece+"%'"+"and fatture.partitaiva like "+"'"+partitaIvaHead+"%' "
					+ "and operazionieffettuate.nometabella='fatture' and operazionieffettuate.indiceOggetto=fatture.indiceOggetto order by indiceOggetto, operazionieffettuate.tstamp desc ";
			System.out.println(stmString);
			stm=con.createStatement();
			stm.execute(stmString);
			ResultSet rs=stm.getResultSet();
			while(rs.next()) {
				Fattura fattura=new Fattura(rs.getString("partitaIva"),rs.getString("nomeAzienda"),rs.getString("codiceUnivoco"),
						LocalDate.parse(rs.getString("dataEmissione")),LocalDate.parse(rs.getString("dataScadenza")), Money.of(CurrencyUnit.EUR,rs.getDouble("importo")),
						rs.getString("note"));
				fattura.setUser(rs.getString("utente"));
				fattura.setNumeroProgressivo( rs.getInt("indiceOggetto"));
				fatture.add(fattura);
			}
			System.out.println("Caricate "+fatture.size()+" fatture");
			for(Fattura fattura:fatture) {
				ArrayList<PagamentoParziale> pagamentiParziali=leggiPagamentiParziali(fattura.getCodiceUnivoco());
				fattura.setPagamentiParziali(pagamentiParziali);
			}
		}catch(SQLException | MalformedDataException e) {
			throw e;
		}finally {
			stm.close();	
			if(con!=null) con.close();
		}
		return fatture;
	}
	public ArrayList<Fattura> leggiFattureFiltroTempo(String nomeAziendaPiece,String partitaIvaHead,LocalDate dataInizio,
			LocalDate dataFine) throws SQLException , MalformedDataException{
		Connection con=DriverManager.getConnection(url, user, password);
		Statement stm=null;
		ArrayList<Fattura> fatture=new ArrayList<Fattura>();
		try {
			String stmString="select * from fatture where nomeAzienda like"+"'%"+nomeAziendaPiece+"%'"+"and partitaiva like "+"'"+partitaIvaHead+"%'"
					+ "and dataemissione>='"+dataInizio.toString()+"' and dataemissione<='"+dataFine+"'";
			stm=con.createStatement();
			stm.execute(stmString);
			ResultSet rs=stm.getResultSet();
			while(rs.next()) {
				Fattura fattura=new Fattura(rs.getString("partitaIva"),rs.getString("nomeAzienda"),rs.getString("codiceUnivoco"),
						LocalDate.parse(rs.getString("dataEmissione")),LocalDate.parse(rs.getString("dataScadenza")), Money.of(CurrencyUnit.EUR,rs.getDouble("importo")),
						rs.getString("note"));
				fattura.setNumeroProgressivo( rs.getInt("numeroProgressivo"));
				fattura.setStato(rs.getString("stato"));
				fatture.add(fattura);
			}
			System.out.println("Caricate "+fatture.size()+" fatture");
			for(Fattura fattura:fatture) {
				ArrayList<PagamentoParziale> pagamentiParziali=leggiPagamentiParziali(fattura.getCodiceUnivoco());
				fattura.setPagamentiParziali(pagamentiParziali);
			}
		}catch(SQLException | MalformedDataException e) {
			throw e;
		}finally {
			stm.close();	
			if(con!=null) con.close();
		}
		return fatture;
	}
	
	public byte[] getPdfFileContent(String codiceUnivoco,String partitaIva) throws SQLException {
		Connection con=DriverManager.getConnection(url, user, password);
		Statement stm;
		byte[] pdfFileContent=null;
		String stmString="select pdfFile from fatture where (codiceunivoco,partitaiva)=('"+codiceUnivoco+"','"+partitaIva+"')";
		stm=con.createStatement();
		stm.execute(stmString);
		ResultSet rs=stm.getResultSet();
		if(rs.next()) {
			pdfFileContent=rs.getBytes("pdfFile");
		}
		if(pdfFileContent!=null) System.out.println("Caricato contenuto pdf, lunghezza "+pdfFileContent.length);
		else throw new SQLException("Nessun file Trovato");
		return pdfFileContent;
		
	}
	
	public Azienda leggiAzienda(String partitaIva,String nomeAzienda) throws SQLException, MalformedDataException{
		Connection con=DriverManager.getConnection(url, user, password);
		Statement stm=null;
		Azienda azienda=null;
		try {
			con=DriverManager.getConnection(url,user,password);
			String stmString="select * from aziende where partitaiva ='"+partitaIva+"' and nomeazienda = '"+nomeAzienda+"'";
			stm=con.createStatement();
			stm.execute(stmString);
			ResultSet rs=stm.getResultSet();
			if(!rs.next()) throw new SQLException("azienda non trovata");
	        azienda=new Azienda(rs.getString("nomeAzienda"),rs.getString("partitaIva"));	
		}catch(SQLException | MalformedDataException e) {
			throw e;
		}finally {
			stm.close();
			if(con!=null) con.close();
		}
		System.out.println("Caricata azienda "+azienda.getNomeAzienda());
		return azienda;
	}
	
	public ArrayList<Azienda> leggiAziende(String partitaIvaHead,String nomeAziendaPiece) throws SQLException,MalformedDataException{
		Connection con=DriverManager.getConnection(url, user, password);
		Statement stm=null;
		ArrayList<Azienda> aziende=new ArrayList<Azienda>();
		try {
			con=DriverManager.getConnection(url,user,password);
			String stmString="select * from aziende where partitaiva like '"+partitaIvaHead+"%' and nomeazienda like '%"+nomeAziendaPiece+"%'";
			stm=con.createStatement();
			stm.execute(stmString);
			ResultSet rs=stm.getResultSet();
			if(!rs.next()) throw new SQLException("nessuna azienda trovata");
			do {
		        aziende.add(new Azienda(rs.getString("nomeAzienda"),rs.getString("partitaIva")));	
			}while(rs.next());
		}catch(SQLException | MalformedDataException e) {
			throw e;
		}finally {
			stm.close();
			if(con!=null) con.close();
		}
		System.out.println("Caricate "+aziende.size()+" aziende");
		return aziende;
	}
	
	
	public boolean checkIfAlreadyExistPP(LocalDate dataPagamento,Money importo) throws SQLException {
		Connection con=DriverManager.getConnection(url,user,password);
		Statement stm=null;
		String smtString="select count(*) from pagamentiparziali where (datapagamento,importo)=('"+dataPagamento.toString()+"',"+
		importo.getAmount().toString()+")";
		stm=con.createStatement();
		stm.execute(smtString);
		ResultSet rs=stm.getResultSet();
		rs.next();
		if(rs.getInt("count")!=0) return true;
		return false;
	}
	
	public void scriviEstrattoConto(String nomeAzienda,String partitaIva,LocalDate dataInizio,LocalDate dataFine) throws SQLException, MalformedDataException, FileNotFoundException {
		Azienda azienda;
		ArrayList<Fattura> fatture;
		try {
			azienda=leggiAzienda(partitaIva,nomeAzienda);
			fatture=leggiFattureFiltroTempo(nomeAzienda, partitaIva, dataInizio, dataFine);
			azienda.setFatture(fatture);
			new EstrattoContoBuilder(azienda, dataInizio, dataFine).scriviPdf();
		}catch(SQLException e) {
			throw e;
		}
	}
	
	public void modificaFattura(Fattura fatturaOriginale,Fattura fatturaModificata) throws Exception {
		Connection con=DriverManager.getConnection(url,user,password);
		PreparedStatement ps=null;
		String stmString;
		try {
			if(fatturaModificata.getPdfFile()==null) {
				stmString="update fatture set (partitaiva,nomeazienda,codiceunivoco,dataemissione,datascadenza,importo,note)=";
				StringJoiner sj=new StringJoiner("','", "('", "')");
				sj.add(fatturaModificata.getPartitaIva());
				sj.add(fatturaModificata.getNomeAzienda());
				sj.add(fatturaModificata.getCodiceUnivoco());
				sj.add(fatturaModificata.getDataEmissione().toString());
				sj.add(fatturaModificata.getDataScadenza().toString());
				sj.add(fatturaModificata.getImporto().getAmount().toString());
				sj.add(fatturaModificata.getNote());
				stmString+=sj.toString()+"where (codiceUnivoco,partitaIva)=('"+fatturaOriginale.getCodiceUnivoco()+"','"+fatturaOriginale.getPartitaIva()+"')";
				ps=con.prepareStatement(stmString);
			}
			else {
				stmString="update fatture set (partitaiva,nomeazienda,codiceunivoco,dataemissione,datascadenza,importo,note,pdfFile)=";
				StringJoiner sj=new StringJoiner("','", "('", "',?)");
				sj.add(fatturaModificata.getPartitaIva());
				sj.add(fatturaModificata.getNomeAzienda());
				sj.add(fatturaModificata.getCodiceUnivoco());
				sj.add(fatturaModificata.getDataEmissione().toString());
				sj.add(fatturaModificata.getDataScadenza().toString());
				sj.add(fatturaModificata.getImporto().getAmount().toString());
				sj.add(fatturaModificata.getNote());
				stmString+=sj.toString()+"where (codiceUnivoco,partitaIva)=('"+fatturaOriginale.getCodiceUnivoco()+"','"+fatturaOriginale.getPartitaIva()+"')";
				ps=con.prepareStatement(stmString);
				ps.setBinaryStream(1, new FileInputStream(fatturaModificata.getPdfFile()));
			}
			ps.execute();
			System.out.println("Modificata fattura "+fatturaOriginale.getCodiceUnivoco()+" emessa da "+fatturaOriginale.getNomeAzienda());
		}catch(SQLException | FileNotFoundException e) {
			throw e;
		}finally {
			if(ps!=null) ps.close();
			con.close();
		}
	}
	
	public void modificaPagamentoParziale(PagamentoParziale ppOriginale,PagamentoParziale ppModificato) throws SQLException {
		Connection con=DriverManager.getConnection(url, user, password);
		Statement stm=null;
		try {
			String stmString="update pagamentiparziali set (dataPagamento,importo,tipoPagamento)=";
			StringJoiner sj=new StringJoiner("','","('","')");
			sj.add(ppModificato.getDataPagamento().toString());
			sj.add(ppModificato.getImporto().getAmount().toString());
			sj.add(ppModificato.getTipoPagamento().toString());
			stmString+=sj.toString()+" where indiceoggetto="+ppOriginale.getIndicepagamento();
			stm=con.createStatement();
			stm.execute(stmString);
			System.out.println("Modificato pagamento parziale di indice "+ppOriginale.getIndicepagamento());
		}catch(Exception e) {
			throw e;
		}finally {
			con.close();
			if(stm!=null) stm.close();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
