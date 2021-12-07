package main.java.main.java.print;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import main.java.main.java.hibernate.entities.LabourCharges;
import main.java.main.java.hibernate.service.service.LabourChargesService;
import main.java.main.java.hibernate.service.serviceImpl.LabourChargesServiceImpl;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrintWeeklyAllLabourReport {
    List<LabourCharges>list;
    LocalDate start,end;
    Document doc;
    public static String filename = "D:\\Software\\Prints\\WeeklyAllLabouCharges.pdf";
    private LabourChargesService labourService;
    private static Font head = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
    private static Font subhead = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.NORMAL);
    private static Font footerFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
    public PrintWeeklyAllLabourReport(LocalDate start, LocalDate end) {
       labourService = new LabourChargesServiceImpl();
       list = new ArrayList<>();
        list.addAll(labourService.getPeriodWiseLabourCharges(start,end));
        this.start = start;
        this.end=end;

        float left = 0, right = 0, top = 20, bottom = 0;
        doc = new Document(PageSize.A4, left, right, top, bottom);
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(filename));
            doc.open();

            addData();
            doc.close();
            System.out.println("Writting Done");
        }catch (Exception e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    private void addData() throws DocumentException {
        PdfPTable table = new PdfPTable(1);
        PdfPCell c1 = new PdfPCell(new Paragraph("Yash Goat Farm & Seeds", head));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell(new Paragraph("All Labour Charges Report", subhead));
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


        doc.add(outer);
       // doc.add(transactionTable());
        PdfPTable tr = new PdfPTable(5);
        float[] columnWidths = new float[] { 6f, 12f, 20f, 10f, 10f};
        tr.setWidths(columnWidths);

        c1 = new PdfPCell(new Paragraph("Sr.No" ));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.BOX);
        tr.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Date" ));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.BOX);
        tr.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Labour Name" ));
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

        float total=0;
        for(LabourCharges t:list)
        {
            total+=t.getAmount();
            c1 = new PdfPCell(new Paragraph(""+list.indexOf(t)+1 ));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(PdfPCell.BOX);
            tr.addCell(c1);

            c1 = new PdfPCell(new Paragraph(""+t.getDate() ));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(PdfPCell.BOX);
            tr.addCell(c1);

            c1 = new PdfPCell(new Paragraph(t.getLabour().getFname()+" "+t.getLabour().getMname()+""+t.getLabour().getLname()));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(PdfPCell.BOX);
            tr.addCell(c1);

            c1 = new PdfPCell(new Paragraph(""+t.getAmount()));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(PdfPCell.BOX);
            tr.addCell(c1);

            c1 = new PdfPCell(new Paragraph(""+t.getBank().getBankname()));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(PdfPCell.BOX);
            tr.addCell(c1);
        }

        c1 = new PdfPCell(new Paragraph("Total",footerFont));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.setBorder(PdfPCell.BOX);
        c1.setColspan(3);
        tr.addCell(c1);

        c1 = new PdfPCell(new Paragraph(""+total,footerFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(PdfPCell.BOX);
        tr.addCell(c1);

        c1 = new PdfPCell(new Paragraph(""));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.BOX);
        tr.addCell(c1);
        doc.add(tr);
    }
}
