package main.java.main.java.print;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import main.java.main.java.hibernate.entities.PurchaseInvoice;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;

public class PurchasePartyReportPrint {
    public static String filename = "D:\\Software\\Prints\\PurchasePartyReport.pdf";
    private static Font font = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private static Font smallfont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
    List<PurchaseInvoice>list;
    public PurchasePartyReportPrint(List<PurchaseInvoice>list)
    {
        this.list= list;
        try {
            float left = 0;
            float right = 0;
            float top = 0;
            float bottom = 0;
            Document doc = new Document(PageSize.A4, left, right, top, bottom);
            PdfWriter.getInstance(doc, new FileOutputStream(filename));
            doc.open();
            addContent(doc);
            doc.close();
            System.out.println("Write Done");
        }catch(Exception e)
        {

        }
    }

    private void addContent(Document doc) throws DocumentException {
        PdfPTable table = new PdfPTable(1);

        PdfPCell c1 = new PdfPCell(new Paragraph("Yash Goat Farm & Seeds", font));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(0);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Purchase Party Report", font));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(0);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Report date"+ LocalDate.now(), smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(0);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);
        doc.add(table);

        float[] columnWidths = new float[]{5f,8f,12f,10f,40f,10f,10f};
        PdfPTable data = new PdfPTable(7);
        data.setWidths(columnWidths);
        addHeader(data);
        int sr=0;
        Float amount=0.0f,paid=0.0f;

        for(PurchaseInvoice tr:list)
        {
            c1 = new PdfPCell(new Paragraph(""+(++sr), smallfont));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setBorder(0);
            c1.setBorder(PdfPCell.BOX);
            data.addCell(c1);

            c1 = new PdfPCell(new Paragraph(""+tr.getBillno(), smallfont));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setBorder(0);
            c1.setBorder(PdfPCell.BOX);
            data.addCell(c1);

            c1 = new PdfPCell(new Paragraph(""+tr.getDate(), smallfont));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(0);
            c1.setBorder(PdfPCell.BOX);
            data.addCell(c1);

            c1 = new PdfPCell(new Paragraph(""+tr.getInvoiceNo(), smallfont));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(0);
            c1.setBorder(PdfPCell.BOX);
            data.addCell(c1);

            c1 = new PdfPCell(new Paragraph(""+tr.getParty().getName(), smallfont));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(0);
            c1.setBorder(PdfPCell.BOX);
            data.addCell(c1);

            c1 = new PdfPCell(new Paragraph(""+tr.getGrandtotal(), smallfont));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setBorder(0);
            c1.setBorder(PdfPCell.BOX);
            data.addCell(c1);

            c1 = new PdfPCell(new Paragraph(""+tr.getPaid(), smallfont));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setBorder(0);
            c1.setBorder(PdfPCell.BOX);
            data.addCell(c1);

            amount+=tr.getGrandtotal();
            paid+=tr.getPaid();
        }
        c1 = new PdfPCell(new Paragraph("Total  ", smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.setBorder(0);
        c1.setColspan(5);
        c1.setBorder(PdfPCell.BOX);
        data.addCell(c1);

        c1 = new PdfPCell(new Paragraph(""+amount, smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(0);
        c1.setBorder(PdfPCell.BOX);
        data.addCell(c1);

        c1 = new PdfPCell(new Paragraph(""+paid, smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(0);
        c1.setBorder(PdfPCell.BOX);
        data.addCell(c1);


        doc.add(data);

    }
    private void addHeader(PdfPTable data) {
        PdfPCell c1 = new PdfPCell(new Paragraph("Sr.No", smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(0);
        c1.setBorder(PdfPCell.BOX);
        data.addCell(c1);

        c1 = new PdfPCell(new Paragraph("BillNo", smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(0);
        c1.setBorder(PdfPCell.BOX);
        data.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Date", smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(0);
        c1.setBorder(PdfPCell.BOX);
        data.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Invoice No", smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(0);
        c1.setBorder(PdfPCell.BOX);
        data.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Party Name",smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(0);
        c1.setBorder(PdfPCell.BOX);
        data.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Amount", smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(0);
        c1.setBorder(PdfPCell.BOX);
        data.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Paid Amount", smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(0);
        c1.setBorder(PdfPCell.BOX);
        data.addCell(c1);
    }
}
