package main.java.main.java.print;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.ObservableList;
import main.java.main.java.hibernate.entities.BankTransaction;
import main.java.main.java.hibernate.service.service.BankService;
import main.java.main.java.hibernate.service.serviceImpl.BankServiceImpl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;

public class PrintBankStatement {
	public static String filename = "D:\\Software\\Prints\\BankStatement.pdf";
	private static Font font = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
	//private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
	//private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
	//private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
	private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
	private static Font smallfont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
	private BankService bankService;
	ObservableList<BankTransaction> list;
	private LocalDate start,end;
	float[] columnWidths = new float[]{10f, 40f, 10f, 10f,10f,10f};
	public PrintBankStatement(ObservableList<BankTransaction>list,LocalDate start,LocalDate end)
	{
		try {
			this.list = list;
			this.start=start;
			this.end=end;
		if(list==null ||list.size()==0)
		{
			return;
		}
		bankService = new BankServiceImpl();
		float left = 30;
        float right = 0;
        float top = 20;
        float bottom = 0;
        Document doc = new Document(PageSize.A4,left,right,top,bottom);
	
		PdfWriter.getInstance(doc, new FileOutputStream(filename));
		doc.open();
		addContent(doc);
		doc.close();
		}catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
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

			c1 = new PdfPCell(new Paragraph("Bank Statement", font));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(0);
			c1.setBorder(PdfPCell.NO_BORDER);
			table.addCell(c1);   
		    
			doc.add(table);
			float[] columnWidths = new float[]{10f,15f,40f, 15f,15f};
			PdfPTable data = new PdfPTable(5);
			data.setWidths(columnWidths);
			c1 = new PdfPCell(new Paragraph("Bank Name : "+bankService.getBankById(list.get(0).getBankid()).getBankname(), smallBold));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(0);
			c1.setColspan(6);
			c1.setBorder(PdfPCell.NO_BORDER);
			data.addCell(c1);
			String d="";
			if(start==null)
				d="All Dates";
			else
				d=""+start+" To "+end;
			c1 = new PdfPCell(new Paragraph("Report Date : "+d, smallBold));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(0);
			c1.setColspan(6);
			c1.setBorder(PdfPCell.NO_BORDER);
			data.addCell(c1);
				
			
			addHeader(data);
			//adding Data
			double credit=0,debit=0;
			int i=0;
			for(BankTransaction tr:list)
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
				
				c1 = new PdfPCell(new Paragraph(""+tr.getDebit(),smallfont));
				c1.setHorizontalAlignment(Element.ALIGN_LEFT);
				c1.setBorder(0);
				c1.setBorder(PdfPCell.BOX);
				data.addCell(c1);
				credit = credit+tr.getDebit();
				
				c1 = new PdfPCell(new Paragraph(""+tr.getCredit(),smallfont));
				c1.setHorizontalAlignment(Element.ALIGN_LEFT);
				c1.setBorder(0);
				c1.setBorder(PdfPCell.BOX);
				data.addCell(c1);
				debit=debit+tr.getCredit();
			}
			c1 = new PdfPCell(new Paragraph("     ",smallfont));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(0);
			c1.setColspan(2);			
			c1.setBorder(PdfPCell.BOX);
			data.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Total",smallBold));
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setBorder(PdfPCell.BOX);
			data.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph(""+credit,smallBold));
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setBorder(PdfPCell.BOX);
			data.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph(""+debit,smallBold));
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setBorder(PdfPCell.BOX);
			data.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("     ",smallfont));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(0);
			c1.setColspan(2);			
			c1.setBorder(PdfPCell.BOX);
			data.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Total Balance",smallBold));
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setBorder(PdfPCell.BOX);
			data.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("",smallBold));
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setBorder(PdfPCell.BOX);
			data.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph(""+bankService.getBankBalance(list.get(0).getBankid()),smallBold));
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setBorder(PdfPCell.BOX);
			data.addCell(c1);
			
			//table.addCell(data);
			doc.add(data);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	void addHeader(PdfPTable data)
	{
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
		
		c1 = new PdfPCell(new Paragraph("Credit",smallBold));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBorder(0);		
		c1.setBorder(PdfPCell.BOX);
		data.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph("Debit", smallBold));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBorder(0);		
		c1.setBorder(PdfPCell.BOX);
		data.addCell(c1);
		
	}
}
