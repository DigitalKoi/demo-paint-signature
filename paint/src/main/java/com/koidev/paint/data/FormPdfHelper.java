package com.koidev.paint.data;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @author KoiDev
 * @email DevSteelKoi@gmail.com
 */

public class FormPdfHelper {
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL);
    private static Font underFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.UNDERLINE);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

    private final String mTexForm;
    private String mUserName;
    private String mSpouseName;
    private ArrayList<String> mSignatureList = new ArrayList<>();
    private String mUrlToDir;
    private final String mDate;

    public FormPdfHelper(ArrayList<String> signatureList,
                         String urlToDir,
                         String textForm,
                         String userName,
                         String spouseName,
                         String date) {
        mSignatureList.addAll(signatureList);
        mUrlToDir = urlToDir;
        mDate = date;
        mTexForm = textForm;
        mUserName = userName;
        mSpouseName = spouseName;
    }

    public String createPdf() {
        try {
            Document document = new Document(PageSize.A4);
            File file = new File(mUrlToDir + File.separator + UUID.randomUUID().toString() + ".pdf");
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            addMetaData(document);
            addFormPage(document, writer);
            document.close();
            return file.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private void addMetaData(Document document) {
        document.addTitle(UUID.randomUUID().toString());
        document.addAuthor("Digital Koi");
        document.addCreator("Digital Koi");
    }

    private void addFormPage(Document document, PdfWriter writer) throws DocumentException {
        String signSt = "Signature: ______________";
        PdfContentByte canvas = writer.getDirectContentUnder();
        Paragraph paragraph = new Paragraph();
        //prepare names for underlines convert
        mUserName = preparerName(mUserName);
        mSpouseName = preparerName(mSpouseName);
        //create underline for names
        Chunk nameUnderline = new Chunk(mUserName, normalFont);
        nameUnderline.setUnderline(0.8f, -7f);
        Chunk spouseUnderline = new Chunk(mSpouseName, normalFont);
        spouseUnderline.setUnderline(0.8f, -7f);
        //create images
        Image signNameImage = null;
        Image signSposeImage = null;
        try {
            signNameImage = Image.getInstance(mSignatureList.get(0));
            if (!mSignatureList.get(1).equals("")) {
                signSposeImage = Image.getInstance(mSignatureList.get(1));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        paragraph.add(new Paragraph(mTexForm, normalFont));
        paragraph.add(Chunk.NEWLINE);

        paragraph.add(nameUnderline);

        paragraph.add(new Paragraph("Customer's name", normalFont));
        paragraph.add(new Paragraph(signSt, normalFont));
        signNameImage.scaleAbsolute(50, 50);
        signNameImage.setAbsolutePosition(120, 270);
        canvas.addImage(signNameImage);
        paragraph.add(Chunk.NEWLINE);

        paragraph.add(spouseUnderline);

        paragraph.add(new Paragraph("Spouse's name", normalFont));
        paragraph.add(new Paragraph(signSt, normalFont));
        if (!mSignatureList.get(1).equals("")) {
            signSposeImage.scaleAbsolute(50, 50);
            signSposeImage.setAbsolutePosition(120, 200);
            canvas.addImage(signSposeImage);
        }
        paragraph.add(Chunk.NEWLINE);

        paragraph.add(new Paragraph("Residing in:", normalFont));
        paragraph.add(new Paragraph("Date: " + mDate, normalFont));

        document.add(paragraph);
    }

    private String preparerName(String nameString) {
        while (nameString.length() < 40)
            nameString += " ";
        return nameString;
    }

}

