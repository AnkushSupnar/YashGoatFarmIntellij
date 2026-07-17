package main.java.main.java.print;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import main.java.main.java.hibernate.entities.Bank;
import main.java.main.java.hibernate.entities.Bill;
import main.java.main.java.hibernate.entities.Transaction;
import main.java.main.java.hibernate.entities.CompanyDetails;
import main.java.main.java.hibernate.service.service.BankService;
import main.java.main.java.hibernate.service.service.BillService;
import main.java.main.java.hibernate.service.service.ItemService;
import main.java.main.java.hibernate.service.serviceImpl.BankServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.BillServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.CompanyServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.ItemServiceImpl;
import main.java.main.java.hibernate.util.AppSettings;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class GenerateBill {

	public static String filename = "D:\\Software\\Prints\\bill.pdf";

	// ── fonts ────────────────────────────────────────────────────────────────
	private static final Font F_NORMAL  = new Font(Font.FontFamily.TIMES_ROMAN,  9, Font.NORMAL);
	private static final Font F_BOLD    = new Font(Font.FontFamily.TIMES_ROMAN,  9, Font.BOLD);
	private static final Font F_HEAD    = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
	private static final Font F_SMALL   = new Font(Font.FontFamily.TIMES_ROMAN,  8, Font.NORMAL);
	private static final Font F_ITALIC  = new Font(Font.FontFamily.TIMES_ROMAN,  8, Font.ITALIC);
	private static final Font F_COL_HDR = new Font(Font.FontFamily.HELVETICA,    9, Font.BOLD, BaseColor.WHITE);
	private static final Font F_TAX     = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);

	// ── colours ──────────────────────────────────────────────────────────────
	private static final BaseColor C_HDR_BG    = new BaseColor(44,  62,  80);   // dark blue-grey header
	private static final BaseColor C_SUB_BG    = new BaseColor(235, 237, 239);  // light grey for sub-totals
	private static final BaseColor C_GRAND_BG  = new BaseColor(255, 245, 157);  // yellow for Grand Total
	private static final BaseColor C_CASH      = new BaseColor(27,  94,  32);
	private static final BaseColor C_CREDIT    = new BaseColor(183, 28,  28);

	// ── fields ───────────────────────────────────────────────────────────────
	private final float[] COL_WIDTHS = {7f, 34f, 8f, 11f, 9f, 10f, 9f, 12f};

	private BillService   billService;
	private BankService   bankService;
	private ItemService   itemService;
	private Bill          bill;
	private Bank          bank;
	private CompanyDetails company;
	long billno;

	// ── constructor ──────────────────────────────────────────────────────────
	public GenerateBill(long billno) {
		try {
			this.billno  = billno;
			billService  = new BillServiceImpl();
			itemService  = new ItemServiceImpl();
			bill         = billService.getBillByBillno(billno);
			if (bill == null) return;
			bankService  = new BankServiceImpl();
			bank = bill.getBank() != null ? bankService.getBankById(bill.getBank().getId()) : null;
			try { company = new CompanyServiceImpl().getCompanyDetails(1); } catch (Exception ignored) {}

			Document doc = new Document(PageSize.A4, 36, 36, 20, 20);
			PdfWriter.getInstance(doc, new FileOutputStream(filename));
			doc.open();
			addContent(doc);
			doc.close();
			System.out.println("Write Done");
		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) { new GenerateBill(189); }

	// ═════════════════════════════════════════════════════════════════════════
	//  MAIN CONTENT
	// ═════════════════════════════════════════════════════════════════════════
	void addContent(Document doc) {
		try {
			PdfPTable page = new PdfPTable(1);
			page.setWidthPercentage(100);

			// ── 1. Letterhead image ───────────────────────────────────────
			try {
				Image img = Image.getInstance("D:\\Software\\Images\\Yash Bill Head.png");
				PdfPCell ic = new PdfPCell(img, true);
				ic.setHorizontalAlignment(Element.ALIGN_CENTER);
				ic.setBorder(PdfPCell.BOX);
				ic.setPadding(0);
				page.addCell(ic);
			} catch (Exception ignored) {}

			// ── 2. GST No / PAN No row ───────────────────────────────────
			String gstTxt = (company != null && nne(company.getGst()))   ? "GST No: " + company.getGst()   : "GST No: —";
			String panTxt = (company != null && nne(company.getPanNo())) ? "PAN No: " + company.getPanNo() : "PAN No: —";
			PdfPTable taxRow = new PdfPTable(2);
			PdfPCell gc = cell(gstTxt, F_TAX, Element.ALIGN_CENTER, PdfPCell.BOX, 5, null); taxRow.addCell(gc);
			PdfPCell pc = cell(panTxt, F_TAX, Element.ALIGN_CENTER, PdfPCell.BOX, 5, null); taxRow.addCell(pc);
			page.addCell(wrap(taxRow));

			// ── 3. Payment status banner ──────────────────────────────────
			float bTotal      = bill.getNettotal() + bill.getTransportingchrges() + bill.getOtherchargs();
			float outstanding = bTotal - bill.getRecivedamount();
			Font bannerFont   = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
			String bannerTxt; BaseColor bannerBg;
			if (outstanding <= 0.01f) {
				bannerTxt = "CASH PAYMENT";
				bannerBg  = C_CASH;
			} else if (bill.getRecivedamount() <= 0.01f) {
				bannerTxt = "CREDIT BILL";
				bannerBg  = C_CREDIT;
			} else {
				bannerTxt = "PARTIAL PAYMENT  —  Paid: Rs. " + fmt(bill.getRecivedamount())
						+ "   |   Outstanding: Rs. " + fmt(outstanding);
				bannerBg = C_CREDIT;
			}
			PdfPCell banner = cell(bannerTxt, bannerFont, Element.ALIGN_CENTER, PdfPCell.BOX, 7, bannerBg);
			page.addCell(banner);

			// ── 4. Customer info  +  Bill meta ───────────────────────────
			PdfPTable custRow = new PdfPTable(new float[]{55f, 45f});
			custRow.setWidthPercentage(100);

			// left: customer block
			StringBuilder sb = new StringBuilder();
			sb.append("To,\n");
			sb.append(safe(bill.getCustomer().getFname())).append(" ")
			  .append(safe(bill.getCustomer().getMname())).append(" ")
			  .append(safe(bill.getCustomer().getLname())).append("\n");
			if (nne(bill.getCustomer().getAddress()))
				sb.append(bill.getCustomer().getAddress()).append(", ").append(safe(bill.getCustomer().getCity())).append("\n");
			sb.append(safe(bill.getCustomer().getTaluka())).append(", ")
			  .append(safe(bill.getCustomer().getDistrict())).append(", ")
			  .append(safe(bill.getCustomer().getState())).append(" — ")
			  .append(bill.getCustomer().getPin()).append("\n");
			sb.append("Contact: ").append(safe(bill.getCustomer().getMobileno()));
			String cGst = bill.getCustomer().getGstno();
			String cPan = bill.getCustomer().getPanno();
			if (cGst != null && !cGst.isEmpty() && !cGst.equals("-")) sb.append("\nGSTIN: ").append(cGst);
			if (cPan != null && !cPan.isEmpty() && !cPan.equals("-")) sb.append("    PAN: ").append(cPan);
			custRow.addCell(cell(sb.toString(), F_NORMAL, Element.ALIGN_LEFT, PdfPCell.BOX, 6, null));

			// right: meta grid
			PdfPTable meta = new PdfPTable(new float[]{42f, 58f});
			addKV(meta, "Invoice No.", "" + bill.getBillno());
			addKV(meta, "Date",        "" + bill.getDate());
			addKV(meta, "Transport",   "");
			PdfPCell metaWrap = new PdfPCell(meta);
			metaWrap.setBorder(PdfPCell.BOX);
			metaWrap.setPadding(0);
			custRow.addCell(metaWrap);

			page.addCell(wrap(custRow));

			// ── 5. Items table ────────────────────────────────────────────
			PdfPTable item = new PdfPTable(8);
			item.setWidthPercentage(100);
			item.setWidths(COL_WIDTHS);

			// header row
			String[] hTxt   = {"Sr.", "Description of Goods", "HSN", "Qty", "Unit", "Rate", "GST %", "Amount"};
			int[]    hAlign = {Element.ALIGN_CENTER, Element.ALIGN_LEFT, Element.ALIGN_CENTER,
			                   Element.ALIGN_CENTER, Element.ALIGN_CENTER, Element.ALIGN_RIGHT,
			                   Element.ALIGN_CENTER, Element.ALIGN_RIGHT};
			for (int i = 0; i < 8; i++) {
				PdfPCell h = cell(hTxt[i], F_COL_HDR, hAlign[i], PdfPCell.BOX, 5, C_HDR_BG);
				item.addCell(h);
			}

			// data rows
			int sr = 0;
			for (Transaction tr : bill.getTransaction()) {
				String hsn  = "";
				try { hsn = safe(itemService.getItemByName(tr.getItemname()).getHsn()); } catch (Exception ignored) {}
				String igst = tr.getIgstPercent() > 0 ? String.format("%.1f%%", tr.getIgstPercent()) : "—";

				itemCell(item, "" + (++sr),                          Element.ALIGN_CENTER);
				itemCell(item, tr.getItemname(),                     Element.ALIGN_LEFT);
				itemCell(item, hsn,                                  Element.ALIGN_CENTER);
				itemCell(item, "" + tr.getQuantity(),                Element.ALIGN_CENTER);
				itemCell(item, "" + tr.getUnit(),                    Element.ALIGN_CENTER);
				itemCell(item, fmt(tr.getRate()),                    Element.ALIGN_RIGHT);
				itemCell(item, igst,                                 Element.ALIGN_CENTER);
				itemCell(item, fmt(tr.getAmount()),                  Element.ALIGN_RIGHT);
			}

			// filler rows (min 8 rows total)
			for (int i = sr; i < Math.max(8, sr); i++) {
				for (int j = 0; j < 8; j++) {
					PdfPCell ec = new PdfPCell(new Paragraph(" "));
					ec.setBorder(PdfPCell.LEFT | PdfPCell.RIGHT);
					ec.setFixedHeight(18);
					item.addCell(ec);
				}
			}

			// horizontal rule closing the item rows
			PdfPCell rule = new PdfPCell(new Paragraph(""));
			rule.setColspan(8);
			rule.setBorder(PdfPCell.TOP);
			rule.setFixedHeight(1);
			item.addCell(rule);

			// goods category note
			PdfPCell goods = cell(
				"FODDER SEEDS  —  NON TAXABLE GOODS  —  EXEMPTED FROM VAT  —  AGRICULTURE PRODUCE",
				F_ITALIC, Element.ALIGN_CENTER, PdfPCell.BOX, 4, C_SUB_BG);
			goods.setColspan(8);
			item.addCell(goods);

			// packaging block (left 4 cols, spans 5 rows) + totals (right 4 cols)
			PdfPTable pkg = new PdfPTable(new float[]{65f, 35f});
			pkg.setWidthPercentage(100);
			pkgRow(pkg, "No. of Bags");
			pkgRow(pkg, "CC Attach");
			pkgRow(pkg, "To Pay / Paid");
			PdfPCell pkgCell = new PdfPCell(pkg);
			pkgCell.setColspan(4);
			pkgCell.setRowspan(5);
			pkgCell.setBorder(PdfPCell.BOX);
			pkgCell.setPadding(0);
			item.addCell(pkgCell);

			float grandTotal = bill.getNettotal() + bill.getIgstTotal()
					+ bill.getOtherchargs() + bill.getTransportingchrges();
			totalRow(item, "Net Total",         fmt(bill.getNettotal()),             F_BOLD, C_SUB_BG);
			totalRow(item, "IGST Total",        fmt(bill.getIgstTotal()),            F_BOLD, null);
			totalRow(item, "Other Charges",     fmt(bill.getOtherchargs()),          F_BOLD, null);
			totalRow(item, "Transport Charges", fmt(bill.getTransportingchrges()),   F_BOLD, null);
			totalRow(item, "Grand Total",       fmt(grandTotal),                     F_HEAD, C_GRAND_BG);

			PdfPCell itemWrap = new PdfPCell(item);
			itemWrap.setBorder(PdfPCell.BOX);
			itemWrap.setPadding(0);
			page.addCell(itemWrap);

			// ── 6. Footer ─────────────────────────────────────────────────
			PdfPCell footCell = new PdfPCell(buildFooter());
			footCell.setBorder(PdfPCell.NO_BORDER);
			footCell.setPadding(0);
			page.addCell(footCell);

			doc.add(page);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ═════════════════════════════════════════════════════════════════════════
	//  FOOTER  (bank details + stamp/signature)
	// ═════════════════════════════════════════════════════════════════════════
	public PdfPTable buildFooter() {
		try {
			String coName = (company != null && nne(company.getName())) ? company.getName() : "Yash Goat Farm And Seeds";

			// signature / stamp nested table
			PdfPTable sig = new PdfPTable(1);
			sig.setWidthPercentage(100);

			Paragraph p1 = new Paragraph("For " + coName, F_SMALL);
			p1.setAlignment(Element.ALIGN_CENTER);
			PdfPCell sc = new PdfPCell(p1);
			sc.setBorder(PdfPCell.NO_BORDER);
			sc.setPadding(4);
			sig.addCell(sc);

			AppSettings.StampConfig stamp = AppSettings.loadStampConfig();
			boolean stampDrawn = false;
			if (stamp != null && stamp.isUsable()) {
				try {
					Image stampImg = Image.getInstance(stamp.imagePath);
					stampImg.scaleAbsolute(stamp.widthPt, stamp.heightPt);
					PdfPCell sc2 = new PdfPCell(stampImg, false);
					sc2.setHorizontalAlignment(Element.ALIGN_CENTER);
					sc2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					sc2.setFixedHeight(Math.max(40f, stamp.heightPt + 6f));
					sc2.setBorder(PdfPCell.NO_BORDER);
					sc2.setPadding(2);
					sig.addCell(sc2);
					stampDrawn = true;
				} catch (Exception ignored) {}
			}
			if (!stampDrawn) {
				PdfPCell sp = new PdfPCell(new Paragraph(" "));
				sp.setFixedHeight(45);
				sp.setBorder(PdfPCell.NO_BORDER);
				sig.addCell(sp);
			}

			Paragraph p2 = new Paragraph("Authorised Signatory", F_SMALL);
			p2.setAlignment(Element.ALIGN_CENTER);
			PdfPCell sc3 = new PdfPCell(p2);
			sc3.setBorder(PdfPCell.TOP);
			sc3.setPadding(4);
			sig.addCell(sc3);

			// footer table: label | value | sig (rowspan=5)
			PdfPTable footer = new PdfPTable(new float[]{14f, 26f, 60f});
			footer.setWidthPercentage(100);

			PdfPCell sigWrap = new PdfPCell(sig);
			sigWrap.setBorder(PdfPCell.BOX);
			sigWrap.setPadding(0);
			sigWrap.setRowspan(5);

			bankRow(footer, "Bank Details", "",                                                            sigWrap, true);
			bankRow(footer, "Bank Name",    coName,                                                        null,   false);
			bankRow(footer, "IFSC Code",    bank != null && bank.getIfsc()      != null ? bank.getIfsc()      : "—", null, false);
			bankRow(footer, "Account No.",  bank != null && bank.getAccountno() != null ? bank.getAccountno() : "—", null, false);
			bankRow(footer, "Bank Branch",  bank != null && bank.getBranch()    != null ? bank.getBranch()    : "—", null, false);

			return footer;
		} catch (Exception e) {
			return null;
		}
	}

	// ═════════════════════════════════════════════════════════════════════════
	//  HELPERS
	// ═════════════════════════════════════════════════════════════════════════

	/** Bold label | plain value row in a 2-col meta table */
	private void addKV(PdfPTable t, String label, String value) {
		PdfPCell lc = new PdfPCell(new Paragraph(label, F_BOLD));
		lc.setBorder(PdfPCell.BOX); lc.setPadding(4);
		t.addCell(lc);
		PdfPCell vc = new PdfPCell(new Paragraph(value, F_NORMAL));
		vc.setBorder(PdfPCell.BOX); vc.setPadding(4);
		t.addCell(vc);
	}

	/** Single item table cell */
	private void itemCell(PdfPTable t, String text, int align) {
		PdfPCell c = new PdfPCell(new Paragraph(text == null ? "" : text, F_NORMAL));
		c.setHorizontalAlignment(align);
		c.setBorder(PdfPCell.BOX);
		c.setPadding(4);
		t.addCell(c);
	}

	/** Packaging label | blank value row */
	private void pkgRow(PdfPTable t, String label) {
		PdfPCell lc = new PdfPCell(new Paragraph(label, F_BOLD));
		lc.setBorder(PdfPCell.BOX); lc.setPadding(5); lc.setFixedHeight(22);
		t.addCell(lc);
		PdfPCell vc = new PdfPCell(new Paragraph(" "));
		vc.setBorder(PdfPCell.BOX); vc.setPadding(5); vc.setFixedHeight(22);
		t.addCell(vc);
	}

	/** Totals row spanning the right 4 columns (colspan 2 + colspan 2) */
	private void totalRow(PdfPTable t, String label, String value, Font f, BaseColor bg) {
		PdfPCell lc = new PdfPCell(new Paragraph(label, f));
		lc.setColspan(2); lc.setHorizontalAlignment(Element.ALIGN_RIGHT);
		lc.setBorder(PdfPCell.BOX); lc.setPadding(4); lc.setFixedHeight(20);
		if (bg != null) lc.setBackgroundColor(bg);
		t.addCell(lc);
		PdfPCell vc = new PdfPCell(new Paragraph(value, f));
		vc.setColspan(2); vc.setHorizontalAlignment(Element.ALIGN_RIGHT);
		vc.setBorder(PdfPCell.BOX); vc.setPadding(4); vc.setFixedHeight(20);
		if (bg != null) vc.setBackgroundColor(bg);
		t.addCell(vc);
	}

	/** Bank detail row; sigWrap (non-null only for first row) goes into col 3 */
	private void bankRow(PdfPTable t, String label, String value, PdfPCell sigWrap, boolean first) {
		PdfPCell lc = new PdfPCell(new Paragraph(label, F_BOLD));
		lc.setBorder(PdfPCell.BOX); lc.setPadding(4); lc.setFixedHeight(16);
		t.addCell(lc);
		PdfPCell vc = new PdfPCell(new Paragraph(value, F_SMALL));
		vc.setBorder(PdfPCell.BOX); vc.setPadding(4); vc.setFixedHeight(16);
		t.addCell(vc);
		if (first && sigWrap != null) t.addCell(sigWrap);
	}

	/** Generic styled cell */
	private PdfPCell cell(String text, Font f, int align, int border, float pad, BaseColor bg) {
		PdfPCell c = new PdfPCell(new Paragraph(text, f));
		c.setHorizontalAlignment(align);
		c.setBorder(border);
		c.setPadding(pad);
		if (bg != null) c.setBackgroundColor(bg);
		return c;
	}

	/** Wraps a nested PdfPTable in a no-padding BOX cell */
	private PdfPCell wrap(PdfPTable t) {
		PdfPCell c = new PdfPCell(t);
		c.setBorder(PdfPCell.BOX);
		c.setPadding(0);
		return c;
	}

	/** True if string is non-null and non-empty */
	private boolean nne(String s) { return s != null && !s.isEmpty(); }

	private String safe(String s) { return s == null ? "" : s; }

	private String fmt(float v) { return String.format("%.2f", v); }
}
