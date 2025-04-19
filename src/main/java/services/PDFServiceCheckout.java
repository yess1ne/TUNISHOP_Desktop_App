package services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import entities.Checkout;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class PDFServiceCheckout {

    public static void exportCommandeToPDF(List<Checkout> checkouts) throws Exception {
        if (checkouts == null || checkouts.isEmpty()) return;

        Checkout first = checkouts.get(0);
        String fileName = "commande_" + first.getCheckoutId() + ".pdf";

        String userHome = System.getProperty("user.home");
        String downloadPath = userHome + File.separator + "Downloads" + File.separator + fileName;

        Document doc = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter.getInstance(doc, new FileOutputStream(downloadPath));
        doc.open();

        // Fonts
        Font logoTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLACK);
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        // Logo
        try {
            String logoPath = "C:\\Users\\yassin\\Desktop\\TUNISHOP_Desktop_App4\\src\\main\\resources\\TuniShop_Logo.jpg";
            Image logo = Image.getInstance(logoPath);
            logo.scaleToFit(100, 100);
            logo.setAlignment(Element.ALIGN_CENTER);
            doc.add(logo);
        } catch (Exception e) {
            System.err.println("⚠️ Logo manquant : " + e.getMessage());
        }

        // Title
        Paragraph title = new Paragraph("Checkout Invoice", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        doc.add(title);

        doc.add(Chunk.NEWLINE);

        // Client Info
        doc.add(new Paragraph("Name: ", labelFont));
        doc.add(new Paragraph(first.getFirstName() + " " + first.getSecondName(), contentFont));
        doc.add(new Paragraph("Email: ", labelFont));
        doc.add(new Paragraph(first.getEmail(), contentFont));
        doc.add(new Paragraph("City: ", labelFont));
        doc.add(new Paragraph(first.getCity(), contentFont));
        doc.add(new Paragraph("Checkout ID: " + first.getCheckoutId(), labelFont));

        doc.add(Chunk.NEWLINE);

        // Table
        Paragraph sectionTitle = new Paragraph("Products Ordered:", labelFont);
        sectionTitle.setSpacingAfter(10);
        doc.add(sectionTitle);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{3, 1});

        PdfPCell cell1 = new PdfPCell(new Phrase("Product", labelFont));
        PdfPCell cell2 = new PdfPCell(new Phrase("Price", labelFont));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell1);
        table.addCell(cell2);

        double total = 0;
        for (Checkout c : checkouts) {
            String titleVal = c.getProduit() != null ? c.getProduit().getTitle() : "(deleted)";
            double price = c.getProduit() != null ? c.getProduit().getPrice() : 0;
            total += price;
            table.addCell(new PdfPCell(new Phrase(titleVal, contentFont)));
            table.addCell(new PdfPCell(new Phrase("$" + price, contentFont)));
        }

        PdfPCell totalLabel = new PdfPCell(new Phrase("Total Produits:", labelFont));
        totalLabel.setColspan(1);
        totalLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(totalLabel);
        PdfPCell totalValue = new PdfPCell(new Phrase("$" + total, labelFont));
        totalValue.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(totalValue);

        double livraison = 10.0;
        PdfPCell shippingLabel = new PdfPCell(new Phrase("Frais de livraison:", labelFont));
        shippingLabel.setColspan(1);
        shippingLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(shippingLabel);
        PdfPCell shippingValue = new PdfPCell(new Phrase("$" + livraison, labelFont));
        shippingValue.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(shippingValue);

        PdfPCell ttcLabel = new PdfPCell(new Phrase("Total TTC:", labelFont));
        ttcLabel.setColspan(1);
        ttcLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(ttcLabel);
        PdfPCell ttcValue = new PdfPCell(new Phrase("$" + (total + livraison), labelFont));
        ttcValue.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(ttcValue);

        doc.add(table);
        doc.close();

        System.out.println("✅ PDF saved in: " + downloadPath);
    }
}