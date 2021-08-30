package main.java.main.java.print;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.scene.control.Alert;
import main.java.main.java.hibernate.entities.PurchaseParty;
import main.java.main.java.hibernate.reportEntity.PurchaseStatementPojo;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;

public class PurchaseStatementPrint {
	public static String filename = "D:\\Software\\Prints\\PurchaseStatement.pdf";
	private static Font font = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
	private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
	private static Font smallfont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
	
	private List<PurchaseStatementPojo> list;
	private String partyName,address;
	private LocalDate fromDate,toDate;
	public PurchaseStatementPrint(List<PurchaseStatementPojo> list, LocalDate fromDate, LocalDate toDate, PurchaseParty party) {
		try {
			this.list=list;
			this.toDate = toDate;
			this.fromDate = fromDate;
			this.partyName = party.getName();
			this.address = party.getAddress();
			float left = 30;
			float right = 0;
			float top = 20;
			float bottom = 0;
			Document doc = new Document(PageSize.A4, left, right, top, bottom);
			PdfWriter.getInstance(doc, new FileOutputStream(filename));
			doc.open();
			addContent(doc);
			doc.close();
			System.out.println("Write Done");
		} catch (Exception e) {

		}
	}

	private void addContent(Document doc) {
		try {
			PdfPTable table = new PdfPTable(1);

			PdfPCell c1 = new PdfPCell(new Paragraph("Yash Goat Farm & Seeds", font));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(0);
			c1.setBorder(PdfPCell.NO_BORDER);
			table.addCell(c1);

			c1 = new PdfPCell(new Paragraph("Purchase Statement", font));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(0);
			c1.setBorder(PdfPCell.NO_BORDER);
			table.addCell(c1);   
			
			doc.add(table);
			float[] columnWidths = new float[]{10f,15f,40f, 15f,15f,15f};
			PdfPTable data = new PdfPTable(6);
			data.setWidths(columnWidths);
			c1 = new PdfPCell(new Paragraph("Party Name : "+partyName, smallBold));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(0);
			c1.setColspan(6);
			c1.setBorder(PdfPCell.NO_BORDER);
			data.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Party Address : "+address, smallBold));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(0);
			c1.setColspan(6);
			c1.setBorder(PdfPCell.NO_BORDER);
			data.addCell(c1);
			
			String d="";
			if(fromDate==null)
				d="All Dates";
			else
				d=""+fromDate+" To "+toDate;
			c1 = new PdfPCell(new Paragraph("Report Date : "+d, smallBold));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(0);
			c1.setColspan(6);
			c1.setBorder(PdfPCell.NO_BORDER);
			data.addCell(c1);
			
			addHeader(data);
			double credit=0,debit=0,balance=0;
			int i=0;
			for(PurchaseStatementPojo tr:list)
			{
				c1 = new PdfPCell(new Paragraph(""+(++i), smallfont));
				c1.setHorizontalAlignment(Element.ALIGN_LEFT);
				c1.setBorder(0);
				c1.setBorder(PdfPCell.BOX);
				data.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(""+tr.getDate(), smallfont));
				c1.setHorizontalAlignment(Element.ALIGN_LEFT);
				c1.setBorder(0);
				c1.setBorder(PdfPCell.BOX);
				data.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(""+tr.getParticulars(), smallfont));
				c1.setHorizontalAlignment(Element.ALIGN_LEFT);
				c1.setBorder(0);
				c1.setBorder(PdfPCell.BOX);
				data.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(""+tr.getDebit(), smallfont));
				c1.setHorizontalAlignment(Element.ALIGN_LEFT);
				c1.setBorder(0);
				c1.setBorder(PdfPCell.BOX);
				data.addCell(c1);
				debit=debit+tr.getDebit();
				
				c1 = new PdfPCell(new Paragraph(""+tr.getCredit(), smallfont));
				c1.setHorizontalAlignment(Element.ALIGN_LEFT);
				c1.setBorder(0);
				c1.setBorder(PdfPCell.BOX);
				data.addCell(c1);
				credit=credit+tr.getCredit();
				
				c1 = new PdfPCell(new Paragraph(""+tr.getBalance(), smallfont));
				c1.setHorizontalAlignment(Element.ALIGN_LEFT);
				c1.setBorder(0);
				c1.setBorder(PdfPCell.BOX);
				data.addCell(c1);
				balance = tr.getBalance();
			}
			c1 = new PdfPCell(new Paragraph("Total", smallBold));
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setColspan(3);
			c1.setBorder(PdfPCell.BOX);
			data.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph(""+debit, smallBold));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(0);			
			c1.setBorder(PdfPCell.BOX);
			data.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph(""+credit, smallBold));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(0);			
			c1.setBorder(PdfPCell.BOX);
			data.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph(""+balance, smallBold));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(0);			
			c1.setBorder(PdfPCell.BOX);
			data.addCell(c1);
			
			
			
			
			doc.add(data);
			
		} catch (Exception e) {
			new Alert(Alert.AlertType.ERROR,e.getMessage()).showAndWait();
		}
		
	}

	private void addHeader(PdfPTable data) {
		PdfPCell c1 = new PdfPCell(new Paragraph("Sr.No", smallBold));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBorder(0);		
		c1.setBorder(PdfPCell.BOX);
		data.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph("Date", smallBold));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBorder(0);		
		c1.setBorder(PdfPCell.BOX);
		data.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph("Particulars", smallBold));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBorder(0);		
		c1.setBorder(PdfPCell.BOX);
		data.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph("Debit",smallBold));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBorder(0);		
		c1.setBorder(PdfPCell.BOX);
		data.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph("Dredit", smallBold));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBorder(0);		
		c1.setBorder(PdfPCell.BOX);
		data.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph("Balance", smallBold));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBorder(0);		
		c1.setBorder(PdfPCell.BOX);
		data.addCell(c1);
		
	}

	public static void main(String[] args) {

	}

}
