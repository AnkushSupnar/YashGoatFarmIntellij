package main.java.main.java.print;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import main.java.main.java.hibernate.entities.CounterStockData;
import main.java.main.java.hibernate.service.service.CounterStockDataService;
import main.java.main.java.hibernate.service.serviceImpl.CounterStockDataServiceImpl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;

public class ViewAllCounterStock {

	public static String filename = "D:\\Software\\Prints\\ViewAllCounterStock.pdf";
	 private static Font head = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
	 private static Font subhead = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.NORMAL);
	 float[] columnWidths = new float[]{8f,35f,8f,12f,10f,10f,10f,10f};
	 Document doc;
	 private CounterStockDataService service;
	public ViewAllCounterStock()
	{
		try {
			service = new CounterStockDataServiceImpl();
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
			
			c1 = new PdfPCell(new Paragraph("Report Date"+ LocalDate.now(),subhead));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			 c1.setBorder(PdfPCell.BOTTOM);
			table.addCell(c1);
			
			PdfPTable tr = new PdfPTable(4);
			//table heads
			float[] columnWidths = new float[] { 5f, 20f, 5f,11f };
			tr.setWidths(columnWidths);
			c1 = new PdfPCell(new Paragraph("Sr.No"));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(PdfPCell.BOX);
			tr.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Item Name"));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(PdfPCell.BOX);
			tr.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Quantity"));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(PdfPCell.BOX);
			tr.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Unit"));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(PdfPCell.BOX);
			tr.addCell(c1);
			//add Data
			for(CounterStockData data:service.getAllCounterStockData())
			{
				c1 = new PdfPCell(new Paragraph(""+data.getId()));
				c1.setHorizontalAlignment(Element.ALIGN_LEFT);
				c1.setBorder(PdfPCell.BOX);
				tr.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(data.getItemname()));
				c1.setHorizontalAlignment(Element.ALIGN_LEFT);
				c1.setBorder(PdfPCell.BOX);
				tr.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(""+data.getQty()));
				c1.setHorizontalAlignment(Element.ALIGN_LEFT);
				c1.setBorder(PdfPCell.BOX);
				tr.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(""+data.getUnit()));
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
	public static void main(String[] args) {
		
		new ViewAllCounterStock();
	}

}
