package main.java.main.java.print;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import main.java.main.java.hibernate.reportEntity.ItemSaleReportPojo;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;

public class PrintMonthlyItemSaleReport {
    public static String filename = "D:\\Software\\Prints\\ItemSalesReport.pdf";
    private static Font font = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private static Font smallfont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
    List<ItemSaleReportPojo>list;
    LocalDate start,end;
    public PrintMonthlyItemSaleReport(List<ItemSaleReportPojo>list,LocalDate start,LocalDate end)
    {
        this.end = end;
        this.start = start;
        this.list = list;
        try{
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
            e.printStackTrace();
        }
    }
    private void addContent(Document doc) throws DocumentException {
        PdfPTable table = new PdfPTable(1);

        PdfPCell c1 = new PdfPCell(new Paragraph("Yash Goat Farm & Seeds", font));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(0);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Item Sales Report", font));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(0);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);
        doc.add(table);

        float[] columnWidths = new float[]{10f,18f,12,40f,10f, 15f,12f,15f};
        PdfPTable data = new PdfPTable(8);
        data.setWidths(columnWidths);

        c1 = new PdfPCell(new Paragraph("Report Date : "+start + " to "+end, smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(0);
        c1.setColspan(8);
        c1.setBorder(PdfPCell.NO_BORDER);
        data.addCell(c1);

        addHeader(data);
        int i=0;
        float qty=0,amount=0;
        for(ItemSaleReportPojo tr:list)
        {
            c1 = new PdfPCell(new Paragraph(""+(++i), smallfont));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(0);
            c1.setBorder(PdfPCell.BOX);
            data.addCell(c1);

            c1 = new PdfPCell(new Paragraph(""+tr.getDate(), smallfont));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(0);
            c1.setBorder(PdfPCell.BOX);
            data.addCell(c1);

            c1 = new PdfPCell(new Paragraph(""+tr.getBillno(), smallfont));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(0);
            c1.setBorder(PdfPCell.BOX);
            data.addCell(c1);

            c1 = new PdfPCell(new Paragraph(""+tr.getItemName(), smallfont));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(0);
            c1.setBorder(PdfPCell.BOX);
            data.addCell(c1);

            c1 = new PdfPCell(new Paragraph(""+tr.getUnit(), smallfont));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(0);
            c1.setBorder(PdfPCell.BOX);
            data.addCell(c1);

            c1 = new PdfPCell(new Paragraph(""+tr.getQty(), smallfont));
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
            qty+=tr.getQty();
            amount+=tr.getAmount();
        }
        if(list.size()>1 && !list.get(0).getItemName().equals(list.get(1).getItemName()))
        qty=0;

        c1 = new PdfPCell(new Paragraph("Total", smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.setBorder(0);
        c1.setColspan(5);
        c1.setBorder(PdfPCell.BOX);
        data.addCell(c1);

        c1 = new PdfPCell(new Paragraph(""+qty, smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(0);
        c1.setBorder(PdfPCell.BOX);
        data.addCell(c1);

        c1 = new PdfPCell(new Paragraph("", smallfont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(0);
        c1.setBorder(PdfPCell.BOX);
        data.addCell(c1);

        c1 = new PdfPCell(new Paragraph(""+amount, smallfont));
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

        c1 = new PdfPCell(new Paragraph("Date", smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(0);
        c1.setBorder(PdfPCell.BOX);
        data.addCell(c1);

        c1 = new PdfPCell(new Paragraph("BillNo", smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(0);
        c1.setBorder(PdfPCell.BOX);
        data.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Item Name", smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(0);
        c1.setBorder(PdfPCell.BOX);
        data.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Unit",smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(0);
        c1.setBorder(PdfPCell.BOX);
        data.addCell(c1);


        c1 = new PdfPCell(new Paragraph("Qty", smallBold));
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
