package storage;

import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import strutturaDati.Azienda;
import strutturaDati.Fattura;
import strutturaDati.PagamentoParziale;

public class EstrattoContoBuilder {
	private Azienda azienda;
	private LocalDate dataInizio;
	private LocalDate dataFine;
	private File dest;

	
	public EstrattoContoBuilder(Azienda azienda, LocalDate dataInizio, LocalDate dataFine,File dest) {
		super();
		this.azienda = azienda;
		this.dataInizio = dataInizio;
		this.dataFine = dataFine;
		this.dest=dest;
	}

	public void prova() throws FileNotFoundException {
		PdfDocument pdfDoc=new PdfDocument(new PdfWriter(dest.getAbsolutePath()));
		Document doc=new Document(pdfDoc);
		Table table=new Table(UnitValue.createPercentArray(8)).useAllAvailableWidth();
		
		 for (int i = 0; i < 4; i++) {
	            table.addCell("hi");
	            
	        }
		 doc.add(table);
		 doc.close();
	}
	
	public void scriviPdf() throws FileNotFoundException {
		PdfDocument pdfDoc=new PdfDocument(new PdfWriter(dest));
		Document doc=new Document(pdfDoc);
		Table table=new Table(8);
		table.setFontSize(11);
		
		doc.add(new Paragraph("Estratto Conto di "+azienda.getNomeAzienda()).setFontSize(17).setBold().setTextAlignment(TextAlignment.CENTER));
		doc.add(new Paragraph("dal: "+dataInizio.toString()+" al: "+dataFine.toString()).setFontSize(12).setBold().setTextAlignment(TextAlignment.CENTER));
		String[] headers={"Codice fattura","Importo","Data emissione","Data Scadenza","Importo PP","Data PP","Tipo Pagamento PP","Importo Rimanente"};
		for(int i=0;i<8;i++) {
			Cell cell=new Cell().add(new Paragraph(headers[i])).setBackgroundColor(ColorConstants.ORANGE);
			table.addCell(cell);
		}
		for(Fattura fattura:azienda.getFatture()) {
			table.addCell(fattura.getCodiceUnivoco());
			table.addCell("€"+fattura.getImporto().getAmount().toString());
			table.addCell(fattura.getDataEmissione().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			table.addCell(fattura.getDataEmissione().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			table.addCell("");
			table.addCell("");
			table.addCell("");
			table.addCell("€"+fattura.getImportoRimanente().getAmount().toString());
			for(PagamentoParziale pp: fattura.getPagamentiParziali()) {
				table.addCell("");
				table.addCell("");
				table.addCell("");
				table.addCell("");
				table.addCell("€"+pp.getImporto().getAmount().toString());
				table.addCell(pp.getDataPagamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
				table.addCell(pp.getTipoPagamento().toString());
				table.addCell("");
			}
		}
		
		doc.add(table);
		doc.add(new Paragraph("\nSaldo: "+azienda.getImportoRimanente()).setBold().setTextAlignment(TextAlignment.RIGHT));
		doc.close();
		
		if(Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().open(dest);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
