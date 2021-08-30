package main.java.main.java.print;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import main.java.main.java.hibernate.entities.CounterStock;
import main.java.main.java.hibernate.entities.CounterStockTransaction;
import main.java.main.java.hibernate.service.service.CounterStockService;
import main.java.main.java.hibernate.service.service.ItemService;
import main.java.main.java.hibernate.service.serviceImpl.CounterStockServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.ItemServiceImpl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class CounterStockEntry {

	long id;
	public static String filename = "D:\\Software\\Prints\\CounterStock.pdf";
	 private static Font head = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
	 private static Font subhead = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.NORMAL);
	 float[] columnWidths = new float[]{8f,35f,8f,12f,10f,10f,10f,10f};
	 Document doc;
	 private CounterStockService service;
	 private ItemService itemService;
	 private CounterStock stock;
	 public CounterStockEntry(long id)
	{
		this.id= id;
		service = new CounterStockServiceImpl();
		stock = service.getCounterStockById(id);
		itemService = new ItemServiceImpl();
		try {
		float left = 0,right = 0,top = 20,bottom = 0;
        doc = new Document(PageSize.A4 ,left,right,top,bottom);
		
			PdfWriter.getInstance(doc, new FileOutputStream(filename));
			doc.open();
			addData();
			doc.close();
			System.out.println("Write Done");
		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		new CounterStockEntry(1);
	}
	private void addData() {
		try {
			PdfPTable table = new PdfPTable(1);
			PdfPCell c1 = new PdfPCell(new Paragraph("Yash Goat Farm & Seeds",head));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(PdfPCell.NO_BORDER);
			table.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Counter Item Stock",subhead));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			 c1.setBorder(PdfPCell.BOTTOM);
			table.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Report Date"+stock.getDate(),subhead));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			 c1.setBorder(PdfPCell.BOTTOM);
			table.addCell(c1);
			
			PdfPTable tr = new PdfPTable(6);
			//table heads
			float[] columnWidths = new float[] { 5f, 20f, 5f,11f, 11f,11f };
			tr.setWidths(columnWidths);
			c1 = new PdfPCell(new Paragraph("Sr.No"));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(PdfPCell.BOX);
			tr.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Item Name"));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(PdfPCell.BOX);
			tr.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Unit"));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(PdfPCell.BOX);
			tr.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Old Quantity"));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(PdfPCell.BOX);
			tr.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("New Quantity"));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(PdfPCell.BOX);
			tr.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Current Stock"));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(PdfPCell.BOX);
			tr.addCell(c1);
			//adding data
			int i=0;
			for(CounterStockTransaction t:stock.getTransaction())
			{
				c1 = new PdfPCell(new Paragraph(++i));
				c1.setHorizontalAlignment(Element.ALIGN_LEFT);
				c1.setBorder(PdfPCell.BOX);
				tr.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(t.getItemname()));
				c1.setHorizontalAlignment(Element.ALIGN_LEFT);
				c1.setBorder(PdfPCell.BOX);
				tr.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(itemService.getItemByName(t.getItemname()).getUnit()));
				c1.setHorizontalAlignment(Element.ALIGN_LEFT);
				c1.setBorder(PdfPCell.BOX);
				tr.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(""+t.getOldqty()));
				c1.setHorizontalAlignment(Element.ALIGN_LEFT);
				c1.setBorder(PdfPCell.BOX);
				tr.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(""+t.getNewqty()));
				c1.setHorizontalAlignment(Element.ALIGN_LEFT);
				c1.setBorder(PdfPCell.BOX);
				tr.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(""+(t.getOldqty()+t.getNewqty())));
				c1.setHorizontalAlignment(Element.ALIGN_LEFT);
				c1.setBorder(PdfPCell.BOX);
				tr.addCell(c1);
			}
			PdfPTable outer = new PdfPTable(1);
			c1 = new PdfPCell(table);
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(PdfPCell.BOX);
			outer.addCell(c1);
			//outer.addCell(tr);
			doc.add(outer);
			doc.add(tr);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
