package main.java.main.java.print;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import main.java.main.java.hibernate.entities.Bill;
import main.java.main.java.hibernate.entities.Customer;
import main.java.main.java.hibernate.entities.Employee;
import main.java.main.java.hibernate.entities.Transaction;
import main.java.main.java.hibernate.service.service.BillService;
import main.java.main.java.hibernate.service.serviceImpl.BillServiceImpl;

import java.io.FileOutputStream;

public class CouriorReceipt {
	public static String filename = "D:\\Software\\Prints\\courior.pdf";
	private static Font font = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
	private static Font font2 = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.NORMAL);
	private static Font font3 = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL);
	private static Font font4 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
	private Customer customer;
	private Employee employee;
	
	private Bill bill;
	public CouriorReceipt(Bill bill) {
		this.bill=bill;
		//empService = new EmployeeServiceImpl();
		employee = bill.getEmployee();
		customer = bill.getCustomer();
		generatePdf();
		System.out.println("Write Done");
	}
	void generatePdf()
	{
		try {
			float left = 0;
	        float right = 0;
	        float top = 20;
	        float bottom = 0;
	        Document doc = new Document(PageSize.A4,left,right,top,bottom);
		
			PdfWriter.getInstance(doc, new FileOutputStream(filename));
			doc.open();
			 PdfPTable table = new PdfPTable(3);
			 PdfPCell c1 = new PdfPCell();
			 Paragraph p = new Paragraph("To,",font2);
			 c1.addElement(p);
			  p = new Paragraph(customer.getFname()+" "+customer.getMname()+" "+customer.getLname()
							 ,font);
			 c1.addElement(p);
			 p = new Paragraph(""+customer.getAddress()+", City/Village-"+customer.getCity()+",",font3);
			 c1.addElement(p);
			 p = new Paragraph("Taluka-"+customer.getTaluka()+", District-"+customer.getDistrict()+",",font3);
		     c1.addElement(p);   
		     p = new Paragraph("State-"+customer.getState()+", Pin-"+customer.getPin()+".",font3);
		     c1.addElement(p);
		     p = new Paragraph("Mobile No-"+customer.getMobileno() +","+customer.getAltermobileno()+".",font3);
		     c1.addElement(p);		     
			 c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		       // c1.setBorder(0);
		       c1.setBorder(PdfPCell.NO_BORDER);
		        c1.setColspan(3);
		        
		        table.addCell(c1);
		        
		        //product Details
		        c1 = new PdfPCell();
		        
		        String pname="";
		        double kg=0;
		        double no=0;
		        for(Transaction tr:bill.getTransaction())
		        {
		        	pname=pname+""+tr.getItemname()+",";
		        	if(tr.getUnit().equalsIgnoreCase("kg"))
		        	{
		        		kg=kg+tr.getQuantity();
		        	}
		        	else
		        	{
		        		no=no+tr.getQuantity();
		        	}
		        }
		        p = new Paragraph("Products-"+pname,font3);
			     c1.addElement(p);
			     c1.setColspan(3);
			     c1.setBorder(PdfPCell.NO_BORDER);
			     table.addCell(c1);
			     //add Qty
			     c1 = new PdfPCell();
			     if(kg>0.0) {
			    	 System.out.println(" weight");
			     p = new Paragraph("Weight-"+kg+"kg",font3);
			     c1.addElement(p);
			     }
			     if(no>0.0) {
			     p = new Paragraph("Nos-"+no,font3);
			     c1.addElement(p);
			     }
			     c1.setColspan(3);
			     c1.setBorder(PdfPCell.NO_BORDER);
			     table.addCell(c1);
			     
		        //add VPP COD
			     
			     double cod=0;
//			     if((bill.getNettotal()+
//			    		 bill.getTransportingchrges()+
//			    		 bill.getOtherchargs())>
//			     bill.getRecivedamount())
//			    	 
			    	 cod=(bill.getNettotal()+
			    			 bill.getTransportingchrges()+
			    			 bill.getOtherchargs())-
			    			 bill.getRecivedamount();
//			    	 System.out.println("netTotal "+bill.getNettotal());
//			    	 System.out.println("Other charges "+bill.getOtherchargs());
//			    	 System.out.println("Transaporting "+bill.getTransportingchrges());
//			    	 System.out.println("Recived "+bill.getRecivedamount());
//			    	 System.out.println("COD "+cod);
			    	 c1 = new PdfPCell();
			     if(cod>0) {
			     p = new Paragraph("V.P.P. Cash On Delivery "+cod+" Rs",font3);			     
			     c1.addElement(p);
			     }
			     else {
			    	 p= new Paragraph("");
			    	 c1.addElement(p);
			     }
			     c1.setColspan(3);
			     c1.setBorder(PdfPCell.NO_BORDER);
			     table.addCell(c1);
			     
		        
		                
		        c1 = new PdfPCell();
		        c1.setBorder(PdfPCell.NO_BORDER);
		        table.addCell(c1);
		        
		        c1 = new PdfPCell();
		        p = new Paragraph("From,",font3);
		        c1.setBorder(PdfPCell.NO_BORDER);
		        c1.addElement(p);
		        
		        p = new Paragraph("Yash Goat Farm & Seeds,",font3);
		        c1.addElement(p);
		        p = new Paragraph("At/Post-Fattepur, Tal-Newasa,Dist-Ahmednagar,",font4);
		        c1.addElement(p);
		        
		        p = new Paragraph("State-Maharashtra,Pin-414606,",font3);
		        c1.addElement(p);
		        p = new Paragraph("Contact No-"+employee.getMobileno()+","+employee.getAltermobileno(),font3);
		        c1.addElement(p);
		        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		        //c1.setBorder(0);
		        c1.setBorder(PdfPCell.NO_BORDER);
		        c1.setColspan(2);
		        table.addCell(c1);
		        doc.add(table);
			doc.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		
		BillService service = new BillServiceImpl();
		Bill bill = service.getBillByBillno(163);
		new CouriorReceipt(bill);
	}
}
