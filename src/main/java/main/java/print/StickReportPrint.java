package main.java.main.java.print;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import main.java.main.java.hibernate.reportEntity.ReportTable;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;

public class StickReportPrint {
	public static String filename = "D:\\Software\\Prints\\StickReport.pdf";
	 private static Font head = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
	 private static Font subhead = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.NORMAL);
	 private static Font footerFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
	 float[] columnWidths = new float[]{8f,35f,8f,12f,10f,10f,10f,10f};
	 Document doc;
	 List<ReportTable> list;
	 LocalDate start,end;
	 public StickReportPrint(List<ReportTable> list,LocalDate start, LocalDate end) {
			this.list = list;
			this.start = start;
			this.end = end;
			try {
				float left = -40, right = -40, top = 20, bottom = 0;
				doc = new Document(PageSize.A4, left, right, top, bottom);
				try {
					PdfWriter.getInstance(doc, new FileOutputStream(filename));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
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
			
			c1 = new PdfPCell(new Paragraph("Stick Report", subhead));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(PdfPCell.BOTTOM);
			table.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Report Date-  " + start + " to " + end, footerFont));
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(PdfPCell.NO_BORDER);
			table.addCell(c1);
			
			PdfPTable outer = new PdfPTable(1);
			c1 = new PdfPCell(table);
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			c1.setBorder(PdfPCell.BOX);
			outer.addCell(c1);
			
//			c1 = new PdfPCell(transactionTable());
//			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
//			c1.setBorder(PdfPCell.BOX);
//			outer.addCell(c1);
			
		
			
			
			doc.add(outer);
			doc.add(transactionTable());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	private PdfPTable transactionTable() {
		try {
			double amount=0,qty=0,commision=0,labour=0,remain=0;
			float[] columnWidths = new float[]{5f,13f,15f,15f,6f,9f,10f,11f,8f,12f};
			PdfPTable tr = new PdfPTable(10);
			tr.setWidths(columnWidths);
			
			PdfPCell c1 = new PdfPCell(new Paragraph("Sr.No"));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(PdfPCell.BOX);
			tr.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Date"));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(PdfPCell.BOX);
			tr.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Salesman Name"));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(PdfPCell.BOX);
			tr.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Item Name"));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(PdfPCell.BOX);
			tr.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Rate"));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(PdfPCell.BOX);
			tr.addCell(c1);
			
			
			c1 = new PdfPCell(new Paragraph("Quantity"));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(PdfPCell.BOX);
			tr.addCell(c1);
			
			
			c1 = new PdfPCell(new Paragraph("Amount"));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(PdfPCell.BOX);
			tr.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Commision"));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(PdfPCell.BOX);
			tr.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Labour"));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(PdfPCell.BOX);
			tr.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Remaining"));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(PdfPCell.BOX);
			tr.addCell(c1);
			
			for(ReportTable rt:list)
			{
				c1 = new PdfPCell(new Paragraph(""+rt.getSrNo()));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBorder(PdfPCell.BOX);
				tr.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(""+rt.getDate()));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBorder(PdfPCell.BOX);
				tr.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(rt.getSalesman()));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBorder(PdfPCell.BOX);
				tr.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(rt.getItemName()));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBorder(PdfPCell.BOX);
				tr.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(""+rt.getRate()));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBorder(PdfPCell.BOX);
				tr.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(""+rt.getQty()));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBorder(PdfPCell.BOX);
				tr.addCell(c1);
				
				
				
				c1 = new PdfPCell(new Paragraph(""+rt.getAmount()));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBorder(PdfPCell.BOX);
				tr.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(""+rt.getCommision()));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBorder(PdfPCell.BOX);
				tr.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(""+rt.getLabour()));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBorder(PdfPCell.BOX);
				tr.addCell(c1);
				
				c1 = new PdfPCell(new Paragraph(""+rt.getRemain()));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBorder(PdfPCell.BOX);
				tr.addCell(c1);
				amount=amount+rt.getAmount();
				qty=qty+rt.getQty();
				commision=commision+rt.getCommision();
				labour=labour+rt.getLabour();
				remain=remain+rt.getRemain();
			}
			
			c1 = new PdfPCell(new Paragraph(""));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(PdfPCell.BOX);
			c1.setColspan(5);
			tr.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Qty",footerFont));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(PdfPCell.BOX);			
			tr.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Amount",footerFont));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(PdfPCell.BOX);			
			tr.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Commision",footerFont));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(PdfPCell.BOX);			
			tr.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Labour",footerFont));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(PdfPCell.BOX);			
			tr.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph("Remain",footerFont));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(PdfPCell.BOX);			
			tr.addCell(c1);
			
			
			c1 = new PdfPCell(new Paragraph("Total",footerFont));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(PdfPCell.BOX);
			c1.setColspan(5);
			tr.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph(""+qty,footerFont));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(PdfPCell.BOX);			
			tr.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph(""+amount,footerFont));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(PdfPCell.BOX);			
			tr.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph(""+commision,footerFont));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(PdfPCell.BOX);			
			tr.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph(""+labour,footerFont));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(PdfPCell.BOX);			
			tr.addCell(c1);
			
			c1 = new PdfPCell(new Paragraph(""+remain,footerFont));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(PdfPCell.BOX);			
			tr.addCell(c1);
			
			return tr;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	 
}
