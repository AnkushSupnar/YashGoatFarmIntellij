package main.java.main.java.print;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import main.java.main.java.hibernate.entities.Bank;
import main.java.main.java.hibernate.entities.Bill;
import main.java.main.java.hibernate.entities.Transaction;
import main.java.main.java.hibernate.service.service.BankService;
import main.java.main.java.hibernate.service.service.BillService;
import main.java.main.java.hibernate.service.service.ItemService;
import main.java.main.java.hibernate.service.serviceImpl.BankServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.BillServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.ItemServiceImpl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class GenerateBill {
	public static String filename = "D:\\Software\\Prints\\bill.pdf";
	//private static Font font = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL);
	//private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
	//private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
	//private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
	//private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
	private static Font smallfont = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL);
	private BillService billService;
	private BankService bankService;
	private Bank bank;
	private ItemService itemService;
	private Bill bill;
	long billno;
	 float[] columnWidths = new float[]{8f,35f,8f,12f,10f,10f,10f,10f};
	 public GenerateBill(long billno) {
		 try {
				this.billno = billno;
				billService = new BillServiceImpl();
				itemService = new ItemServiceImpl();
				bill = billService.getBillByBillno(billno);
				bankService = new BankServiceImpl();
				bank = bankService.getBankById(bill.getBank().getId());
				
				if(bill==null)
				{
					return;
				}
				float left = 0;
		        float right = 0;
		        float top = 20;
		        float bottom = 0;
		        Document doc = new Document(PageSize.A4 ,left,right,top,bottom);
			
				PdfWriter.getInstance(doc, new FileOutputStream(filename));
				doc.open();
				addContent(doc);
				doc.close();
				System.out.println("Write Done");
			} catch (FileNotFoundException | DocumentException e) {
				e.printStackTrace();
			}
	}
	public static void main(String[] args) {
		
		new GenerateBill(189);
	}
	void addContent(Document doc)
	{
		try {
			PdfPTable table = new PdfPTable(1);
			String imageFile = "D:\\Software\\Images\\Yash Bill Head.png";
			Image image = Image.getInstance(imageFile);

			PdfPCell c1 = new PdfPCell(image, true);
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			// c1.setBorder(0);
			// c1.setBorder(PdfPCell.NO_BORDER);
			table.addCell(c1);
			
			//costomer Infor and bill no
			PdfPTable customer = new PdfPTable(2);
			//1
			c1 = new PdfPCell(new Paragraph("To,"));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			// c1.setBorder(0);
			c1.setBorder(PdfPCell.RIGHT);
			customer.addCell(c1);
			//2
			c1 = new PdfPCell(new Paragraph(""));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			// c1.setBorder(0);
			c1.setBorder(PdfPCell.RIGHT);
			customer.addCell(c1);
			//3
			c1 = new PdfPCell(new Paragraph(bill.getCustomer().getFname()+" "+bill.getCustomer().getMname()+" "+bill.getCustomer().getLname()));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			// c1.setBorder(0);
			c1.setBorder(PdfPCell.RIGHT);
			customer.addCell(c1);
			//4
			c1 = new PdfPCell(new Paragraph("Invoice No-"+bill.getBillno()));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setVerticalAlignment(Element.ALIGN_CENTER);
			// c1.setBorder(0);
			 c1.setBorder(PdfPCell.BOTTOM);
			customer.addCell(c1);			
			
			//5
			c1 = new PdfPCell(new Paragraph(bill.getCustomer().getAddress()+","+bill.getCustomer().getCity()));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			// c1.setBorder(0);
			c1.setBorder(PdfPCell.RIGHT);
			customer.addCell(c1);
			//6
			c1 = new PdfPCell(new Paragraph(""));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			// c1.setBorder(0);
			c1.setBorder(PdfPCell.RIGHT);
			customer.addCell(c1);
			//7
			c1 = new PdfPCell(new Paragraph(bill.getCustomer().getTaluka()+","+bill.getCustomer().getDistrict()+","+bill.getCustomer().getState()+","+bill.getCustomer().getPin()));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			// c1.setBorder(0);
			 c1.setBorder(PdfPCell.RIGHT);
			customer.addCell(c1);
			//8
			c1 = new PdfPCell(new Paragraph("Date:"+bill.getDate()));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			// c1.setBorder(0);
			c1.setBorder(PdfPCell.RIGHT);
			customer.addCell(c1);
			
			//9
			c1 = new PdfPCell(new Paragraph("Contact No:"+bill.getCustomer().getMobileno()));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			// c1.setBorder(0);
			c1.setBorder(PdfPCell.RIGHT);
			customer.addCell(c1);
			//10
			c1 = new PdfPCell(new Paragraph(""));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			// c1.setBorder(0);
			c1.setBorder(PdfPCell.RIGHT);
			customer.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("GSTIN-        "));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			// c1.setBorder(0);
			//c1.setBorder(PdfPCell.RIGHT);
			//c1.setColspan(2);
			customer.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Transport-"));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			// c1.setBorder(0);
			//c1.setBorder(PdfPCell.RIGHT);
			//c1.setColspan(2);
			customer.addCell(c1);
			
			c1 = new PdfPCell(customer);
			//c1.setBorder(PdfPCell.BOX);
			table.addCell(c1);
			
			PdfPTable item = new PdfPTable(8);
			item.setWidths(columnWidths);
			c1 = new PdfPCell(new Paragraph("SrNo"));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
			c1.setBorder(PdfPCell.BOX);
			item.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Description"));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
			c1.setBorder(PdfPCell.BOX);
			item.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("HSN"));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
			c1.setBorder(PdfPCell.BOX);
			item.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Quantity"));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
			c1.setBorder(PdfPCell.BOX);
			item.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Unit"));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
			c1.setBorder(PdfPCell.BOX);
			item.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Rate"));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
			c1.setBorder(PdfPCell.BOX);
			item.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("GST %"));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
			c1.setBorder(PdfPCell.BOX);
			item.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Amount"));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
			c1.setBorder(PdfPCell.BOX);
			item.addCell(c1);
			int sr=0;
			for(Transaction tr:bill.getTransaction())
			{
				c1 = new PdfPCell(new Paragraph(""+(++sr)));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);			
				c1.setBorder(PdfPCell.RIGHT);
				c1.setFixedHeight(20);
				item.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(tr.getItemname()));
				c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
				c1.setBorder(PdfPCell.RIGHT);
				item.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(itemService.getItemByName(tr.getItemname()).getHsn()));
				c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
				c1.setBorder(PdfPCell.RIGHT);
				item.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(""+tr.getQuantity()));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);			
				c1.setBorder(PdfPCell.RIGHT);				
				item.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(""+tr.getUnit()));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);			
				c1.setBorder(PdfPCell.RIGHT);
				item.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(""+tr.getRate()));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);			
				c1.setBorder(PdfPCell.RIGHT);
				item.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph("0%"));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);			
				c1.setBorder(PdfPCell.RIGHT);
				item.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(""+tr.getAmount()));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);			
				c1.setBorder(PdfPCell.RIGHT);
				item.addCell(c1);
			}
			
			for(int i=sr;i<10;i++)
			{
				for(int j=0;j<8;j++)
				{
					c1 = new PdfPCell(new Paragraph(" "));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);			
					c1.setBorder(PdfPCell.RIGHT);
					c1.setFixedHeight(20);
					item.addCell(c1);
				}
			}
			
			//Add GoodsDetails
			c1 = new PdfPCell(new Paragraph(" "));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);			
			c1.setBorder(PdfPCell.RIGHT);
			//c1.setFixedHeight(20);
			item.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("",smallfont));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
			c1.setBorder(PdfPCell.RIGHT);
			//c1.setFixedHeight(20);
			item.addCell(c1);
			
			for(int i=0;i<6;i++)
			{
				c1 = new PdfPCell(new Paragraph(" "));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);			
				c1.setBorder(PdfPCell.RIGHT);
				//c1.setFixedHeight(20);
				item.addCell(c1);
			}
			
			c1 = new PdfPCell(new Paragraph(" "));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);			
			c1.setBorder(PdfPCell.RIGHT);
			//c1.setFixedHeight(20);
			item.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph(" ",smallfont));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
			c1.setBorder(PdfPCell.RIGHT);
			//c1.setFixedHeight(20);
			item.addCell(c1);
			
			for(int i=0;i<6;i++)
			{
				c1 = new PdfPCell(new Paragraph(" "));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);			
				c1.setBorder(PdfPCell.RIGHT);
				//c1.setFixedHeight(20);
				item.addCell(c1);
			}
			
			c1 = new PdfPCell(new Paragraph(" "));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);			
			c1.setBorder(PdfPCell.TOP);
			//c1.setFixedHeight(20);
			item.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("FODDER SEEDS",smallfont));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
			//c1.setBorder(PdfPCell.RIGHT);
			c1.setBorder(PdfPCell.TOP);
			//c1.setFixedHeight(20);
			item.addCell(c1);
			
			for(int i=0;i<6;i++)
			{
				
				c1 = new PdfPCell(new Paragraph(" "));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);			
				c1.setBorder(PdfPCell.LEFT);
				//c1.setFixedHeight(20);
				item.addCell(c1);
				
			}
			
			c1 = new PdfPCell(new Paragraph(" "));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);			
			//c1.setBorder(PdfPCell.RIGHT);
			c1.setBorder(PdfPCell.NO_BORDER);
			//c1.setFixedHeight(20);
			//c1.setFixedHeight(20);
			item.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("NON TAXABLE GOODS",smallfont));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
			c1.setBorder(PdfPCell.RIGHT);
			//c1.setBorder(PdfPCell.NO_BORDER);
			//c1.setFixedHeight(20);
			item.addCell(c1);
			
			
			
			for(int i=0;i<6;i++)
			{
				c1 = new PdfPCell(new Paragraph(" "));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);			
				c1.setBorder(PdfPCell.RIGHT);
				//c1.setFixedHeight(20);
				item.addCell(c1);
			}
			
			c1 = new PdfPCell(new Paragraph(" "));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);			
			//c1.setBorder(PdfPCell.RIGHT);
			c1.setBorder(PdfPCell.NO_BORDER);
			//c1.setFixedHeight(20);
			//c1.setFixedHeight(20);
			item.addCell(c1);
			
			
			//c1 = new PdfPCell(packaging);
			c1 = new PdfPCell(new Paragraph("EXEMPTED FROM VAT",smallfont));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
			c1.setBorder(PdfPCell.RIGHT);
			//c1.setBorder(PdfPCell.NO_BORDER);
			//c1.setFixedHeight(20);
			item.addCell(c1);
			
			
			
			for(int i=0;i<6;i++)
			{
				c1 = new PdfPCell(new Paragraph(" ",smallfont));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);			
				c1.setBorder(PdfPCell.RIGHT);
				//c1.setFixedHeight(20);
				item.addCell(c1);
			}
			
			c1 = new PdfPCell(new Paragraph(" "));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);			
			//c1.setBorder(PdfPCell.RIGHT);
			c1.setBorder(PdfPCell.NO_BORDER);
			//c1.setFixedHeight(20);
			//c1.setFixedHeight(20);
			item.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("AGRICULTURE PRODUCE",smallfont));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
			c1.setBorder(PdfPCell.RIGHT);
			//c1.setBorder(PdfPCell.LEFT);
			//c1.setFixedHeight(20);
			//c1.setFixedHeight(20);
			item.addCell(c1);
			
			
			
			for(int i=0;i<6;i++)
			{
				c1 = new PdfPCell(new Paragraph(" ",smallfont));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);			
				c1.setBorder(PdfPCell.RIGHT);
				c1.setFixedHeight(20);
				item.addCell(c1);
			}
			
			
			
			
			for(int i=0;i<5;i++)
			{
				if(i==1)
				{
					c1 = new PdfPCell(addPackaging());
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);			
					c1.setBorder(PdfPCell.RIGHT);
					c1.setFixedHeight(20);
					c1.setRowspan(3);
					item.addCell(c1);
				}
				else if(i==0)
				{
					c1 = new PdfPCell(new Paragraph(" "));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);			
					c1.setBorder(PdfPCell.NO_BORDER);
					c1.setFixedHeight(20);
					//c1.setRowspan(3);
					item.addCell(c1);
				}
				else {
				c1 = new PdfPCell(new Paragraph(" "));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);			
				c1.setBorder(PdfPCell.RIGHT);
				c1.setFixedHeight(20);
				item.addCell(c1);
				}
			}
			
			c1 = new PdfPCell(new Paragraph(" Net Total"));
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);			
			c1.setBorder(PdfPCell.BOX);
			c1.setColspan(2);
			c1.setFixedHeight(20);
			item.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph(""+bill.getNettotal()));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
			c1.setBorder(PdfPCell.BOX);
			c1.setColspan(2);
			c1.setFixedHeight(20);
			item.addCell(c1);
			//for other Charges
			for(int i=0;i<5;i++)
			{
				
				if(i>1)
				{
				c1 = new PdfPCell(new Paragraph(" "));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);			
				c1.setBorder(PdfPCell.RIGHT);
				c1.setFixedHeight(20);
				item.addCell(c1);
				}
				else if(i==0)
				{
					c1 = new PdfPCell(new Paragraph(" "));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);			
					c1.setBorder(PdfPCell.NO_BORDER);
					c1.setFixedHeight(20);
					item.addCell(c1);
				}
				
				
			}
			c1 = new PdfPCell(new Paragraph("Other Charges"));
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);			
			c1.setBorder(PdfPCell.BOX);
			c1.setColspan(2);
			c1.setFixedHeight(20);
			item.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph(""+bill.getOtherchargs()));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
			c1.setBorder(PdfPCell.BOX);
			//c1.setColspan(2);
			c1.setFixedHeight(20);
			item.addCell(c1);
			//for Transporting Charges
			for(int i=0;i<5;i++)
			{
				if(i>1) {
				c1 = new PdfPCell(new Paragraph(" "));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);			
				c1.setBorder(PdfPCell.RIGHT);
				c1.setFixedHeight(20);
				item.addCell(c1);
				}
				else if(i==0)
				{
					c1 = new PdfPCell(new Paragraph(" "));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);			
					c1.setBorder(PdfPCell.NO_BORDER);
					c1.setFixedHeight(20);
					item.addCell(c1);
				}
			}
			
			c1 = new PdfPCell(new Paragraph("Transp.Chargs"));
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);			
			c1.setBorder(PdfPCell.BOX);
			c1.setColspan(2);
			c1.setFixedHeight(20);
			item.addCell(c1);
			
			
			c1 = new PdfPCell(new Paragraph(""+bill.getTransportingchrges()));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
			c1.setBorder(PdfPCell.BOX);
			//c1.setColspan(2);
			c1.setFixedHeight(20);
			item.addCell(c1);
			//for Grand Total
			for(int i=0;i<5;i++)
			{
				if(i==1)
				{
					c1 = new PdfPCell(new Paragraph("GSTIN:27AHKPL3715E1ZG"));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);			
					c1.setBorder(PdfPCell.TOP);
					c1.setFixedHeight(20);
					item.addCell(c1);
				}else if(i==0)
				{
					c1 = new PdfPCell(new Paragraph(" "));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);			
					c1.setBorder(PdfPCell.TOP);
					c1.setFixedHeight(20);
					item.addCell(c1);
				}else
				{
					c1 = new PdfPCell(new Paragraph(" "));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);			
					c1.setBorder(PdfPCell.LEFT);
					c1.setFixedHeight(20);
					item.addCell(c1);
				}
			}
			c1 = new PdfPCell(new Paragraph("Grand Total"));
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);			
			c1.setBorder(PdfPCell.BOX);
			c1.setColspan(2);
			c1.setFixedHeight(20);
			item.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph(""+bill.getNettotal()+bill.getOtherchargs()+bill.getTransportingchrges()));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
			c1.setBorder(PdfPCell.BOX);
			c1.setColspan(2);
			c1.setFixedHeight(20);
			item.addCell(c1);
			
			c1 = new PdfPCell(item);
			//c1.setBorder(PdfPCell.BOX);
			table.addCell(c1);
			
			
			
			
			
			c1 = new PdfPCell(addFooter());
			//c1.setBorder(PdfPCell.BOX);
			table.addCell(c1);
			
			doc.add(table);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public PdfPTable addFooter()
	{
		try {
		PdfPTable footer = new PdfPTable(3);
		float width[] = new float[] {5f,10f,20f};
		footer.setWidths(width);
		PdfPCell c1 = new PdfPCell(new Paragraph("Bank Details",smallfont));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
		c1.setBorder(PdfPCell.NO_BORDER);			
		c1.setFixedHeight(15);
		footer.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph(" ",smallfont));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
		c1.setBorder(PdfPCell.RIGHT);
		c1.setFixedHeight(15);
		footer.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph(" For Yash Goat Farm And Seeds"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setVerticalAlignment(Element.ALIGN_BOTTOM);
		c1.setBorder(PdfPCell.NO_BORDER);
		c1.setFixedHeight(15);
		c1.setRowspan(2);
		footer.addCell(c1);
		
		
		c1 = new PdfPCell(new Paragraph("Name",smallfont));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
		c1.setBorder(PdfPCell.NO_BORDER);
		c1.setFixedHeight(15);
		footer.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph("Yash Goat Farm And Seeds",smallfont));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
		c1.setBorder(PdfPCell.RIGHT);
		c1.setFixedHeight(15);
		footer.addCell(c1);
		
		
		
		c1 = new PdfPCell(new Paragraph("IFSC Code ",smallfont));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
		c1.setBorder(PdfPCell.NO_BORDER);
		c1.setFixedHeight(15);
		footer.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph(bank.getIfsc(),smallfont));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
		c1.setBorder(PdfPCell.RIGHT);
		c1.setFixedHeight(15);
		footer.addCell(c1);
		c1 = new PdfPCell(new Paragraph(" ",smallfont));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
		c1.setBorder(PdfPCell.NO_BORDER);
		c1.setFixedHeight(15);
		footer.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph("Account No ",smallfont));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
		c1.setBorder(PdfPCell.NO_BORDER);
		c1.setFixedHeight(15);
		footer.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph(bank.getAccountno(),smallfont));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
		c1.setBorder(PdfPCell.RIGHT);
		c1.setFixedHeight(15);
		footer.addCell(c1);
		c1 = new PdfPCell(new Paragraph("Proprietor "));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setVerticalAlignment(Element.ALIGN_BOTTOM);
		c1.setBorder(PdfPCell.NO_BORDER);
		c1.setFixedHeight(15);
		c1.setRowspan(2);
		footer.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph("Bank Branch ",smallfont));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
		c1.setBorder(PdfPCell.NO_BORDER);
		c1.setFixedHeight(15);
		footer.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph(bank.getBranch(),smallfont));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
		c1.setBorder(PdfPCell.RIGHT);
		c1.setFixedHeight(15);
		footer.addCell(c1);
		c1 = new PdfPCell(new Paragraph(" Propritor",smallfont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);			
		c1.setBorder(PdfPCell.NO_BORDER);
		c1.setFixedHeight(15);
		//footer.addCell(c1);
		
		return footer;
		}catch(Exception e)
		{
			return null;
		}
		
		
	}

	public PdfPTable addPackaging()
	{
		try {
		float[] widths = new float[]{35f,8f};
		PdfPTable packaging = new PdfPTable(2);
		packaging.setWidths(widths);
		PdfPCell c1 = new PdfPCell(new Paragraph(" "));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
		c1.setBorder(PdfPCell.NO_BORDER);
		//c1.setFixedHeight(20);
		//packaging.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph("No.OF BAGS"));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
		c1.setBorder(PdfPCell.BOX);
		//c1.setFixedHeight(20);
		packaging.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph(" "));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
		c1.setBorder(PdfPCell.NO_BORDER);
		//c1.setFixedHeight(20);
		packaging.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph(" "));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
		c1.setBorder(PdfPCell.NO_BORDER);
		//c1.setFixedHeight(20);
		//packaging.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph("CC ATTACH"));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
		c1.setBorder(PdfPCell.BOX);
		//c1.setFixedHeight(20);
		packaging.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph(" "));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
		c1.setBorder(PdfPCell.NO_BORDER);
		//c1.setFixedHeight(20);
		packaging.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph(" "));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
		c1.setBorder(PdfPCell.NO_BORDER);
		//c1.setFixedHeight(20);
		//packaging.addCell(c1);		
		c1 = new PdfPCell(new Paragraph("TO PAY/PAID"));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
		c1.setBorder(PdfPCell.BOX);
		c1.setFixedHeight(15);
		packaging.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph(" "));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
		c1.setBorder(PdfPCell.NO_BORDER);
		//c1.setFixedHeight(20);
		packaging.addCell(c1);
		return packaging;
		}catch(Exception e)
		{
			return null;
		}
	}
}
