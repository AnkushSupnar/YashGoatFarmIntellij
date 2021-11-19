package main.java.main.java.print;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import main.java.main.java.hibernate.entities.Commision;
import main.java.main.java.hibernate.entities.Employee;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;

public class PrintAllSalesmanMasterReport {
    public static String filename = "D:\\Software\\Prints\\AllSalesmanMaster.pdf";
    float[] columnWidths = new float[]{8f,35f,8f,12f,10f,10f,10f,10f};
    private Employee employee;
    Document doc;
    private static Font head = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
    private static Font subhead = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.NORMAL);
    private static Font footerFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
    private List<Commision>list;
    private LocalDate start,end;
    public PrintAllSalesmanMasterReport(List<Commision> list, LocalDate start, LocalDate end)
    {
        this.list = list;
        this.start = start;
        this.end = end;
        float left = 0, right = 0, top = 20, bottom = 0;
        doc = new Document(PageSize.A4, left, right, top, bottom);
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(filename));
            doc.open();
            addData(doc);
            doc.close();
            System.out.println("Writting Done");
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void addData(Document doc) throws DocumentException {
        PdfPTable table = new PdfPTable(1);
        PdfPCell c1 = new PdfPCell(new Paragraph("Yash Goat Farm & Seeds", head));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Salesman Commission Report", subhead));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(PdfPCell.BOTTOM);
        table.addCell(c1);

        if(start==null && end==null)
        c1 = new PdfPCell(new Paragraph("Report Date-  All Date", footerFont));
        else
            c1 = new PdfPCell(new Paragraph("Report Date-  " + start + " to " + end, footerFont));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

        PdfPTable outer = new PdfPTable(1);
        c1 = new PdfPCell(table);
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.BOX);
        outer.addCell(c1);
        doc.add(outer);

        PdfPTable tr = new PdfPTable(5);
        float[] columnWidths = new float[] { 5f, 10f, 25f, 8f, 20f};
        tr.setWidths(columnWidths);

        c1 = new PdfPCell(new Paragraph("Sr.No" ));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.BOX);
        tr.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Date" ));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.BOX);
        tr.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Salesman Name" ));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.BOX);
        tr.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Amount" ));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.BOX);
        tr.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Bank Name" ));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.BOX);
        tr.addCell(c1);
        //adding data
        int sr=0;
        float total=0;
        for(Commision t:list)
        {
            c1 = new PdfPCell(new Paragraph(""+(++sr) ));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(PdfPCell.BOX);
            tr.addCell(c1);

            c1 = new PdfPCell(new Paragraph(""+t.getDate()));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(PdfPCell.BOX);
            tr.addCell(c1);

            c1 = new PdfPCell(new Paragraph(""+t.getEmployee().getFname()+" "+t.getEmployee().getMname()+" "+t.getEmployee().getLname()));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(PdfPCell.BOX);
            tr.addCell(c1);

            c1 = new PdfPCell(new Paragraph(""+t.getPaidCommision()));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(PdfPCell.BOX);
            tr.addCell(c1);

            c1 = new PdfPCell(new Paragraph(""+t.getBank().getBankname()));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(PdfPCell.BOX);
            tr.addCell(c1);
            total+=t.getPaidCommision();
        }
        c1 = new PdfPCell(new Paragraph("Total  ",footerFont ));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(PdfPCell.BOX);
        c1.setColspan(3);
        tr.addCell(c1);

        c1 = new PdfPCell(new Paragraph(""+total ,footerFont));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.BOX);
        c1.setColspan(2);
        tr.addCell(c1);
        doc.add(tr);
    }
}
