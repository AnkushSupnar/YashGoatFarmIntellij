package main.java.main.java.print;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import main.java.main.java.hibernate.reportEntity.ItemStockReport;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;

public class ItemStockReport2 {
	public static String filename = "D:\\Software\\Prints\\ItemStock.pdf";
	 private static Font head = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
	 private static Font subhead = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.NORMAL);
	 float[] columnWidths = new float[]{8f,35f,8f,12f,10f,10f,10f,10f};
	 Document doc;
	// private ItemStockService service;
	 List<ItemStockReport> list;
	 LocalDate dateStart,dateEnd;
	public ItemStockReport2(LocalDate dateStart,LocalDate dateEnd,List<ItemStockReport> list) {
		try {
			this.list = list;
			this.dateEnd = dateEnd;
			this.dateStart = dateStart;
			//service = new ItemStockServiceImpl();
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
		//new ItemStockReport2();
	}
	private void addData() {
		try {
			PdfPTable table = new PdfPTable(1);
			PdfPCell c1 = new PdfPCell(new Paragraph("Yash Goat Farm & Seeds",head));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(PdfPCell.NO_BORDER);
			table.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Item Stock Report",subhead));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			 c1.setBorder(PdfPCell.BOTTOM);
			table.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Report Date"+dateStart+" To "+dateEnd,subhead));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			 c1.setBorder(PdfPCell.BOTTOM);
			table.addCell(c1);
			
			//Stock Data Heading
			PdfPTable tr = new PdfPTable(6);
			float[] columnWidths = new float[] { 5f, 20f, 5f,11f, 11f, 11f };
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
			
			c1 = new PdfPCell(new Paragraph("Sold Qty"));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(PdfPCell.BOX);
			tr.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Quantity"));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(PdfPCell.BOX);
			tr.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Current Stock"));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(PdfPCell.BOX);
			tr.addCell(c1);
			for(ItemStockReport stock:list )
			//for(ItemStock stock:service.getAllItemStock())
			{
				c1 = new PdfPCell(new Paragraph(""+stock.getId()));
				c1.setHorizontalAlignment(Element.ALIGN_LEFT);
				 c1.setBorder(PdfPCell.BOX);
				tr.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(stock.getItemName()));
				c1.setHorizontalAlignment(Element.ALIGN_LEFT);
				 c1.setBorder(PdfPCell.BOX);
				tr.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(stock.getUnit()));
				c1.setHorizontalAlignment(Element.ALIGN_LEFT);
				 c1.setBorder(PdfPCell.BOX);
				tr.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(""+stock.getSoldQty()));
				c1.setHorizontalAlignment(Element.ALIGN_LEFT);
				 c1.setBorder(PdfPCell.BOX);
				tr.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(""+stock.getQty()));
				c1.setHorizontalAlignment(Element.ALIGN_LEFT);
				 c1.setBorder(PdfPCell.BOX);
				tr.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(""));
				c1.setHorizontalAlignment(Element.ALIGN_LEFT);
				 c1.setBorder(PdfPCell.BOX);
				tr.addCell(c1);
			}
			//table.addCell(tr);
			
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
