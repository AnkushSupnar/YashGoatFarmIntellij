package main.java.main.java.print;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import main.java.main.java.hibernate.entities.Bill;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;

public class WeeklySalesReportPrint {
    public static String filename = "D:\\Software\\Prints\\WeeklySalesReport.pdf";
    private static Font font = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private static Font smallfont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
    List<Bill> billList;
    LocalDate start,end;
    public WeeklySalesReportPrint(List<Bill>billList, LocalDate start, LocalDate end)
    {
        try{
            this.billList = billList;
            this.start = start;
            this.end = end;
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
        }catch(Exception e){
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

        c1 = new PdfPCell(new Paragraph("Sales Report", font));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(0);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);
        doc.add(table);

        float[] columnWidths = new float[]{10f,20f,14f,15f,17f,25f,30f};
        PdfPTable data = new PdfPTable(7);
        data.setWidths(columnWidths);

        c1 = new PdfPCell(new Paragraph("Report Date : "+start+" To "+end, smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(0);
        c1.setColspan(7);
        c1.setBorder(PdfPCell.NO_BORDER);
        data.addCell(c1);

        addHeader(data);
        int sr=0;
        float amount=0.0f,paid=0.0f;
        for(Bill bill:billList)
        {
            amount=amount+(bill.getNettotal()+bill.getTransportingchrges()+bill.getOtherchargs());
            System.out.println(bill.getNettotal());
            System.out.println(bill.getTransportingchrges());
            System.out.println(bill.getOtherchargs());
            paid +=bill.getRecivedamount();
            c1 = new PdfPCell(new Paragraph(""+(++sr), smallfont));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(0);
            c1.setBorder(PdfPCell.BOX);
            data.addCell(c1);

            c1 = new PdfPCell(new Paragraph(""+bill.getDate(), smallfont));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(0);
            c1.setBorder(PdfPCell.BOX);
            data.addCell(c1);

            c1 = new PdfPCell(new Paragraph(""+bill.getBillno(), smallfont));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(0);
            c1.setBorder(PdfPCell.BOX);
            data.addCell(c1);

            c1 = new PdfPCell(new Paragraph(""+(bill.getNettotal()+bill.getTransportingchrges()+bill.getOtherchargs()), smallfont));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(0);
            c1.setBorder(PdfPCell.BOX);
            data.addCell(c1);

            c1 = new PdfPCell(new Paragraph(""+bill.getRecivedamount(), smallfont));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(0);
            c1.setBorder(PdfPCell.BOX);
            data.addCell(c1);

            c1 = new PdfPCell(new Paragraph(""+bill.getBank().getBankname(), smallfont));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(0);
            c1.setBorder(PdfPCell.BOX);
            data.addCell(c1);

            c1 = new PdfPCell(new Paragraph(""+bill.getCustomer().getFname()+" "+bill.getCustomer().getMname()+" "+bill.getCustomer().getLname(), smallfont));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(0);
            c1.setBorder(PdfPCell.BOX);
            data.addCell(c1);
        }
        c1 = new PdfPCell(new Paragraph("Total", smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.setBorder(0);
        c1.setColspan(3);
        c1.setBorder(PdfPCell.BOX);
        data.addCell(c1);

        c1 = new PdfPCell(new Paragraph(""+amount, smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.setBorder(0);
        c1.setBorder(PdfPCell.BOX);
        data.addCell(c1);

        c1 = new PdfPCell(new Paragraph(""+paid, smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.setBorder(0);
        c1.setBorder(PdfPCell.BOX);
        data.addCell(c1);

        c1 = new PdfPCell(new Paragraph("", smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.setBorder(0);
        c1.setColspan(2);
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

        c1 = new PdfPCell(new Paragraph("Amount", smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(0);
        c1.setBorder(PdfPCell.BOX);
        data.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Recieved Amount", smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(0);
        c1.setBorder(PdfPCell.BOX);
        data.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Bank Name", smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(0);
        c1.setBorder(PdfPCell.BOX);
        data.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Customer Name", smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(0);
        c1.setBorder(PdfPCell.BOX);
        data.addCell(c1);


    }
}
