package main.java.main.java.print;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import main.java.main.java.hibernate.entities.Employee;
import main.java.main.java.hibernate.entities.LabourCharges;
import main.java.main.java.hibernate.entities.LabourChargesTransaction;
import main.java.main.java.hibernate.service.service.LabourChargesService;
import main.java.main.java.hibernate.service.serviceImpl.LabourChargesServiceImpl;

import java.io.FileOutputStream;
import java.time.LocalDate;

public class LabourChargesPrint {
	public static String filename = "D:\\Software\\Prints\\LabouCharges.pdf";
	 float[] columnWidths = new float[]{8f,35f,8f,12f,10f,10f,10f,10f};
	 private LabourChargesService service;
	 private Employee labour;
	 private LabourCharges lc;
	 Document doc;
	 
	 private static Font head = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
	 private static Font subhead = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.NORMAL);
	 private static Font footerFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
	 long id;
	 
	 public LabourChargesPrint(long id) {
		 try {
				this.id = id;
				service = new LabourChargesServiceImpl();
				lc = service.getLabourChargesById(id);
				labour = lc.getLabour();

				float left = 0, right = 0, top = 20, bottom = 0;
				doc = new Document(PageSize.A4, left, right, top, bottom);

				PdfWriter.getInstance(doc, new FileOutputStream(filename));
				doc.open();
				
				addData();
				doc.close();
				System.out.println("Writting Done");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	private void addData() {
		try {
			PdfPTable table = new PdfPTable(1);
			PdfPCell c1 = new PdfPCell(new Paragraph("Yash Goat Farm & Seeds", head));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(PdfPCell.NO_BORDER);
			table.addCell(c1);

			c1 = new PdfPCell(new Paragraph("Labour Charges Report", subhead));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(PdfPCell.BOTTOM);
			table.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Labour Name-  " + labour.getFullName(), footerFont));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(PdfPCell.NO_BORDER);
			table.addCell(c1);
			
			LocalDate d1 = lc.getTransaction().get(0).getDate();
			LocalDate d2 = lc.getTransaction().get(lc.getTransaction().size()-1).getDate();

			c1 = new PdfPCell(new Paragraph("Report Date-  " + d1 + " to " + d2, footerFont));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(PdfPCell.NO_BORDER);
			table.addCell(c1);
					
			c1 = new PdfPCell(new Paragraph("Payment Bank-  " + lc.getBank().getBankname(), footerFont));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(PdfPCell.NO_BORDER);
			table.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Transaction No-  "+lc.getBankReffNo(),footerFont));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			 c1.setBorder(PdfPCell.NO_BORDER);
			table.addCell(c1);
			
			PdfPTable outer = new PdfPTable(1);
			c1 = new PdfPCell(table);
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(PdfPCell.BOX);
			outer.addCell(c1);
			
			c1 = new PdfPCell(transactionTable());
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(PdfPCell.BOX);
			outer.addCell(c1);
			
			doc.add(outer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	private PdfPTable transactionTable() {
		try {
			float[] columnWidths = new float[] { 8f, 10f, 15f, 11f, 11f };
			PdfPTable tr = new PdfPTable(5);
			tr.setWidths(columnWidths);
			PdfPCell c1 = new PdfPCell(new Paragraph("Sr.No"));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(PdfPCell.BOX);
			tr.addCell(c1);

				c1 = new PdfPCell(new Paragraph("Date"));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBorder(PdfPCell.BOX);
				tr.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph("Item Name"));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBorder(PdfPCell.BOX);
				tr.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph("Quantity"));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBorder(PdfPCell.BOX);
				tr.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph("Cutting Charges"));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBorder(PdfPCell.BOX);
				tr.addCell(c1);
				int sr=0;
				double qty=0,charges=0;
				for(LabourChargesTransaction t:lc.getTransaction())
				{
					c1 = new PdfPCell(new Paragraph(""+(++sr)));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBorder(PdfPCell.BOX);
					tr.addCell(c1);
					
					c1 = new PdfPCell(new Paragraph(""+t.getDate()));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBorder(PdfPCell.BOX);
					tr.addCell(c1);
					
					c1 = new PdfPCell(new Paragraph(t.getItem()));
					c1.setHorizontalAlignment(Element.ALIGN_LEFT);
					c1.setBorder(PdfPCell.BOX);
					tr.addCell(c1);
					
					c1 = new PdfPCell(new Paragraph(""+t.getQty()));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBorder(PdfPCell.BOX);
					tr.addCell(c1);
					
					c1 = new PdfPCell(new Paragraph(""+t.getPaidLabourCharges()));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBorder(PdfPCell.BOX);
					tr.addCell(c1);
					qty=qty+t.getQty();
					charges = charges+t.getPaidLabourCharges();
				}
				c1 = new PdfPCell(new Paragraph("Total         ",footerFont));
				c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				c1.setBorder(PdfPCell.BOX);
				c1.setColspan(3);
				tr.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(""+qty,footerFont));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBorder(PdfPCell.BOX);				
				tr.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(""+charges,footerFont));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBorder(PdfPCell.BOX);				
				tr.addCell(c1);
				
				
				return tr;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		new LabourChargesPrint(1);
	}
}
