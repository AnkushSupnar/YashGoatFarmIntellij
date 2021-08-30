package main.java.main.java.print;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import main.java.main.java.hibernate.entities.PaymentReciept;
import main.java.main.java.hibernate.service.service.PaymentRecieptService;
import main.java.main.java.hibernate.service.serviceImpl.PaymentRecieptServiceImpl;

import java.io.FileOutputStream;

public class PrintPaymentReceipt {
	public static String filename = "D:\\Software\\Prints\\receipt.pdf";
	private static Font font = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
	private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
	private static Font smallfont = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL);
	private PaymentRecieptService service;
	private PaymentReciept receipt;
	private long id;
	float[] columnWidths = new float[]{10f, 40f, 10f, 10f,10f,10f};
	public PrintPaymentReceipt(long id)
	{
		try {
			this.id=id;
			service = new PaymentRecieptServiceImpl();
			receipt = service.getPaymentRecieptById(id);
			float left = 30;
	        float right = 0;
	        float top = 20;
	        float bottom = 0;
	        Document doc = new Document(PageSize.A4,left,right,top,bottom);
		
			PdfWriter.getInstance(doc, new FileOutputStream(filename));
			doc.open();
			addContent(doc);
			doc.close();
		System.out.println("Write done");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void addContent(Document doc) {
		try {
			PdfPTable table = new PdfPTable(1);
			 PdfPCell c1 = new PdfPCell(new Paragraph("Yash Goat Farm & Seeds",font));
		        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		        c1.setBorder(0);
		        c1.setBorder(PdfPCell.NO_BORDER);		     
		        table.addCell(c1);
		        
		        c1 = new PdfPCell(new Paragraph("At/Post-Fattepur, Tal-Newasa",smallBold));
		        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		        c1.setBorder(PdfPCell.NO_BORDER);
		        table.addCell(c1);
		      
		        c1 = new PdfPCell(new Paragraph("Dist Ahmednagar, State-Maharashtra 414105",smallBold));
		        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		        c1.setBorder(PdfPCell.NO_BORDER);
		        table.addCell(c1);
		        
		        c1 = new PdfPCell(new Paragraph("Contact No-7350334378/9075747714/9850625022",smallBold));
		        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		        c1.setBorder(PdfPCell.NO_BORDER);
		        table.addCell(c1);
		        
		        c1 = new PdfPCell(new Paragraph("Cash Receipt",font));
		        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		        table.addCell(c1);
		        
		        PdfPTable data = new PdfPTable(4);
		        c1 = new PdfPCell(new Paragraph("Sr.No: ",smallBold));
		        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		        c1.setBorder(PdfPCell.NO_BORDER);
		        data.addCell(c1);
		        
		        c1 = new PdfPCell(new Paragraph(" "+receipt.getId(),smallBold));		        
		        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		        c1.setBorder(PdfPCell.NO_BORDER);
		        data.addCell(c1);
		        
		        c1 = new PdfPCell(new Paragraph("Date: ",smallBold));
		        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        c1.setBorder(PdfPCell.NO_BORDER);
		        data.addCell(c1);
		        
		        c1 = new PdfPCell(new Paragraph(""+receipt.getDate(),smallBold));
		        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		        c1.setBorder(PdfPCell.NO_BORDER);
		        data.addCell(c1);

		        c1 = new PdfPCell(new Paragraph("Name: ",smallBold));
		        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		        c1.setBorder(PdfPCell.LEFT);
		        data.addCell(c1);
		        
		        c1 = new PdfPCell(new Paragraph(receipt.getName(),smallBold));
		        c1.setColspan(3);
		        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		        c1.setBorder(PdfPCell.NO_BORDER);
		        data.addCell(c1);
		        
		        c1 = new PdfPCell(new Paragraph("Amount: ",smallBold));
		        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		        c1.setBorder(PdfPCell.LEFT);
		        data.addCell(c1);
		        
		        c1 = new PdfPCell(new Paragraph(""+receipt.getAmount(),smallBold));
		        c1.setColspan(3);
		        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		        c1.setBorder(PdfPCell.NO_BORDER);
		        data.addCell(c1);
		        
		        c1 = new PdfPCell(new Paragraph("Method of Payment",smallBold));
		        c1.setColspan(4);
		        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		        c1.setBorder(PdfPCell.LEFT);
		        data.addCell(c1);
		        
		        if(receipt.getBank().getIfsc().equalsIgnoreCase("cash")) {
		        c1 = new PdfPCell(new Paragraph("Cash",smallBold));
		        c1.setColspan(4);
		        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		        c1.setBorder(PdfPCell.LEFT);
		        data.addCell(c1);
		        }
		        else
		        {
		        	c1 = new PdfPCell(new Paragraph("Bank: "+receipt.getBank().getBankname(),smallBold));
			        c1.setColspan(4);
			        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			        c1.setBorder(PdfPCell.LEFT);
			        data.addCell(c1);
		        }
			c1 = new PdfPCell(new Paragraph("Reference Note: "+receipt.getNote(),smallBold));
			c1.setColspan(4);
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(PdfPCell.NO_BORDER);
			data.addCell(c1);

		        c1 = new PdfPCell(new Paragraph("            Signature               ",smallBold));
		        c1.setColspan(4);
		        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        c1.setBorder(PdfPCell.NO_BORDER);
		        data.addCell(c1);

		        c1 = new PdfPCell(new Paragraph("            ",smallBold));
		        c1.setColspan(4);
		        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        c1.setBorder(PdfPCell.BOTTOM);
		        data.addCell(c1);
	
		        
		        c1 = new PdfPCell(data);
		        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		        table.addCell(c1);
		        
		        doc.add(table);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		new PrintPaymentReceipt(1);
	}
}
