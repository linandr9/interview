package com.ke.consultant;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;


public class CreatePDF {
    static final FilenameFilter IMAGE_FILTER = new FilenameFilter() {

        @Override
        public boolean accept(final File dir, final String name) {
            return name.endsWith(".jpg");
        }
    };
    static Path inputPath = Paths.get("", "Input\\");
    static final File dir = new File(inputPath.toString());

    public static void main(String[] args) {
        try {
            Document document = new Document();
            document.setMargins(0,0,100,0);
            PdfWriter.getInstance(document, new FileOutputStream("Output/Pics.pdf"));
            document.open();
            if (dir.isDirectory()) {
                for (final File f : Objects.requireNonNull(dir.listFiles(IMAGE_FILTER))) {
                    Image img = Image.getInstance(String.valueOf(f));
                    img.scaleToFit(595, 842);
                    document.add(img);
                }
                document.close();
            }
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }


}

