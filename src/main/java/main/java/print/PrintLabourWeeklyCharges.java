package main.java.main.java.print;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import main.java.main.java.hibernate.entities.LabourCharges;
import main.java.main.java.hibernate.entities.LabourChargesTransaction;
import main.java.main.java.hibernate.service.service.LabourChargesService;
import main.java.main.java.hibernate.service.serviceImpl.LabourChargesServiceImpl;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;

public class PrintLabourWeeklyCharges {

    private List<LabourCharges>list;
    LocalDate start,end;
    long empid;

    Document doc;
    public static String filename = "D:\\Software\\Prints\\WeeklyLabouCharges.pdf";

    private static Font head = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
    private static Font subhead = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.NORMAL);
    private static Font footerFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
    LabourChargesService service;
   public PrintLabourWeeklyCharges(long empid,LocalDate start,LocalDate end)
    {
        this.service = new LabourChargesServiceImpl();
        this.start = start;
        this.end = end;
        this.empid = empid;
        list = service.getPeriodWiseLabourChargesByEmployee(start,end,empid);


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

        c1 = new PdfPCell(new Paragraph("Weekly Labour Charges Report", subhead));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(PdfPCell.BOTTOM);
        table.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Labour Name-  " +
                list.get(0).getLabour().getFname()+" "+list.get(0).getLabour().getMname()+" "+list.get(0).getLabour().getLname()
                , footerFont));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Report Date-  " + start + " to " + end, footerFont));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Payment Bank-  " + list.get(0).getBank().getBankname(), footerFont));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Transaction No-  "+list.get(0).getBankReffNo(),footerFont));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

        PdfPTable outer = new PdfPTable(1);
        c1 = new PdfPCell(table);
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.BOX);
        outer.addCell(c1);

        //outer.addCell(transactionTable());
//        c1 = new PdfPCell(transactionTable());
//        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
//        c1.setBorder(PdfPCell.BOX);
//        outer.addCell(c1);

        doc.add(outer);
        doc.add(transactionTable());
    }

    private PdfPTable transactionTable() throws DocumentException {
        float[] columnWidths = new float[] { 6f, 12f, 20f, 10f, 10f,9f,15f };
        PdfPTable tr = new PdfPTable(7);
        tr.setWidths(columnWidths);

        PdfPCell c1 = new PdfPCell(new Paragraph("Sr.No"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(PdfPCell.BOX);
        tr.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Date"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(PdfPCell.BOX);
        tr.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Item Name"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(PdfPCell.BOX);
        tr.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Qty"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(PdfPCell.BOX);
        tr.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Charges"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(PdfPCell.BOX);
        tr.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Paid"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(PdfPCell.BOX);
        tr.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Bank"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(PdfPCell.BOX);
        tr.addCell(c1);
        int sr=0;
        float qty=0,c=0,p=0;
        for(LabourCharges lc:list)
        {
            for(LabourChargesTransaction t:lc.getTransaction())
            {
                c1 = new PdfPCell(new Paragraph(""+(++sr)));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                c1.setBorder(PdfPCell.BOX);
                tr.addCell(c1);

                c1 = new PdfPCell(new Paragraph(""+lc.getDate()));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                c1.setBorder(PdfPCell.BOX);
                tr.addCell(c1);

                c1 = new PdfPCell(new Paragraph(t.getItem()));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                c1.setBorder(PdfPCell.BOX);
                tr.addCell(c1);

                c1 = new PdfPCell(new Paragraph(""+t.getQty()));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                c1.setBorder(PdfPCell.BOX);
                tr.addCell(c1);

                c1 = new PdfPCell(new Paragraph(""+t.getCharges()));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                c1.setBorder(PdfPCell.BOX);
                tr.addCell(c1);

                c1 = new PdfPCell(new Paragraph(""+t.getPaidLabourCharges()));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                c1.setBorder(PdfPCell.BOX);
                tr.addCell(c1);

                c1 = new PdfPCell(new Paragraph(""+lc.getBank().getBankname()));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                c1.setBorder(PdfPCell.BOX);
                tr.addCell(c1);
                qty+=t.getQty();
                c+=t.getCharges();
                p+=t.getPaidLabourCharges();
            }
        }
        c1 = new PdfPCell(new Paragraph("Total",footerFont));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.setBorder(PdfPCell.BOX);
        c1.setColspan(3);
        tr.addCell(c1);

        c1 = new PdfPCell(new Paragraph(""+qty,footerFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(PdfPCell.BOX);
        tr.addCell(c1);

        c1 = new PdfPCell(new Paragraph(""+c,footerFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(PdfPCell.BOX);
        tr.addCell(c1);

        c1 = new PdfPCell(new Paragraph(""+p,footerFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(PdfPCell.BOX);
        tr.addCell(c1);

        c1 = new PdfPCell(new Paragraph(""));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(PdfPCell.BOX);
        tr.addCell(c1);
        return tr;
    }
}
