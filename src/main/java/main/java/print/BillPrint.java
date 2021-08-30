package main.java.main.java.print;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import main.java.main.java.hibernate.entities.Bill;
import main.java.main.java.hibernate.entities.Transaction;
import main.java.main.java.hibernate.service.service.BillService;
import main.java.main.java.hibernate.service.serviceImpl.BillServiceImpl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class BillPrint {
	public static String filename = "D:\\Software\\Prints\\bill.pdf";
	private static Font font = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
	//private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
	//private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
	//private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
	private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
	private static Font smallfont = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL);
	private BillService billService;
	private Bill bill;
	long billno;
	 float[] columnWidths = new float[]{10f, 40f, 10f, 10f,10f,10f};
	public BillPrint(long billno) {
		try {
			this.billno = billno;
			billService = new BillServiceImpl();
			bill = billService.getBillByBillno(billno);
			if(bill==null)
			{
				return;
			}
			float left = 30;
	        float right = 0;
	        float top = 20;
	        float bottom = 0;
	        Document doc = new Document(PageSize.A4,left,right,top,bottom);
		
			PdfWriter.getInstance(doc, new FileOutputStream(filename));
			doc.open();
			addContent(doc);
			doc.close();
			
		} catch (FileNotFoundException | DocumentException e) {
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
	        
	        c1 = new PdfPCell(new Paragraph("Contact No-9960855742/8329394603",smallBold));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        c1.setBorder(PdfPCell.NO_BORDER);
	        table.addCell(c1);
	        
	        c1 = new PdfPCell(new Paragraph("Cash/Credit",smallBold));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(c1);
	        
	        //Add Bill No And date
	        PdfPTable billnotable = new PdfPTable(2);
	        c1 = new PdfPCell(billnotable);
	        PdfPCell billCell = new PdfPCell(new Paragraph("Bill No:-"+billno,smallBold));
	        billCell.setBorder(PdfPCell.BOTTOM);
	        billnotable.addCell(billCell);
	        
	        PdfPCell dateCell = new PdfPCell(new Paragraph("Date:-"+bill.getDate(),smallBold));
	        dateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        dateCell.setBorder(PdfPCell.BOTTOM);
	        billnotable.addCell(dateCell);
	        table.addCell(c1);
	        
	        c1 = new PdfPCell(new Paragraph("Customer Name:-"+bill.getCustomer().getFname()+" "+bill.getCustomer().getMname()+" "+bill.getCustomer().getLname(),smallBold));
	        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
	        c1.setBorder(0);
	        table.addCell(c1);
	        
	        c1 = new PdfPCell(new Paragraph("Custome Address:-"+
	        bill.getCustomer().getAddress()+
	        ",City-"+bill.getCustomer().getCity()+
	        ",Taluka-"+bill.getCustomer().getTaluka()+
	        ",Dist-"+bill.getCustomer().getDistrict()+
	        ",State-"+bill.getCustomer().getState()+
	        " "+bill.getCustomer().getPin()
	        ,smallBold));
	        c1.setBorder(PdfPCell.BOTTOM);
	        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
	        table.addCell(c1);
	        //Adding Transactions
	       
	        
	        PdfPTable trTableHead = new PdfPTable(6);
	        trTableHead.setWidths(columnWidths);
	        c1 = new PdfPCell(new Paragraph("Sr.No",smallBold));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        trTableHead.addCell(c1);
	        
	        c1 = new PdfPCell(new Paragraph("Item Name",smallBold));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        trTableHead.addCell(c1);
	        
	        c1 = new PdfPCell(new Paragraph("Unit",smallBold));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        trTableHead.addCell(c1);
	        
	        c1 = new PdfPCell(new Paragraph("Quantity",smallBold));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        trTableHead.addCell(c1);
	        
	        c1 = new PdfPCell(new Paragraph("Rate",smallBold));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        trTableHead.addCell(c1);
	        
	        c1 = new PdfPCell(new Paragraph("Amount",smallBold));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        trTableHead.addCell(c1);
	        
	        c1 = new PdfPCell(trTableHead);
	        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
	        table.addCell(c1);
	        int srno=0;
	        double amount = 0.0f;
	        PdfPTable trTable = new PdfPTable(6);
	        for(Transaction tr : bill.getTransaction())
	        {
	        	
	        	trTable.setWidths(columnWidths);
	        	c1 = new PdfPCell(new Paragraph(""+(++srno),smallBold));
		        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		        c1.setBorder(PdfPCell.LEFT);
		        trTable.addCell(c1);
		        
		        c1 = new PdfPCell(new Paragraph(tr.getItemname(),smallBold));
		        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		        c1.setBorder(PdfPCell.NO_BORDER);
		        c1.setBorder(PdfPCell.LEFT);
		        trTable.addCell(c1);
		        
		        c1 = new PdfPCell(new Paragraph(""+tr.getUnit(),smallBold));
		        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		        c1.setBorder(PdfPCell.NO_BORDER);
		        c1.setBorder(PdfPCell.LEFT);
		        trTable.addCell(c1);
		        
		        c1 = new PdfPCell(new Paragraph(""+tr.getQuantity(),smallBold));
		        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		        c1.setBorder(PdfPCell.NO_BORDER);
		        c1.setBorder(PdfPCell.LEFT);
		        trTable.addCell(c1);
		        
		        c1 = new PdfPCell(new Paragraph(""+tr.getRate(),smallBold));
		        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		        c1.setBorder(PdfPCell.NO_BORDER);
		        c1.setBorder(PdfPCell.LEFT);
		        trTable.addCell(c1);
		        
		        c1 = new PdfPCell(new Paragraph(""+tr.getAmount(),smallBold));
		        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		        c1.setBorder(PdfPCell.NO_BORDER);
		        c1.setBorder(PdfPCell.LEFT);
		        trTable.addCell(c1);
		        amount = amount+tr.getAmount();
	        }
	        
	        
	        c1 = new PdfPCell(trTable);
	        c1.setFixedHeight(400);
	        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
	        c1.setBorder(PdfPCell.NO_BORDER);
	        c1.setBorder(PdfPCell.BOX);
	        table.addCell(c1);
	        
	       
	        //Add to Main Table
	        c1 = new PdfPCell(addColumnAsTable("Net Total",amount));
	        c1.setFixedHeight(20);
	        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
	        c1.setBorder(PdfPCell.NO_BORDER);
	        c1.setBorder(PdfPCell.BOX);
	        table.addCell(c1);
	        
	        c1 = new PdfPCell(addColumnAsTable("Transp.Charges",bill.getTransportingchrges()));
	        c1.setFixedHeight(20);
	        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
	        c1.setBorder(PdfPCell.NO_BORDER);
	        c1.setBorder(PdfPCell.BOX);
	        table.addCell(c1);
	        
	        c1 = new PdfPCell(addColumnAsTable("Other Charges",bill.getOtherchargs()));
	        c1.setFixedHeight(20);
	        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
	        c1.setBorder(PdfPCell.NO_BORDER);
	        c1.setBorder(PdfPCell.BOX);
	        table.addCell(c1);
	        
	        c1 = new PdfPCell(addColumnAsTable("Grand Total",(bill.getNettotal()+bill.getTransportingchrges()+bill.getOtherchargs())));
	        c1.setFixedHeight(20);
	        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
	        c1.setBorder(PdfPCell.NO_BORDER);
	        c1.setBorder(PdfPCell.BOX);
	        table.addCell(c1);
	        
	        
	        c1 = new PdfPCell(addFooterTable());
	        c1.setFixedHeight(50);
	        c1.setHorizontalAlignment(Element.ALIGN_BOTTOM);
	        c1.setBorder(PdfPCell.NO_BORDER);
	        c1.setBorder(PdfPCell.BOX);
	        table.addCell(c1);
	     
	        PdfPTable mainBorder = new PdfPTable(1);
	        
	        c1 = new PdfPCell(table);
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        c1.setBorder(PdfPCell.NO_BORDER);
	        c1.setBorder(PdfPCell.BOX);
	        mainBorder.addCell(c1);
	        
	        c1 = new PdfPCell(new Paragraph("Software By Ankush Supnar MNo.8329394603",smallfont));
	        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        c1.setBorder(PdfPCell.NO_BORDER);
	        //c1.setBorder(PdfPCell.BOX);
	        mainBorder.addCell(c1);
	        
	        doc.add(mainBorder);
	        
	        //doc.add(p);
	        System.out.println("Write Done");
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) {
		new BillPrint(6);
		//new PrintFile("D:\\Software\\Prints\\bill.pdf");
	}
	

	private PdfPTable addColumnAsTable(String note,double amount)
	{
		try {
		 //Adding Total Column In Table
        PdfPTable totalTable = new PdfPTable(6);
        totalTable.setWidths(columnWidths);
        
        PdfPCell c1 = new PdfPCell(new Paragraph("",smallBold));
       
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(PdfPCell.NO_BORDER);
        c1.setColspan(3);
       // c1.setBorder(PdfPCell.LEFT);
        totalTable.addCell(c1);
        
        c1 = new PdfPCell(new Paragraph(note,smallBold));
        c1.setColspan(2);
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.setBorder(PdfPCell.NO_BORDER);
        c1.setBorder(PdfPCell.BOX);
        totalTable.addCell(c1);
        
        c1 = new PdfPCell(new Paragraph(""+amount,smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(PdfPCell.NO_BORDER);
        c1.setBorder(PdfPCell.BOX);
        totalTable.addCell(c1);
        
        return totalTable;
		}catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	private PdfPTable addFooterTable()
	{
		try {
			 PdfPTable table = new PdfPTable(6);
		        table.setWidths(columnWidths);
		        
		        PdfPCell c1 = new PdfPCell(new Paragraph("For Recivers Sign",smallBold));
		        c1.setFixedHeight(20);
		        c1.setHorizontalAlignment(Element.ALIGN_BOTTOM);
		        c1.setVerticalAlignment(Element.ALIGN_BOTTOM);
		        c1.setBorder(PdfPCell.NO_BORDER);
		        c1.setColspan(3);
		       
		        table.addCell(c1);
		        
		        c1 = new PdfPCell(new Paragraph("For Yash Goat Farm & Seeds",smallBold));
		        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        c1.setVerticalAlignment(Element.ALIGN_BOTTOM);
		        c1.setBorder(PdfPCell.NO_BORDER);
		        c1.setColspan(3);
		        table.addCell(c1);		       
		       return table; 
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
		
}
