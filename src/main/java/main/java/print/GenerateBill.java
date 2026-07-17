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
				if(bill==null)
				{
					return;
				}
				bankService = new BankServiceImpl();
				bank = bill.getBank() != null ? bankService.getBankById(bill.getBank().getId()) : null;
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

			// Payment status banner: green CASH if fully paid, red CREDIT/PARTIAL otherwise.
			float billTotal = bill.getNettotal() + bill.getTransportingchrges() + bill.getOtherchargs();
			float outstanding = billTotal - bill.getRecivedamount();
			Font bannerFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.WHITE);
			String bannerLabel;
			BaseColor bannerColor;
			if (outstanding <= 0.01f) {
				bannerLabel = "CASH";
				bannerColor = new BaseColor(46, 125, 50);
			} else if (bill.getRecivedamount() <= 0.01f) {
				bannerLabel = "CREDIT BILL";
				bannerColor = new BaseColor(211, 47, 47);
			} else {
				bannerLabel = "PARTIAL / CREDIT BILL — Paid Rs. " + String.format("%.2f", bill.getRecivedamount())
						+ "  |  Outstanding Rs. " + String.format("%.2f", outstanding);
				bannerColor = new BaseColor(211, 47, 47);
			}
			PdfPCell bannerCell = new PdfPCell(new Paragraph(bannerLabel, bannerFont));
			bannerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			bannerCell.setBackgroundColor(bannerColor);
			bannerCell.setPadding(6);
			bannerCell.setBorder(PdfPCell.BOX);
			table.addCell(bannerCell);

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
			
			String gst = bill.getCustomer().getGstno();
			String pan = bill.getCustomer().getPanno();
			c1 = new PdfPCell(new Paragraph("GSTIN- " + (gst == null || gst.isEmpty() || gst.equals("-") ? "" : gst)
					+ "    PAN- " + (pan == null || pan.isEmpty() || pan.equals("-") ? "" : pan)));
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
				
				String igstLabel = tr.getIgstPercent() > 0 ? String.format("%.1f%%", tr.getIgstPercent()) : "0%";
				c1 = new PdfPCell(new Paragraph(igstLabel));
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
			// IGST Total row (only shown when non-zero)
			if (bill.getIgstTotal() > 0.001f) {
				for (int i = 0; i < 5; i++) {
					c1 = new PdfPCell(new Paragraph(" "));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBorder(i == 0 ? PdfPCell.NO_BORDER : PdfPCell.LEFT);
					c1.setFixedHeight(20);
					item.addCell(c1);
				}
				c1 = new PdfPCell(new Paragraph("IGST Total"));
				c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				c1.setBorder(PdfPCell.BOX);
				c1.setColspan(2);
				c1.setFixedHeight(20);
				item.addCell(c1);
				c1 = new PdfPCell(new Paragraph(String.format("%.2f", bill.getIgstTotal())));
				c1.setHorizontalAlignment(Element.ALIGN_LEFT);
				c1.setBorder(PdfPCell.BOX);
				c1.setColspan(2);
				c1.setFixedHeight(20);
				item.addCell(c1);
			}

			c1 = new PdfPCell(new Paragraph("Grand Total"));
			c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c1.setBorder(PdfPCell.BOX);
			c1.setColspan(2);
			c1.setFixedHeight(20);
			item.addCell(c1);

			float grandTotal = bill.getNettotal() + bill.getIgstTotal() + bill.getOtherchargs() + bill.getTransportingchrges();
			c1 = new PdfPCell(new Paragraph(String.format("%.2f", grandTotal)));
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
		
		c1 = new PdfPCell(new Paragraph(bank != null && bank.getIfsc() != null ? bank.getIfsc() : "-",smallfont));
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
		
		c1 = new PdfPCell(new Paragraph(bank != null && bank.getAccountno() != null ? bank.getAccountno() : "-",smallfont));
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
		
		c1 = new PdfPCell(new Paragraph(bank != null && bank.getBranch() != null ? bank.getBranch() : "-",smallfont));
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
