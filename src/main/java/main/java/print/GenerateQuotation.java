package main.java.main.java.print;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import main.java.main.java.hibernate.entities.Bank;
import main.java.main.java.hibernate.entities.CompanyDetails;
import main.java.main.java.hibernate.entities.Quotation;
import main.java.main.java.hibernate.entities.QuotationTransaction;
import main.java.main.java.hibernate.service.service.CompanyService;
import main.java.main.java.hibernate.service.service.QuotationService;
import main.java.main.java.hibernate.service.serviceImpl.CompanyServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.QuotationServiceImpl;
import main.java.main.java.hibernate.util.AppSettings;

public class GenerateQuotation {

	private static final Font TITLE = new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.BOLD);
	private static final Font SUBTITLE = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
	private static final Font HEAD = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
	private static final Font NORMAL = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
	private static final Font SMALL = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL);
	private static final Font SMALL_BOLD = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD);
	private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final String LETTERHEAD_IMAGE = "D:\\Software\\Images\\Yash Bill Head.png";

	private final long quotationId;
	private final String outputPath;

	public GenerateQuotation(long quotationId, String outputPath) {
		this.quotationId = quotationId;
		this.outputPath = outputPath;
		generate();
	}

	private void generate() {
		QuotationService qs = new QuotationServiceImpl();
		Quotation q = qs.getQuotationById(quotationId);
		if (q == null) {
			System.out.println("Quotation not found: " + quotationId);
			return;
		}
		CompanyDetails company = null;
		try {
			CompanyService cs = new CompanyServiceImpl();
			company = cs.getCompanyDetails(1);
		} catch (Exception ignored) {}

		Document doc = new Document(PageSize.A4, 25, 25, 25, 25);
		try {
			PdfWriter.getInstance(doc, new FileOutputStream(outputPath));
			doc.open();
			doc.add(buildHeader(company));
			doc.add(buildCustomerAndMeta(q));
			doc.add(buildItemsTable(q));
			doc.add(buildBoilerplate());
			doc.add(buildTotalsTable(q));
			doc.add(buildStatusTable(q, company));
			doc.add(buildBankFooter(q.getBank(), company));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { doc.close(); } catch (Exception ignored) {}
		}
	}

	private PdfPTable buildHeader(CompanyDetails company) {
		PdfPTable t = new PdfPTable(1);
		t.setWidthPercentage(100);

		boolean imageAdded = false;
		try {
			Image image = Image.getInstance(LETTERHEAD_IMAGE);
			PdfPCell ic = new PdfPCell(image, true);
			ic.setHorizontalAlignment(Element.ALIGN_CENTER);
			ic.setBorder(PdfPCell.NO_BORDER);
			t.addCell(ic);
			imageAdded = true;
		} catch (Exception ignored) {
		}

		if (!imageAdded) {
			String companyName = company != null ? company.getName() : "YASH GOAT FARM & SEEDS";
			Paragraph sub = new Paragraph(companyName, SUBTITLE);
			sub.setAlignment(Element.ALIGN_CENTER);
			PdfPCell c = new PdfPCell(sub);
			c.setBorder(PdfPCell.NO_BORDER);
			t.addCell(c);

			if (company != null) {
				StringBuilder addr = new StringBuilder();
				if (company.getAddress() != null) addr.append(company.getAddress());
				if (company.getTaluka() != null) addr.append(", Taluka-").append(company.getTaluka());
				if (company.getDistrict() != null) addr.append(", ").append(company.getDistrict());
				if (company.getPin() != null) addr.append(" ").append(company.getPin());
				Paragraph p = new Paragraph(addr.toString(), NORMAL);
				p.setAlignment(Element.ALIGN_CENTER);
				c = new PdfPCell(p);
				c.setBorder(PdfPCell.NO_BORDER);
				t.addCell(c);

				StringBuilder contact = new StringBuilder();
				if (company.getEmail() != null && !company.getEmail().isEmpty()) contact.append("Email: ").append(company.getEmail());
				if (company.getContact() != null && !company.getContact().isEmpty()) {
					if (contact.length() > 0) contact.append("   |   ");
					contact.append("Mobile: ").append(company.getContact());
				}
				if (company.getAltercontact() != null && !company.getAltercontact().isEmpty()) {
					contact.append(" / ").append(company.getAltercontact());
				}
				if (contact.length() > 0) {
					Paragraph cp = new Paragraph(contact.toString(), NORMAL);
					cp.setAlignment(Element.ALIGN_CENTER);
					c = new PdfPCell(cp);
					c.setBorder(PdfPCell.NO_BORDER);
					t.addCell(c);
				}
			}
		}

		Paragraph title = new Paragraph("QUOTATION", TITLE);
		title.setAlignment(Element.ALIGN_CENTER);
		PdfPCell tc = new PdfPCell(title);
		tc.setBorder(PdfPCell.NO_BORDER);
		tc.setHorizontalAlignment(Element.ALIGN_CENTER);
		tc.setPaddingTop(4);
		t.addCell(tc);

		return t;
	}

	private PdfPTable buildCustomerAndMeta(Quotation q) {
		PdfPTable t = new PdfPTable(2);
		t.setWidthPercentage(100);
		t.setSpacingBefore(8);

		StringBuilder left = new StringBuilder();
		left.append("To,\n");
		if (q.getCustomer() != null) {
			left.append(safe(q.getCustomer().getFname())).append(" ")
					.append(safe(q.getCustomer().getMname())).append(" ")
					.append(safe(q.getCustomer().getLname())).append("\n");
			if (q.getCustomer().getAddress() != null) left.append(q.getCustomer().getAddress()).append("\n");
			StringBuilder line = new StringBuilder();
			if (q.getCustomer().getTaluka() != null) line.append(q.getCustomer().getTaluka());
			if (q.getCustomer().getDistrict() != null) {
				if (line.length() > 0) line.append(", ");
				line.append(q.getCustomer().getDistrict());
			}
			if (q.getCustomer().getState() != null) {
				if (line.length() > 0) line.append(", ");
				line.append(q.getCustomer().getState());
			}
			if (q.getCustomer().getPin() != 0) {
				if (line.length() > 0) line.append(" - ");
				line.append(q.getCustomer().getPin());
			}
			if (line.length() > 0) left.append(line).append("\n");
			if (q.getCustomer().getMobileno() != null) left.append("Contact No: ").append(q.getCustomer().getMobileno());
		}
		PdfPCell c = new PdfPCell(new Paragraph(left.toString(), NORMAL));
		c.setBorder(PdfPCell.BOX);
		c.setPadding(6);
		t.addCell(c);

		PdfPTable meta = new PdfPTable(1);
		meta.addCell(metaRow("Quotation No: " + formatQuotationNo(q)));
		meta.addCell(metaRow("Date: " + (q.getDate() != null ? q.getDate().format(DATE_FMT) : "")));
		meta.addCell(metaRow("Valid Until: " + (q.getValidUntil() != null ? q.getValidUntil().format(DATE_FMT) : "As per terms")));
		String transport = (q.getNotes() == null || q.getNotes().isEmpty()) ? "To Be Arranged" : q.getNotes();
		meta.addCell(metaRow("Transport: " + transport));

		c = new PdfPCell(meta);
		c.setBorder(PdfPCell.BOX);
		c.setPadding(0);
		t.addCell(c);

		return t;
	}

	private PdfPCell metaRow(String text) {
		PdfPCell c = new PdfPCell(new Paragraph(text, NORMAL));
		c.setBorder(PdfPCell.NO_BORDER);
		c.setPadding(4);
		return c;
	}

	private String formatQuotationNo(Quotation q) {
		int year = (q.getDate() != null ? q.getDate().getYear() : LocalDate.now().getYear());
		return "YGFS/" + year + "/" + q.getId();
	}

	private PdfPTable buildItemsTable(Quotation q) throws Exception {
		PdfPTable t = new PdfPTable(new float[] { 8f, 35f, 10f, 12f, 10f, 12f, 8f, 15f });
		t.setWidthPercentage(100);
		t.setSpacingBefore(8);

		addHeader(t, "Sr.No");
		addHeader(t, "Description");
		addHeader(t, "HSN");
		addHeader(t, "Quantity");
		addHeader(t, "Unit");
		addHeader(t, "Rate");
		addHeader(t, "GST %");
		addHeader(t, "Amount");

		int sr = 0;
		if (q.getTransaction() != null) {
			for (QuotationTransaction tr : q.getTransaction()) {
				addCell(t, "" + (++sr), Element.ALIGN_CENTER);
				addCell(t, tr.getItemname(), Element.ALIGN_LEFT);
				addCell(t, tr.getHsn() != null ? tr.getHsn() : "-", Element.ALIGN_CENTER);
				addCell(t, "" + tr.getQuantity(), Element.ALIGN_CENTER);
				addCell(t, tr.getUnit() != null ? tr.getUnit() : "-", Element.ALIGN_CENTER);
				addCell(t, "" + tr.getRate(), Element.ALIGN_CENTER);
				addCell(t, "0%", Element.ALIGN_CENTER);
				addCell(t, "" + tr.getAmount(), Element.ALIGN_RIGHT);
			}
		}
		int filled = sr;
		for (int i = filled; i < Math.max(6, filled); i++) {
			for (int j = 0; j < 8; j++) {
				PdfPCell c = new PdfPCell(new Paragraph(" ", NORMAL));
				c.setFixedHeight(18);
				c.setBorder(PdfPCell.LEFT | PdfPCell.RIGHT);
				t.addCell(c);
			}
		}
		return t;
	}

	private void addHeader(PdfPTable t, String text) {
		PdfPCell c = new PdfPCell(new Paragraph(text, HEAD));
		c.setBackgroundColor(new BaseColor(230, 230, 230));
		c.setHorizontalAlignment(Element.ALIGN_CENTER);
		c.setBorder(PdfPCell.BOX);
		c.setPadding(4);
		t.addCell(c);
	}

	private void addCell(PdfPTable t, String text, int align) {
		PdfPCell c = new PdfPCell(new Paragraph(text == null ? "" : text, NORMAL));
		c.setHorizontalAlignment(align);
		c.setBorder(PdfPCell.LEFT | PdfPCell.RIGHT);
		c.setPadding(3);
		t.addCell(c);
	}

	private PdfPTable buildBoilerplate() {
		PdfPTable t = new PdfPTable(1);
		t.setWidthPercentage(100);
		PdfPCell c = new PdfPCell();
		c.setBorder(PdfPCell.LEFT | PdfPCell.RIGHT | PdfPCell.BOTTOM);
		c.setPadding(4);
		Paragraph p = new Paragraph();
		p.add(new Phrase("FODDER SEEDS \u2014 NON TAXABLE GOODS \u2014 EXEMPTED FROM VAT \u2014 AGRICULTURE PRODUCE", SMALL_BOLD));
		c.addElement(p);
		t.addCell(c);
		return t;
	}

	private PdfPTable buildTotalsTable(Quotation q) {
		PdfPTable t = new PdfPTable(new float[] { 70f, 30f });
		t.setWidthPercentage(100);
		t.setSpacingBefore(6);

		addTotalRow(t, "Net Total", q.getNettotal(), false);
		addTotalRow(t, "Other Charges", q.getOtherchargs(), false);
		addTotalRow(t, "Transp. Charges", q.getTransportingchrges(), false);
		addTotalRow(t, "Estimated Total", q.getNettotal() + q.getOtherchargs() + q.getTransportingchrges(), true);
		return t;
	}

	private void addTotalRow(PdfPTable t, String label, float value, boolean bold) {
		Font f = bold ? HEAD : NORMAL;
		PdfPCell c = new PdfPCell(new Paragraph(label, f));
		c.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c.setBorder(PdfPCell.BOX);
		c.setPadding(4);
		t.addCell(c);
		c = new PdfPCell(new Paragraph("" + value, f));
		c.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c.setBorder(PdfPCell.BOX);
		c.setPadding(4);
		t.addCell(c);
	}

	private PdfPTable buildStatusTable(Quotation q, CompanyDetails company) {
		PdfPTable t = new PdfPTable(new float[] { 25f, 75f });
		t.setWidthPercentage(100);
		t.setSpacingBefore(6);

		addStatusRow(t, "No. OF BAGS", "-");
		addStatusRow(t, "CC ATTACH", "-");
		addStatusRow(t, "STATUS", q.getStatus() == null || q.getStatus().isEmpty() ? "ESTIMATE / QUOTATION" : q.getStatus());

		if (company != null && company.getGst() != null && !company.getGst().isEmpty()) {
			Paragraph p = new Paragraph("GSTIN: " + company.getGst(), SMALL_BOLD);
			PdfPCell c = new PdfPCell(p);
			c.setColspan(2);
			c.setBorder(PdfPCell.BOX);
			c.setPadding(4);
			t.addCell(c);
		}
		return t;
	}

	private void addStatusRow(PdfPTable t, String label, String value) {
		PdfPCell c = new PdfPCell(new Paragraph(label, SMALL_BOLD));
		c.setBorder(PdfPCell.BOX);
		c.setPadding(4);
		t.addCell(c);
		c = new PdfPCell(new Paragraph(value, NORMAL));
		c.setBorder(PdfPCell.BOX);
		c.setPadding(4);
		t.addCell(c);
	}

	private PdfPTable buildBankFooter(Bank bank, CompanyDetails company) {
		PdfPTable t = new PdfPTable(new float[] { 60f, 40f });
		t.setWidthPercentage(100);
		t.setSpacingBefore(10);

		StringBuilder b = new StringBuilder();
		b.append("Our Bank Details:\n");
		if (bank != null) {
			b.append("Name: ").append(safe(bank.getBankname())).append("\n");
			if (bank.getBranch() != null) b.append("Branch: ").append(bank.getBranch()).append("\n");
			if (bank.getAccountno() != null) b.append("Account No: ").append(bank.getAccountno()).append("\n");
			if (bank.getIfsc() != null) b.append("IFSC Code: ").append(bank.getIfsc());
		}
		PdfPCell c = new PdfPCell(new Paragraph(b.toString(), NORMAL));
		c.setBorder(PdfPCell.BOX);
		c.setPadding(6);
		t.addCell(c);

		PdfPTable sig = new PdfPTable(1);
		String name = company != null ? company.getName() : "Yash Goat Farm & Seeds";
		Paragraph p1 = new Paragraph("For " + name, HEAD);
		p1.setAlignment(Element.ALIGN_CENTER);
		PdfPCell sc = new PdfPCell(p1);
		sc.setBorder(PdfPCell.NO_BORDER);
		sc.setPadding(4);
		sig.addCell(sc);

		PdfPCell space;
		AppSettings.StampConfig stamp = AppSettings.loadStampConfig();
		boolean stampDrawn = false;
		if (stamp != null && stamp.isUsable()) {
			try {
				Image stampImg = Image.getInstance(stamp.imagePath);
				stampImg.scaleAbsolute(stamp.widthPt, stamp.heightPt);
				space = new PdfPCell(stampImg, false);
				space.setHorizontalAlignment(Element.ALIGN_CENTER);
				space.setVerticalAlignment(Element.ALIGN_MIDDLE);
				space.setFixedHeight(Math.max(40f, stamp.heightPt + 6f));
				space.setBorder(PdfPCell.NO_BORDER);
				space.setPadding(2);
				sig.addCell(space);
				stampDrawn = true;
			} catch (Exception ignored) {
			}
		}
		if (!stampDrawn) {
			space = new PdfPCell(new Paragraph(" ", NORMAL));
			space.setFixedHeight(40);
			space.setBorder(PdfPCell.NO_BORDER);
			sig.addCell(space);
		}

		Paragraph p2 = new Paragraph("Authorised Signatory", SMALL);
		p2.setAlignment(Element.ALIGN_CENTER);
		PdfPCell sc2 = new PdfPCell(p2);
		sc2.setBorder(PdfPCell.TOP);
		sc2.setPadding(4);
		sig.addCell(sc2);

		c = new PdfPCell(sig);
		c.setBorder(PdfPCell.BOX);
		c.setPadding(0);
		t.addCell(c);

		return t;
	}

	private String safe(String s) { return s == null ? "" : s; }
}
