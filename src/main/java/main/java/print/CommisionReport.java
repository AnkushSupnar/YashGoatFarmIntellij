package main.java.main.java.print;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import main.java.main.java.hibernate.entities.Bill;
import main.java.main.java.hibernate.entities.Commision;
import main.java.main.java.hibernate.entities.CommisionTransaction;
import main.java.main.java.hibernate.entities.Employee;
import main.java.main.java.hibernate.service.service.CommisionService;
import main.java.main.java.hibernate.service.serviceImpl.CommisionServiceImpl;

import java.io.FileOutputStream;
import java.time.LocalDate;

public class CommisionReport {

	public static String filename = "D:\\Software\\Prints\\commision.pdf";
	 float[] columnWidths = new float[]{8f,35f,8f,12f,10f,10f,10f,10f};
	 private CommisionService service;
	 private Commision com;
	 
	 private Employee emp;
	 Document doc;
	 
	 private static Font head = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
	 private static Font subhead = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.NORMAL);
	 private static Font footerFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
	public CommisionReport(long id) {
		try {
			service = new CommisionServiceImpl();
			com = service.getCommisionById(id);
			emp = com.getEmployee();
			float left = 0,right = 0,top = 20,bottom = 0;
	        doc = new Document(PageSize.A4 ,left,right,top,bottom);
			PdfWriter.getInstance(doc, new FileOutputStream(filename));
			doc.open();
			addData();
			doc.close();
		System.out.println("Write Done");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		new CommisionReport(4);
	}
	public void addData()
	{
		try {
			PdfPTable table = new PdfPTable(1);
			PdfPCell c1 = new PdfPCell(new Paragraph("Yash Goat Farm & Seeds",head));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			// c1.setBorder(0);
			c1.setBorder(PdfPCell.NO_BORDER);
			table.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Commision Report",subhead));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			 c1.setBorder(PdfPCell.BOTTOM);
			table.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Employee Name-  "+
			emp.getFname()+" "+
			emp.getMname()+" "+
			emp.getLname(),footerFont));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(PdfPCell.NO_BORDER);
			table.addCell(c1);
			LocalDate d1 = com.getTransaction().get(0).getDate();
			LocalDate d2 = com.getTransaction().get(com.getTransaction().size()-1).getDate();
			
			c1 = new PdfPCell(new Paragraph("Report Date-  "+
			d1+" to "+d2,footerFont));
					c1.setHorizontalAlignment(Element.ALIGN_LEFT);
					 c1.setBorder(PdfPCell.NO_BORDER);
					table.addCell(c1);
			
					c1 = new PdfPCell(new Paragraph("Payment Bank-  "+com.getBank().getBankname(),footerFont));
					c1.setHorizontalAlignment(Element.ALIGN_LEFT);
					 c1.setBorder(PdfPCell.NO_BORDER);
					table.addCell(c1);

					c1 = new PdfPCell(new Paragraph("Transaction No-  "+com.getBankReffNo(),footerFont));
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
	public PdfPTable transactionTable()
	{
		try {
		 float[] columnWidths = new float[]{8f,15f,9f,11f,11f,15f,12f,15f};
		PdfPTable tr = new PdfPTable(8);
		tr.setWidths(columnWidths);
		PdfPCell c1 = new PdfPCell(new Paragraph("Sr.No"));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setBorder(PdfPCell.BOX);
		tr.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph("Date"));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setBorder(PdfPCell.BOX);
		tr.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph("Bill No"));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setBorder(PdfPCell.BOX);
		tr.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph("Cash Payment"));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setBorder(PdfPCell.BOX);
		tr.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph("Bank Payment"));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setBorder(PdfPCell.BOX);
		tr.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph("Transporting"));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setBorder(PdfPCell.BOX);
		tr.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph("Total Bill"));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setBorder(PdfPCell.BOX);
		tr.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph("Commision"));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setBorder(PdfPCell.BOX);
		tr.addCell(c1);
		
		//Add Data
		Bill bill;
		int sr=0;
		double cash = 0, bank = 0, totalCash = 0, totalBank = 0, totalBill = 0, totaltran = 0, totalCom = 0;
		for(CommisionTransaction t:com.getTransaction())
		{
			cash=0;
			bank=0;
			bill=null;
			bill = t.getBill();
			c1 = new PdfPCell(new Paragraph(""+(++sr)));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(PdfPCell.BOX);
			tr.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph(""+t.getDate()));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(PdfPCell.BOX);
			tr.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph(""+t.getBill().getBillno()));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(PdfPCell.BOX);
			tr.addCell(c1);
			
			
			if(t.getBill().getBank().getBankname().equalsIgnoreCase("Cash Account"))
			{
				cash=bill.getNettotal()+bill.getOtherchargs()+bill.getTransportingchrges();
				totalCash = totalCash+cash;
			}
			else {
				bank = bill.getNettotal()+bill.getOtherchargs()+bill.getTransportingchrges();
				totalBank = totalBank+bank;
			}
			c1 = new PdfPCell(new Paragraph(""+cash));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(PdfPCell.BOX);
			tr.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph(""+bank));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(PdfPCell.BOX);
			tr.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph(""+bill.getTransportingchrges()));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(PdfPCell.BOX);
			tr.addCell(c1);
			totaltran = totaltran+bill.getTransportingchrges();
			
			c1 = new PdfPCell(new Paragraph(""+(bill.getNettotal()+bill.getOtherchargs()+bill.getTransportingchrges())));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(PdfPCell.BOX);
			tr.addCell(c1);
			totalBill = totalBill+bill.getNettotal()+bill.getOtherchargs()+bill.getTransportingchrges();
			

			c1 = new PdfPCell(new Paragraph(""+bill.getPaidcommision()));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(PdfPCell.BOX);
			tr.addCell(c1);
			totalCom=totalCom+bill.getPaidcommision();
		}
		addTotalHeading(tr);
		//adding TOtal row
		c1 = new PdfPCell(new Paragraph("Total",footerFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setColspan(3);
		c1.setBorder(PdfPCell.BOX);
		tr.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph(""+totalCash,footerFont));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setBorder(PdfPCell.BOX);
		tr.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph(""+totalBank,footerFont));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setBorder(PdfPCell.BOX);
		tr.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph(""+totaltran,footerFont));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setBorder(PdfPCell.BOX);
		tr.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph(""+totalBill,footerFont));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setBorder(PdfPCell.BOX);
		tr.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph(""+totalCom,footerFont));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setBorder(PdfPCell.BOX);
		tr.addCell(c1);
		
		return tr;
		}catch(Exception e)
		{
		e.printStackTrace();
		return null;
		}
		
	}
	void addTotalHeading(PdfPTable tr)
	{
		PdfPCell c1 = new PdfPCell(new Paragraph(""));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setBorder(PdfPCell.BOX);
		c1.setColspan(3);
		tr.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph(""));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setBorder(PdfPCell.BOX);
		//tr.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph(""));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setBorder(PdfPCell.BOX);
		//tr.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph("Cash",footerFont));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setBorder(PdfPCell.BOX);
		tr.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph("Bank",footerFont));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setBorder(PdfPCell.BOX);
		tr.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph("Transport",footerFont));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setBorder(PdfPCell.BOX);
		tr.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph("Bill",footerFont));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setBorder(PdfPCell.BOX);
		tr.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph("Commision",footerFont));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setBorder(PdfPCell.BOX);
		tr.addCell(c1);
	}

}
