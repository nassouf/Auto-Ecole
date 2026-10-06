package org.example.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.io.image.ImageDataFactory;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;

import org.example.entities.AutoEcoleInfo;
import org.example.entities.Candidat;
import org.example.entities.Paiement;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.nio.file.Paths;

public class PDFGenerator {

    private AutoEcoleInfoServices autoEcoleInfoServices;

    public PDFGenerator() {
        autoEcoleInfoServices = new AutoEcoleInfoServices();
    }

    public byte[] generateReceipt(Paiement paiement, Candidat candidat) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            // Create PDF document with writer
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Get auto-école information
            AutoEcoleInfo autoEcoleInfo = autoEcoleInfoServices.getAutoEcoleInfo(null);

            if (autoEcoleInfo != null) {
                // Add header with auto-école information
                addHeader(document, autoEcoleInfo);
            }

            // Add title
            Paragraph title = new Paragraph("REÇU DE PAIEMENT")
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            // Add spacing
            document.add(new Paragraph("\n"));

            // Add receipt information
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dateStr = paiement.getDatePaiement().format(formatter);

            // Create a table for receipt details
            Table table = new Table(UnitValue.createPercentArray(new float[]{40, 60}));
            table.setWidth(UnitValue.createPercentValue(100));

            // Add receipt details rows
            addTableRow(table, "Numéro de reçu:", String.valueOf(paiement.getId()));
            addTableRow(table, "Date:", dateStr);
            addTableRow(table, "Candidat:", candidat.getNom() + " " + candidat.getPrenom());
            addTableRow(table, "CIN:", String.valueOf(candidat.getCIN()));
            addTableRow(table, "Montant:", String.format("%.2f DT", paiement.getMontant()));

            // Add payment status with color coding
            Cell statusLabel = new Cell().add(new Paragraph("État:")).setBold();
            Cell statusValue = new Cell();
            Paragraph statusText = new Paragraph(paiement.getEtat());

            if ("fait".equalsIgnoreCase(paiement.getEtat())) {
                statusText.setFontColor(ColorConstants.GREEN);
            } else {
                statusText.setFontColor(ColorConstants.RED);
            }

            statusValue.add(statusText);
            table.addCell(statusLabel);
            table.addCell(statusValue);

            if (paiement.isFacilite()) {
                addTableRow(table, "Facilité de paiement:", "Oui");
                addTableRow(table, "Tranche:", String.valueOf(paiement.getNumeroTranche()));
            } else {
                addTableRow(table, "Facilité de paiement:", "Non");
            }

            document.add(table);

            document.add(new Paragraph("\n"));

            // Add footer with auto-école information
            if (autoEcoleInfo != null) {
                addFooter(document, autoEcoleInfo);
            } else {
                Paragraph footer = new Paragraph("Ce reçu est généré automatiquement. Merci de votre paiement.")
                        .setFontSize(10)
                        .setItalic()
                        .setTextAlignment(TextAlignment.CENTER);
                document.add(footer);
            }

            document.close();

            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void addHeader(Document document, AutoEcoleInfo autoEcoleInfo) {
        try {
            // Create header table
            Table headerTable = new Table(UnitValue.createPercentArray(new float[]{30, 70}));
            headerTable.setWidth(UnitValue.createPercentValue(100));

            // Logo cell
            Cell logoCell = new Cell();
            logoCell.setBorder(null);

            // Check if logo path exists and add logo
            String logoPath = autoEcoleInfo.getAutoEcolelogo();
            if (logoPath != null && !logoPath.isEmpty()) {
                try {
                    File logoFile = new File(logoPath);
                    if (logoFile.exists()) {
                        Image logo = new Image(ImageDataFactory.create(logoPath));
                        logo.setWidth(UnitValue.createPercentValue(80));
                        logo.setAutoScale(true);
                        logoCell.add(logo);
                    } else {
                        logoCell.add(new Paragraph("Logo non disponible"));
                    }
                } catch (Exception e) {
                    logoCell.add(new Paragraph("Erreur de chargement du logo"));
                    e.printStackTrace();
                }
            } else {
                logoCell.add(new Paragraph(""));
            }

            // Info cell
            Cell infoCell = new Cell();
            infoCell.setBorder(null);
            infoCell.setVerticalAlignment(VerticalAlignment.MIDDLE);

            // Add auto-école name with bigger font
            Paragraph ecoleNameParagraph = new Paragraph(autoEcoleInfo.getAutoEcoleName())
                    .setBold()
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.RIGHT);
            infoCell.add(ecoleNameParagraph);

            // Add contact info
            Paragraph contactInfo = new Paragraph()
                    .add(autoEcoleInfo.getAutoEcoleAddress() + "\n")
                    .add("Tél: " + autoEcoleInfo.getAutoEcolePhone() + "\n")
                    .add("Email: " + autoEcoleInfo.getAutoEcoleEmail() + "\n")
                    .add("Site web: " + autoEcoleInfo.getAutoEcoleWebsite())
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.RIGHT);
            infoCell.add(contactInfo);

            // Add cells to table
            headerTable.addCell(logoCell);
            headerTable.addCell(infoCell);

            // Add table to document
            document.add(headerTable);

            // Add separator line
            Paragraph line = new Paragraph("_".repeat(120))
                    .setFontColor(ColorConstants.LIGHT_GRAY);
            document.add(line);

            // Add spacing
            document.add(new Paragraph("\n"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addFooter(Document document, AutoEcoleInfo autoEcoleInfo) {
        try {
            // Add separator line
            Paragraph line = new Paragraph("_".repeat(120))
                    .setFontColor(ColorConstants.LIGHT_GRAY);
            document.add(line);

            // Create footer table
            Table footerTable = new Table(1);
            footerTable.setWidth(UnitValue.createPercentValue(100));

            // Add auto-école description if available
            if (autoEcoleInfo.getAutoEcoleDescription() != null && !autoEcoleInfo.getAutoEcoleDescription().isEmpty()) {
                Cell descCell = new Cell();
                descCell.setBorder(null);
                descCell.add(new Paragraph(autoEcoleInfo.getAutoEcoleDescription())
                        .setFontSize(9)
                        .setItalic()
                        .setTextAlignment(TextAlignment.CENTER));
                footerTable.addCell(descCell);
            }

            // Add contact information
            Cell contactCell = new Cell();
            contactCell.setBorder(null);
            contactCell.add(new Paragraph("Contact: " + autoEcoleInfo.getAutoEcolePhone() + " | "
                    + autoEcoleInfo.getAutoEcoleEmail() + " | " + autoEcoleInfo.getAutoEcoleWebsite())
                    .setFontSize(8)
                    .setTextAlignment(TextAlignment.CENTER));
            footerTable.addCell(contactCell);

            // Add thank you message
            Cell thankYouCell = new Cell();
            thankYouCell.setBorder(null);
            thankYouCell.add(new Paragraph("Ce reçu est généré automatiquement. Merci de votre paiement.")
                    .setFontSize(9)
                    .setItalic()
                    .setTextAlignment(TextAlignment.CENTER));
            footerTable.addCell(thankYouCell);

            // Add legal notice
            Cell legalCell = new Cell();
            legalCell.setBorder(null);
            legalCell.add(new Paragraph("© " + java.time.LocalDate.now().getYear() + " " + autoEcoleInfo.getAutoEcoleName() + ". Tous droits réservés.")
                    .setFontSize(8)
                    .setTextAlignment(TextAlignment.CENTER));
            footerTable.addCell(legalCell);

            document.add(footerTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addTableRow(Table table, String label, String value) {
        Cell labelCell = new Cell().add(new Paragraph(label)).setBold();
        Cell valueCell = new Cell().add(new Paragraph(value));
        table.addCell(labelCell);
        table.addCell(valueCell);
    }
}