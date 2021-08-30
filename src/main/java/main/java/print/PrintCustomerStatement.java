package main.java.main.java.print;

import com.itextpdf.text.Font;
import main.java.main.java.hibernate.entities.CustomerAdvancePayment;

import java.util.List;

public class PrintCustomerStatement {
    public static String filename = "D:\\Software\\Prints\\PurchaseStatement.pdf";
    private static Font font = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private static Font smallfont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

    List<CustomerAdvancePayment>list;
}
