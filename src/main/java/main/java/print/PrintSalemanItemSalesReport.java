package main.java.main.java.print;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import main.java.main.java.hibernate.entities.Transaction;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;

public class PrintSalemanItemSalesReport {
    public static String filename = "D:\\Software\\Prints\\SalesmanItemSalesReport.pdf";
    private static Font font = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private static Font smallfont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
    List<Transaction>list;
    String emp;
    LocalDate start,end;
    public PrintSalemanItemSalesReport(List<Transaction>list,String emp,LocalDate start,LocalDate end)
    {
        this.emp = emp;
        this.end = end;
        this.list = list;
        this.start = start;
        try {
            float left = 30;
            float right = 0;
            float top = 20;
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

        c1 = new PdfPCell(new Paragraph("Salesman Sales Report", font));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(0);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);
        doc.add(table);

        float[] columnWidths = new float[]{10f,12f,20f,40f, 15f,10f,15f};
        PdfPTable data = new PdfPTable(7);
        data.setWidths(columnWidths);
        c1 = new PdfPCell(new Paragraph("Salesman Name : "+emp, smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(0);
        c1.setColspan(7);
        c1.setBorder(PdfPCell.NO_BORDER);
        data.addCell(c1);

        String d="";
        if(end==null)
         d = ""+start;
        else d=start +" to "+end;

        c1 = new PdfPCell(new Paragraph("Report Date : "+d, smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(0);
        c1.setColspan(7);
        c1.setBorder(PdfPCell.NO_BORDER);
        data.addCell(c1);

        addHeader(data);
        int i=0;
        float qty=0,amount=0;
        for(Transaction tr:list)
        {
            c1 = new PdfPCell(new Paragraph(""+(++i), smallfont));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(0);
            c1.setBorder(PdfPCell.BOX);
            data.addCell(c1);

            c1 = new PdfPCell(new Paragraph(""+tr.getBill().getBillno(), smallfont));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(0);
            c1.setBorder(PdfPCell.BOX);
            data.addCell(c1);

            c1 = new PdfPCell(new Paragraph(""+tr.getBill().getDate(), smallfont));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(0);
            c1.setBorder(PdfPCell.BOX);
            data.addCell(c1);

            c1 = new PdfPCell(new Paragraph(""+tr.getItemname(), smallfont));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(0);
            c1.setBorder(PdfPCell.BOX);
            data.addCell(c1);

            c1 = new PdfPCell(new Paragraph(""+tr.getQuantity(), smallfont));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(0);
            c1.setBorder(PdfPCell.BOX);
            data.addCell(c1);

            c1 = new PdfPCell(new Paragraph(""+tr.getRate(), smallfont));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(0);
            c1.setBorder(PdfPCell.BOX);
            data.addCell(c1);

            c1 = new PdfPCell(new Paragraph(""+tr.getAmount(), smallfont));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(0);
            c1.setBorder(PdfPCell.BOX);
            data.addCell(c1);

            qty+=tr.getQuantity();
            amount+=tr.getAmount();

        }
        if(list.size()> 1 && !list.get(0).getItemname().equals(list.get(1).getItemname()))
        {
            qty=0;
        }
            c1 = new PdfPCell(new Paragraph(" ",smallBold));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(0);
            c1.setColspan(7);
            c1.setBorder(PdfPCell.BOX);
            data.addCell(c1);

            c1 = new PdfPCell(new Paragraph("Total",smallBold));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            c1.setBorder(0);
            c1.setColspan(4);
            c1.setBorder(PdfPCell.BOX);
            data.addCell(c1);

            c1 = new PdfPCell(new Paragraph(""+qty,smallBold));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(0);
            c1.setBorder(PdfPCell.BOX);
            data.addCell(c1);

        c1 = new PdfPCell(new Paragraph("",smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(0);
        c1.setBorder(PdfPCell.BOX);
        data.addCell(c1);

            c1 = new PdfPCell(new Paragraph(""+amount,smallBold));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
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

        c1 = new PdfPCell(new Paragraph("Item Name", smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(0);
        c1.setBorder(PdfPCell.BOX);
        data.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Qty",smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(0);
        c1.setBorder(PdfPCell.BOX);
        data.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Rate", smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(0);
        c1.setBorder(PdfPCell.BOX);
        data.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Amount", smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(0);
        c1.setBorder(PdfPCell.BOX);
        data.addCell(c1);
    }
}
