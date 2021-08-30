package main.java.main.java.print;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import main.java.main.java.hibernate.entities.PaymentReciept;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;

public class PaymentReceiptReportPrint {
	public static String filename = "D:\\Software\\Prints\\PaymentReceiptReport.pdf";
	private static Font font = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
	private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
	private static Font smallfont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
	private List<PaymentReciept> list;
	private LocalDate start,end;
	float[] columnWidths = new float[]{10f, 40f, 10f, 10f,10f,10f};
	public PaymentReceiptReportPrint(List<PaymentReciept>list,LocalDate start,LocalDate end)
	{
		try {
			this.list = list;
			this.start=start;
			this.end=end;
		if(list==null ||list.size()==0)
		{
			return;
		}
		//bankService = new BankServiceImpl(); 
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
			String imageFile = "D:\\Software\\Images\\Yash Bill Head.png";
			Image image = Image.getInstance(imageFile);
			
			
			PdfPCell c1 = new PdfPCell(image, true);
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(0);
			c1.setBorder(PdfPCell.BOTTOM);
			table.addCell(c1);

			c1 = new PdfPCell(new Paragraph("Payment Receipt Report", font));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(0);
			c1.setBorder(PdfPCell.NO_BORDER);
			table.addCell(c1);
			table.setWidthPercentage(90);
			doc.add(table);
			
			float[] columnWidths = new float[]{10f,15f,35f, 35f,15f};
			PdfPTable data = new PdfPTable(5);
			data.setWidths(columnWidths);
			
			String d="";
			if(start==null)
				d="All Dates";
			else
				d=""+start+" To "+end;
			c1 = new PdfPCell(new Paragraph("Report Duration : "+d, smallBold));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(0);
			c1.setColspan(3);
			c1.setBorder(PdfPCell.NO_BORDER);
			data.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Report Date : "+LocalDate.now(), smallBold));
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setColspan(2);
			c1.setBorder(PdfPCell.NO_BORDER);
			data.addCell(c1);
			
			addHeader(data);
			float total=0.0f;
			int i=0;
			for(PaymentReciept pay:list)
			{
				c1 = new PdfPCell(new Paragraph(""+(++i), smallfont));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBorder(0);
				c1.setBorder(PdfPCell.BOX);
				data.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(""+pay.getDate(), smallfont));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBorder(0);
				c1.setBorder(PdfPCell.BOX);
				data.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(""+pay.getNote(), smallfont));
				c1.setHorizontalAlignment(Element.ALIGN_LEFT);
				c1.setBorder(0);
				c1.setBorder(PdfPCell.BOX);
				data.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(""+pay.getBank().getBankname(), smallfont));
				c1.setHorizontalAlignment(Element.ALIGN_LEFT);
				c1.setBorder(0);
				c1.setBorder(PdfPCell.BOX);
				data.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(""+pay.getAmount(), smallfont));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBorder(0);
				c1.setBorder(PdfPCell.BOX);
				data.addCell(c1);
				total +=pay.getAmount(); 
			}
			c1 = new PdfPCell(new Paragraph("Total", smallBold));
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(0);
			c1.setColspan(4);
			c1.setBorder(PdfPCell.BOX);
			data.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph(""+total, smallBold));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(0);
			
			c1.setBorder(PdfPCell.BOX);
			data.addCell(c1);
			
			
			data.setWidthPercentage(90);
			doc.add(data);

		} catch (Exception e) {
			e.printStackTrace();
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
		
		c1 = new PdfPCell(new Paragraph("Payment Note", smallBold));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBorder(0);		
		c1.setBorder(PdfPCell.BOX);
		data.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph("Bank Name", smallBold));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBorder(0);		
		c1.setBorder(PdfPCell.BOX);
		data.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph("Amount",smallBold));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBorder(0);		
		c1.setBorder(PdfPCell.BOX);
		data.addCell(c1);		
	}
}
